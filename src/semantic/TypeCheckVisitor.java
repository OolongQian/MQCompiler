package semantic;

import ast.node.dec.class_.ClassDec;
import ast.node.dec.function.FunctDec;
import ast.node.dec.function.MethodDec;
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
import ast.typeref.VarTypeRef;

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
  public Hashtable<String, ClassDec> classTable;

  /**
   * Create association from function's literal name to its actual functionDec.
   * */
  public Hashtable<String, FunctDec> functTable;

  /**
   * Note that methods inside class scope appears the same as functions, thus we need to
   * search for it first in class scope, then in global scope.
   * */
  private Stack<ClassDec> curClassStack = new Stack<>();

  public TypeCheckVisitor(TypeThreadVisitor typeThreadVisitor) {
    classTable = typeThreadVisitor.classTable;
    functTable = typeThreadVisitor.functTable;
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
      return rhs != null && Assignable(lhs, rhs.varTypeRef);
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
  public Void visit(ArithBinaryExp node) {
    super.visit(node);
    VarTypeRef lhs = node.lhs.varTypeRef;
    VarTypeRef rhs = node.rhs.varTypeRef;
    switch (node.op) {
      // arithmetic
      case "*": case "/":
      case "-": case "%":
      case "<<": case ">>":
        if (!(lhs.isInt() && rhs.isInt()))
          throw new RuntimeException("ArithBinaryExp type error...\n" + node.LocationToString());
        node.varTypeRef = VarTypeRef.CreatePrimitiveType("int");
        break;
      case "+":
        if (!(lhs.isInt() && rhs.isInt() || lhs.isString() && rhs.isString()))
          throw new RuntimeException("ArithBinaryExp type error...\n" + node.LocationToString());
        node.varTypeRef = lhs;
        break;
      case ">": case ">=":
      case "<": case "<=":
        if (!(lhs.isInt() && rhs.isInt() || lhs.isString() && rhs.isString()))
          throw new RuntimeException("ArithBinaryExp type error...\n" + node.LocationToString());
        node.varTypeRef = VarTypeRef.CreatePrimitiveType("bool");
        break;
      case "==": case "!=":
        if (!Comparable(lhs, rhs))
          throw new RuntimeException("ArithBinaryExp type error...\n" + node.LocationToString());
        node.varTypeRef = VarTypeRef.CreatePrimitiveType("bool");
        break;
      case "&": case "^": case "|":
        if (!(lhs.isInt() && rhs.isInt() || lhs.isBool() && rhs.isBool()))
          throw new RuntimeException("ArithBinaryExp type error...\n" + node.LocationToString());
        node.varTypeRef = lhs;
        break;
      default:
        throw new RuntimeException("Parser error...\n" + node.LocationToString());
    }
    return null;
  }

  @Override
  public Void visit(LogicBinaryExp logicBinaryExp) {
    super.visit(logicBinaryExp);
    VarTypeRef lhs = logicBinaryExp.lhs.varTypeRef;
    VarTypeRef rhs = logicBinaryExp.rhs.varTypeRef;
    if (!(lhs.isBool() && rhs.isBool()))
      throw new RuntimeException("logicBinaryExp type error...\n" + logicBinaryExp.LocationToString());
    logicBinaryExp.varTypeRef = lhs;
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
        if (!prefixExp.objInstance.varTypeRef.isBool())
          throw new RuntimeException("prefixExp type error...\n" + prefixExp.LocationToString());
        break;
      case "~":
        if (!(prefixExp.objInstance.varTypeRef.isBool() || prefixExp.objInstance.varTypeRef.isInt()))
          throw new RuntimeException("prefixExp type error...\n" + prefixExp.LocationToString());
        break;
      case "-" :
        if (!prefixExp.objInstance.varTypeRef.isInt())
          throw new RuntimeException("prefixExp type error...\n" + prefixExp.LocationToString());
        break;
      case "++": case "--":
        if (!prefixExp.objInstance.varTypeRef.isInt())
          throw new RuntimeException("prefixExp type error...\n" + prefixExp.LocationToString());
        // NOTE LV
        prefixExp.lValue = true;
        break;
      default:
        throw new RuntimeException("encounter unknown prefix...\n" + prefixExp.LocationToString());
    }
    prefixExp.varTypeRef = prefixExp.objInstance.varTypeRef;
    return null;
  }

  @Override
  public Void visit(SuffixExp suffixExp) {
    super.visit(suffixExp);
    if (!suffixExp.objInstance.varTypeRef.isInt())
      throw new RuntimeException("suffixExp type error...\n" + suffixExp.LocationToString());
    suffixExp.varTypeRef = suffixExp.objInstance.varTypeRef;
    return null;
  }


  /**
   * Distinguish method (inside class) from function.
   * Check parameter matching.
   * record methodDec and functDec onto FunctCallExp.
   * */
  @Override
  public Void visit(FunctCallExp node) {
    super.visit(node);

    FunctDec temp;
    ClassDec curClass = curClassStack.isEmpty() ? null : curClassStack.peek();

    if (curClass != null) {
      // this is a functCall inside a class, hence it can be a global function call or a in-class method call.
      if (functTable.containsKey(AddPrefix(curClass, node.functName))) {
        // check if this is a method.
        temp = functTable.get(AddPrefix(curClass, node.functName));
      } else if (functTable.containsKey(node.functName)) {
        // is a global function
        temp = functTable.get(node.functName);
      } else {
        throw new RuntimeException("invoke undefined function inside class, not method nor function...\n" + node.LocationToString());
      }
    } else {
      // this is just a plain functionCall
      if (functTable.containsKey(node.functName)) {
        temp = functTable.get(node.functName);
      } else {
        throw new RuntimeException("invoke undefined function outside class...\n" + node.LocationToString());
      }
    }

    if (temp.arguments.varDecs.size() != node.arguments.args.size())
      throw new RuntimeException("arguments number doesn't much during method invoking...\n" + node.LocationToString());
    for (int i = 0; i < node.arguments.args.size(); ++i) {
      if (!Assignable(temp.arguments.varDecs.get(i).varType, node.arguments.args.get(i).varTypeRef))
        throw new RuntimeException(i + "th argument type doesn't much method signature...\n" + node.LocationToString());
    }

    node.varTypeRef = temp.returnType;
    node.functDec = temp;
    return null;

  }

  /**
   * Incorporate arrayType.size() into framework.
   * */
  @Override
  public Void visit(MethodCallExp node) {
    super.visit(node);
    ClassDec objClass;
    FunctDec methodDec;
    if (node.objInstance.varTypeRef.isArray())
      objClass = classTable.get("-array");
    else
      objClass = classTable.get(node.objInstance.varTypeRef.GetTypeName());

    methodDec = functTable.get(AddPrefix(objClass, node.functName));
    if (methodDec == null)
      throw new RuntimeException("access undefined method...\n" + node.LocationToString());

    if (methodDec.arguments.varDecs.size() != node.arguments.args.size())
      throw new RuntimeException("arguments number doesn't much during method invoking...\n" + node.LocationToString());
    for (int i = 0; i < node.arguments.args.size(); ++i) {
      if (!Assignable(methodDec.arguments.varDecs.get(i).varType, node.arguments.args.get(i).varTypeRef))
        throw new RuntimeException(i + "th argument type doesn't much method signature...\n" + node.LocationToString());
    }
    // NOTE : methodCall is linking methodDec and classDec
    node.varTypeRef = methodDec.returnType;
    node.calledClass = objClass;
    node.calledMethod = (MethodDec) methodDec;
    return null;
  }

  @Override
  public Void visit(FieldAccessExp fieldAccessExp) {
    // find the class entity from classTable.
//    System.out.println(fieldAccessExp.toString());
    super.visit(fieldAccessExp);
    ClassDec classEntity = classTable.get(fieldAccessExp.objInstance.varTypeRef.GetTypeName());
    VarDec fieldDec = classEntity.FindField(fieldAccessExp.memberName);
    if (fieldDec == null)
      throw new RuntimeException("object instance tries to access undefined reg field...\n" + fieldAccessExp.LocationToString());

    // NOTE : record class and field declaration on fieldAccessExp.
    fieldAccessExp.varTypeRef = fieldDec.varType;
    fieldAccessExp.objClassDec = classEntity;
    fieldAccessExp.fieldDec = fieldDec;
    // NOTE LV
    fieldAccessExp.lValue = true;
    return null;
  }

  /**
   * Check array index type to be integer.
   * Check be indexed type to be arrayType.
   * */
  @Override
  public Void visit(ArrayAccessExp node) {
    super.visit(node);
    if (!node.accessor.varTypeRef.isInt())
      throw new RuntimeException("array accessor index isn't integer...\n" + node.LocationToString());
    if (!node.arrInstance.varTypeRef.isArray())
      throw new RuntimeException("Access non-array type by indexing...\n" + node.LocationToString());
    node.varTypeRef = node.arrInstance.varTypeRef.innerType;

    // NOTE LV
    node.lValue = true;
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
    if (!Assignable(assignExp.lhs.varTypeRef, assignExp.rhs.varTypeRef))
      throw new RuntimeException("Incompatible assignment types...\n" + assignExp.LocationToString());
    if (!assignExp.lhs.lValue)
      throw new RuntimeException("cannot assign a non-lvalue...\n" + assignExp.LocationToString());
    return null;
  }

  /**
   * void type can't be created.
   * */
  @Override
  public Void visit(VarDec varDec) {
    super.visit(varDec);

    // NOTE : store class in VarDec's VarTypeRef.
    ClassDec baseClass = classTable.get(varDec.varType.GetBaseTypeName());
    varDec.varType.SetBaseType(baseClass);

    if (varDec.varType.baseType.classType.typeName.equals("void"))
      throw new RuntimeException("cannot initialize void type...\n" + varDec.LocationToString());
    if (!classTable.containsKey(varDec.varType.GetBaseTypeName()))
      throw new RuntimeException("declare undefined type...\n" + varDec.LocationToString());
    if (varDec.inital != null && !Assignable(varDec.varType, varDec.inital.varTypeRef))
      throw new RuntimeException("variable declaration has incompatible initialization...\n" + varDec.LocationToString());

    return null;
  }

  @Override
  public Void visit(IfStm ifStm) {
    super.visit(ifStm);
    if (!ifStm.condition.varTypeRef.isBool())
      throw new RuntimeException("If condition isn't bool...\n" + ifStm.LocationToString());
    return null;
  }

  @Override
  public Void visit(WhileStm whileStm) {
    super.visit(whileStm);
    if (!whileStm.condition.varTypeRef.isBool())
      throw new RuntimeException("While condition isn't bool...\n" + whileStm.LocationToString());
    return null;
  }

  @Override
  public Void visit(ForControl forControl) {
    super.visit(forControl);
    if (forControl.check != null && !forControl.check.varTypeRef.isBool())
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
    if (!classTable.containsKey(functDec.returnType.GetTypeName()))
      throw new RuntimeException("function has undefined return type...\n" + functDec.LocationToString());
    return null;
  }

  @Override
  public Void visit(MethodDec methodDec) {
    return visit((FunctDec) methodDec);
  }

  /**
   * new int[1][2]
   * */
  @Override
  public Void visit(CreationExp creationExp) {
    // NOTE : creationExp is basically a plain type object. and we visit it to get an interpreted VarTypeRef and analyze it.
    // NOTE : and of course we need to associate the real type to it.
    super.visit(creationExp);

    // NOTE : store class in VarDec's VarTypeRef.
    ClassDec baseClass = classTable.get(creationExp.varTypeRef.GetBaseTypeName());
    creationExp.varTypeRef.SetBaseType(baseClass);

    if (creationExp.varTypeRef.GetBaseTypeName().equals("void"))
      throw new RuntimeException("new a void...\n" + creationExp.LocationToString());
    if (creationExp.varTypeRef.isArray()) {
      VarTypeRef tmp = creationExp.varTypeRef;
      while (tmp.isArray()) {
        if (tmp.dim != null && !tmp.dim.varTypeRef.isInt()) {
          throw new RuntimeException("create non-integer dimension array...\n" + creationExp.LocationToString());
        }
        tmp = tmp.innerType;
      }
    }
    return null;
  }
}
