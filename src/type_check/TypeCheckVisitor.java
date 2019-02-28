package type_check;

import ast.node.dec.class_.ClassDec;
import ast.node.dec.function.FunctDec;
import ast.node.dec.variable.VarDec;
import ast.node.exp.*;
import ast.node.exp.binary.ArithBinaryExp;
import ast.node.exp.binary.LogicBinaryExp;
import ast.node.exp.lvalue.ArrayAccessExp;
import ast.node.exp.lvalue.FieldAccessExp;
import ast.node.exp.lvalue.ThisExp;
import ast.node.exp.unary.PrefixExp;
import ast.node.exp.unary.SuffixExp;
import ast.node.prog.Prog;
import ast.node.stm.ForControl;
import ast.node.stm.IfStm;
import ast.node.stm.ReturnStm;
import ast.node.stm.WhileStm;
import ast.typeref.ArrayTypeRef;
import ast.typeref.VarTypeRef;
import type_check.symbol_table.Symbol;

import java.util.Hashtable;
import java.util.Stack;

/**
 * This is a visitor that runs through a threaded abstract syntax tree.
 * It takes the previous funct/classTables and does type-checking.
 *
 * Jobs:
 *  1. check operation type compatibility and assign type for operation results.
 *  2. check undefined function/method(appear to be function) in functionCall,
 *    and check its arguments types.
 *  3. assign return types for function/method invoking expressions, which cannot be
 *    done in the previous phase because of function forward reference.
 *  4. 'this' can't be assigned
 *  5. array index type should be integer, its initializer needs to be all integer type.
 *  6. void type cannot be created
 *
 * significant pitfalls are as follows:
 *  1. method and function invoking appear the same inside a class scope.
 *  2. null can't be assigned to int, bool, string.
 *  3. void can only appear as function returnType.
 * */
public class TypeCheckVisitor extends AstTraverseVisitor<Void> {
  /**
   * Create association from class's literal name to its actual classDec.
   * */
  private Hashtable<Symbol, ClassDec> classTable;

  /**
   * Create association from function's literal name to its actual functionDec.
   * */
  private Hashtable<Symbol, FunctDec> functTable;

  public Hashtable<Symbol, ClassDec> getClassTable() {
    return classTable;
  }

  public Hashtable<Symbol, FunctDec> getFunctTable() {
    return functTable;
  }

  /**
   * Note that methods inside class scope appears the same as functions, thus we need to
   * search for it first in class scope, then in global scope.
   * */
  private Stack<ClassDec> curClassStack = new Stack<>();

  public TypeCheckVisitor(TypeThreadVisitor typeThreadVisitor) {
    classTable = typeThreadVisitor.getClassTable();
    functTable = typeThreadVisitor.getFunctTable();
  }

  /**
   * invoked by AssignExp for type-checking.
   * These functions are used for expression type checking,
   * which has nothing to do with void type, don't care about it.
   * */
  private boolean Assignable(VarTypeRef lhs, VarTypeRef rhs) {
    if (rhs.isNull()) {
      if (lhs.isInt() || lhs.isBool() || lhs.isString())
        return false;
      else
        return true;
    }
    else {
      return lhs.Equal(rhs);
    }
  }

  private boolean Comparable(VarTypeRef lhs, VarTypeRef rhs) {
    return Assignable(lhs, rhs);
  }

  /**
   * for void returnType, even null can't be returned.
   * */
  private boolean Returnable(VarTypeRef lhs, Exp rhs) {
    if (lhs.isVoid())
      return rhs == null; // null can't be returned for void returnType.
    else
      return rhs != null && Assignable(lhs, rhs.varTypeRefDec);
  }

  /**
   * Go!
   * */
  public void ExpTypeCheck(Prog prog) {
    visit(prog);
  }

  @Override
  public Void visit(ClassDec classDec) {
    curClassStack.push(classDec);
    super.visit(classDec);
    curClassStack.pop();
    return null;
  }

  /**
   * '+' is overloaded for string concatenation.
   * Comparison is overloaded for string in dictionary-order.
   * */
  @Override
  public Void visit(ArithBinaryExp arithBinaryExp) {
    super.visit(arithBinaryExp);
    VarTypeRef lhs = arithBinaryExp.lhs.varTypeRefDec;
    VarTypeRef rhs = arithBinaryExp.rhs.varTypeRefDec;
    arithBinaryExp.varTypeRefDec = new VarTypeRef();
    switch (arithBinaryExp.op) {
      // arithmetic
      case "*": case "/":
      case "-": case "%":
      case "<<": case ">>":
        if (!(lhs.isInt() && rhs.isInt()))
          throw new RuntimeException("ArithBinaryExp type error...\n" + arithBinaryExp.LocationToString());
        arithBinaryExp.varTypeRefDec.typeName = "int";
        break;
      case "+":
        if (!(lhs.isInt() && rhs.isInt() || lhs.isString() && rhs.isString()))
          throw new RuntimeException("ArithBinaryExp type error...\n" + arithBinaryExp.LocationToString());
        arithBinaryExp.varTypeRefDec = lhs;
        break;
      case ">": case ">=":
      case "<": case "<=":
        if (!(lhs.isInt() && rhs.isInt() || lhs.isString() && rhs.isString()))
          throw new RuntimeException("ArithBinaryExp type error...\n" + arithBinaryExp.LocationToString());
        arithBinaryExp.varTypeRefDec.typeName = "bool";
        break;
      case "==": case "!=":
        if (!Comparable(lhs, rhs))
          throw new RuntimeException("ArithBinaryExp type error...\n" + arithBinaryExp.LocationToString());
        arithBinaryExp.varTypeRefDec.typeName = "bool";
        break;
      case "&": case "^": case "|":
        if (!(lhs.isInt() && rhs.isInt() || lhs.isBool() && rhs.isBool()))
          throw new RuntimeException("ArithBinaryExp type error...\n" + arithBinaryExp.LocationToString());
        arithBinaryExp.varTypeRefDec.typeName = lhs.typeName;
        break;
      default:
        throw new RuntimeException("Parser error...\n" + arithBinaryExp.LocationToString());
    }
    return null;
  }

  @Override
  public Void visit(LogicBinaryExp logicBinaryExp) {
    super.visit(logicBinaryExp);
    VarTypeRef lhs = logicBinaryExp.lhs.varTypeRefDec;
    VarTypeRef rhs = logicBinaryExp.rhs.varTypeRefDec;
    if (!(lhs.isBool() && rhs.isBool()))
      throw new RuntimeException("logicBinaryExp type error...\n" + logicBinaryExp.LocationToString());
    logicBinaryExp.varTypeRefDec = lhs;
    return null;
  }


  /**
   * ! is for logic only.
   * ~ can be logic or integer.
   * */
  @Override
  public Void visit(PrefixExp prefixExp) {
    super.visit(prefixExp);
    switch (prefixExp.op) {
      case "!":
        if (!prefixExp.objInstance.varTypeRefDec.isBool())
          throw new RuntimeException("prefixExp type error...\n" + prefixExp.LocationToString());
        break;
      case "~":
        if (!(prefixExp.objInstance.varTypeRefDec.isBool() || prefixExp.objInstance.varTypeRefDec.isInt()))
          throw new RuntimeException("prefixExp type error...\n" + prefixExp.LocationToString());
        break;
      default:
        if (!prefixExp.objInstance.varTypeRefDec.isInt())
          throw new RuntimeException("prefixExp type error...\n" + prefixExp.LocationToString());
        break;
    }
    prefixExp.varTypeRefDec = prefixExp.objInstance.varTypeRefDec;
    return null;
  }

  @Override
  public Void visit(SuffixExp suffixExp) {
    super.visit(suffixExp);
    if (!suffixExp.objInstance.varTypeRefDec.isInt())
      throw new RuntimeException("suffixExp type error...\n" + suffixExp.LocationToString());
    suffixExp.varTypeRefDec = suffixExp.objInstance.varTypeRefDec;
    return null;
  }


  /**
   * Distinguish method (inside class) from function.
   * Check parameter matching.
   * */
  @Override
  public Void visit(FunctCallExp functCallExp) {
    super.visit(functCallExp);
    FunctDec temp;
    ClassDec curClass = curClassStack.isEmpty() ? null : curClassStack.peek();
    if (curClass != null && (temp = functTable.get(AddPrefix(curClass, functCallExp.functName))) != null) {
      functCallExp.varTypeRefDec = temp.returnType;
    }
    else {
      temp = functTable.get(Symbol.GetSymbol(functCallExp.functName));
      if (temp == null)
        throw new RuntimeException("invoke undefined function...\n" + functCallExp.LocationToString());
      functCallExp.varTypeRefDec = temp.returnType;
    }
    if (temp.arguments.varDecs.size() != functCallExp.arguments.args.size())
      throw new RuntimeException("arguments number doesn't much during function invoking...\n" + functCallExp.LocationToString());
    for (int i = 0; i < functCallExp.arguments.args.size(); ++i) {
      if (!Assignable(temp.arguments.varDecs.get(i).varType, functCallExp.arguments.args.get(i).varTypeRefDec))
        throw new RuntimeException(i + "th argument type doesn't much function signature...\n" + functCallExp.LocationToString());
    }
    return null;
  }

  /**
   * Incorporate arrayType.size() into framework.
   * */
  @Override
  public Void visit(MethodCallExp methodCallExp) {
//    methodCallExp.varTypeRefDec = functTable.get(Symbol.GetSymbol(methodCallExp.methodName)).returnType;
    super.visit(methodCallExp);
    ClassDec objClass;
    FunctDec methodDec;
    if (methodCallExp.objInstance.varTypeRefDec instanceof ArrayTypeRef)
      objClass = classTable.get(Symbol.GetSymbol("-array"));
    else
      objClass = classTable.get(Symbol.GetSymbol(methodCallExp.objInstance.varTypeRefDec.typeName));

    methodDec = functTable.get(AddPrefix(objClass, methodCallExp.functName));
    if (methodDec == null)
      throw new RuntimeException("access undefined method...\n" + methodCallExp.LocationToString());

    if (methodDec.arguments.varDecs.size() != methodCallExp.arguments.args.size())
      throw new RuntimeException("arguments number doesn't much during method invoking...\n" + methodCallExp.LocationToString());
    for (int i = 0; i < methodCallExp.arguments.args.size(); ++i) {
      if (!Assignable(methodDec.arguments.varDecs.get(i).varType, methodCallExp.arguments.args.get(i).varTypeRefDec))
        throw new RuntimeException(i + "th argument type doesn't much method signature...\n" + methodCallExp.LocationToString());
    }
    methodCallExp.varTypeRefDec = methodDec.returnType;
    return null;
  }

  @Override
  public Void visit(FieldAccessExp fieldAccessExp) {
    // find the class entity from classTable.
//    System.out.println(fieldAccessExp.toString());
    super.visit(fieldAccessExp);
    ClassDec classEntity = classTable.get(Symbol.GetSymbol(fieldAccessExp.objInstance.varTypeRefDec.typeName));
    VarDec fieldDec = classEntity.FindField(fieldAccessExp.memberName);
    if (fieldDec == null)
      throw new RuntimeException("object instance tries to access undefined data field...\n" + fieldAccessExp.LocationToString());
    fieldAccessExp.varTypeRefDec = fieldDec.varType;
    return null;
  }

  /**
   * Check array index type to be integer.
   * Check be indexed type to be arrayType.
   * */
  @Override
  public Void visit(ArrayAccessExp arrayAccessExp) {
    super.visit(arrayAccessExp);
    if (!arrayAccessExp.accessor.varTypeRefDec.isInt())
      throw new RuntimeException("array accessor index isn't integer...\n" + arrayAccessExp.LocationToString());
    try {
      arrayAccessExp.varTypeRefDec = arrayAccessExp.arrInstance.varTypeRefDec.GetAccessedTypeRef();
    } catch (RuntimeException e) {
      throw new RuntimeException("Access non-array type by indexing...\n" + arrayAccessExp.LocationToString());
    }
    return null;
  }

  /**
   * This can't be assigned
   * */
  @Override
  public Void visit(AssignExp assignExp) {
    super.visit(assignExp);
    if (assignExp.lhs.getClass() == ThisExp.class)
      throw new RuntimeException("This cannot be assigned...\n" + assignExp.LocationToString());
    if (!Assignable(assignExp.lhs.varTypeRefDec, assignExp.rhs.varTypeRefDec))
      throw new RuntimeException("Incompatible assignment types...\n" + assignExp.LocationToString());
    return null;
  }

  /**
   * void type can't be created.
   * */
  @Override
  public Void visit(VarDec varDec) {
    super.visit(varDec);
    if (varDec.varType.typeName.equals("void"))
      throw new RuntimeException("cannot initialize void type...\n" + varDec.LocationToString());
    if (!classTable.containsKey(Symbol.GetSymbol(varDec.varType.typeName)))
      throw new RuntimeException("declare undefined type...\n" + varDec.LocationToString());
    if (varDec.inital != null && !Assignable(varDec.varType, varDec.inital.varTypeRefDec))
      throw new RuntimeException("variable declaration has incompatible initialization...\n" + varDec.LocationToString());
    return null;
  }


  @Override
  public Void visit(IfStm ifStm) {
    super.visit(ifStm);
    if (!ifStm.condition.varTypeRefDec.isBool())
      throw new RuntimeException("If condition isn't bool...\n" + ifStm.LocationToString());
    return null;
  }

  @Override
  public Void visit(WhileStm whileStm) {
    super.visit(whileStm);
    if (!whileStm.condition.varTypeRefDec.isBool())
      throw new RuntimeException("While condition isn't bool...\n" + whileStm.LocationToString());
    return null;
  }

  @Override
  public Void visit(ForControl forControl) {
    super.visit(forControl);
    if (forControl.check != null && !forControl.check.varTypeRefDec.isBool())
      throw new RuntimeException("for condition check isn't null, but isn't bool, either...\n" + forControl.LocationToString());
    return null;
  }

  /**
   * When retType is void, returnStm can either have null retVal exp or
   * have retVal exp with varTypeRef null.
   * */
  @Override
  public Void visit(ReturnStm returnStm) {
    super.visit(returnStm);

    if (!Returnable(returnStm.retType, returnStm.retVal))
      throw new RuntimeException("return value doesn't match return type...\n" + returnStm.LocationToString());
    return null;
  }

  @Override
  public Void visit(FunctDec functDec) {
    super.visit(functDec);
    if (!classTable.containsKey(Symbol.GetSymbol(functDec.returnType.typeName)))
      throw new RuntimeException("function has undefined return type...\n" + functDec.LocationToString());
    return null;
  }

  /**
   * new int[1][2]
   * */
  @Override
  public Void visit(CreationExp creationExp) {
    super.visit(creationExp);
    if (creationExp.varTypeRefDec.typeName.equals("void"))
      throw new RuntimeException("new a void...\n" + creationExp.LocationToString());

    if (creationExp.varTypeRefDec.getClass() == ArrayTypeRef.class) {
      // initializer should be integer type
      ArrayTypeRef arrayTypeRef = (ArrayTypeRef) creationExp.varTypeRefDec;
      for (int i = 0; i < arrayTypeRef.dim; ++i) {
        if (arrayTypeRef.dimBound.get(i) != null) {
          if (!arrayTypeRef.dimBound.get(i).varTypeRefDec.isInt())
            throw new RuntimeException("create non-integer dimension array...\n" + creationExp.LocationToString());
        }
      }
    }
    return null;
  }
}
