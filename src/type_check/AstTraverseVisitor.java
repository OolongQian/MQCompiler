package type_check;


import ast.node.dec.class_.ClassDec;
import ast.node.dec.function.ConstructDec;
import ast.node.dec.function.FunctDec;
import ast.node.dec.function.MethodDec;
import ast.node.dec.variable.*;
import ast.node.exp.AssignExp;
import ast.node.exp.CreationExp;
import ast.node.exp.FunctCallExp;
import ast.node.exp.MethodCallExp;
import ast.node.exp.binary.ArithBinaryExp;
import ast.node.exp.binary.LogicBinaryExp;
import ast.node.exp.lvalue.ArrayAccessExp;
import ast.node.exp.lvalue.FieldAccessExp;
import ast.node.exp.lvalue.ThisExp;
import ast.node.exp.lvalue.VarExp;
import ast.node.exp.unary.PrefixExp;
import ast.node.exp.unary.SuffixExp;
import ast.node.prog.Prog;
import ast.node.stm.*;
import ast.typeref.ArrayTypeRef;
import ast.usage.AstBaseVisitor;
import ast.util.DecArgs;
import type_check.symbol_table.Symbol;

/**
 * This Traverse visitor provides default traversal top-down
 * for AST prog tree.
 * */
public class AstTraverseVisitor<T> extends AstBaseVisitor<T> {

  protected Symbol AddPrefix(ClassDec classDec, String methodName) {
    return Symbol.GetSymbol("-" + classDec.classTypeName.typeName + methodName);
  }

  @Override
  public T visit(Prog prog) {
    for (int i = 0; i < prog.children.size(); ++i)
      visit(prog.children.get(i));
    return null;
  }

  @Override
  public T visit(VarDecList varDecList) {
    for (int i = 0; i < varDecList.varDecs.size(); ++i)
      visit(varDecList.varDecs.get(i));
    return null;
  }

  @Override
  // TODO : This is stupid, consider wipe out GlobalVarDec's accept method.
  public T visit(GlobalVarDec globalVarDec) {
    visit ((VarDecList) globalVarDec);
    return null;
  }

  @Override
  public T visit(FieldDec fieldDec) {
    visit ((VarDecList) fieldDec);
    return null;
  }

  @Override
  public T visit(LocalVarDecStm localVarDecStm) {
    visit(localVarDecStm.localVarDec);
    return null;
  }

  @Override
  public T visit(LocalVarDec localVarDec) {
    visit ((VarDecList) localVarDec);
    return null;
  }
  /**
   * typeName and identifier can be defined or not.
   * */
  @Override
  public T visit(VarDec varDec) {
    if (varDec.inital != null)
      visit(varDec.inital);
    return null;
  }

  @Override
  public T visit(ClassDec classDec) {
    for (int i = 0; i < classDec.field.size(); ++i)
      visit(classDec.field.get(i));
    if (classDec.constructor != null)
      visit (classDec.constructor);
    for (int i = 0; i < classDec.method.size(); ++i)
      visit(classDec.method.get(i));
    return null;
  }


  @Override
  public T visit(FunctDec functDec) {
    visit(functDec.arguments);
    for (int i = 0; i < functDec.functBody.size(); ++i)
      visit(functDec.functBody.get(i));
    return null;
  }

  @Override
  public T visit(ConstructDec constructDec) {
    visit ((FunctDec) constructDec);
    return null;
  }

  @Override
  public T visit(MethodDec methodDec) {
    visit ((FunctDec) methodDec);
    return null;
  }

  @Override
  public T visit(DecArgs decArgs) {
    visit ((VarDecList) decArgs);
    return null;
  }

  /**
   * Then, consider statements.
   * Main purpose is to visit expression so that VarTypeRef can be bound to
   * variables' literal names. Thus statements here serve as a medium of expression.
   * */
  @Override
  public T visit(BlockStm blockStm) {
    for (int i = 0; i < blockStm.children.size(); ++i)
      visit(blockStm.children.get(i));
    return null;
  }

  @Override
  public T visit(ExpStm expStm) {
    visit(expStm.exp);
    return null;
  }

  @Override
  public T visit(IfStm ifStm) {
    visit(ifStm.condition);
    visit(ifStm.thenBody);
    if (ifStm.elseBody != null)
      visit(ifStm.elseBody);
    return null;
  }

  @Override
  public T visit(ForStm forStm) {
    visit(forStm.forControl);
    visit(forStm.forBody);
    return null;
  }

  @Override
  public T visit(ForControl forControl) {
    if (forControl.initIsDec)
      visit(forControl.initDec);
    else {
      for (int i = 0; i < forControl.initExps.size(); ++i) {
        if (forControl.initExps.get(i) != null) {
          visit(forControl.initExps.get(i));
        }
      }
    }
    if (forControl.check != null)
      visit(forControl.check);
    for (int i = 0; i < forControl.updateExps.size(); ++i)
      visit(forControl.updateExps.get(i));
    return null;
  }

  @Override
  public T visit(WhileStm whileStm) {
    visit(whileStm.condition);
    visit(whileStm.whileBody);
    return null;
  }

  @Override
  public T visit(ReturnStm returnStm) {
    if (returnStm.retVal != null)
      visit(returnStm.retVal);
    return null;
  }

  /**
   * Then we deal with expressions.
   * First construct primary expression.
   * Then visit composite expression to pass them down.
   * */
  @Override
  public T visit(ThisExp thisExp) {
    return null;
  }

  @Override
  public T visit(VarExp varExp) {
    return null;
  }

  @Override
  public T visit(ArithBinaryExp arithBinaryExp) {
    visit(arithBinaryExp.lhs);
    visit(arithBinaryExp.rhs);
    return null;
  }

  @Override
  public T visit(LogicBinaryExp logicBinaryExp) {
    visit(logicBinaryExp.lhs);
    visit(logicBinaryExp.rhs);
    return null;
  }

  @Override
  public T visit(ArrayAccessExp arrayAccessExp) {
    visit(arrayAccessExp.arrInstance);
    visit(arrayAccessExp.accessor);
    return null;
  }

  @Override
  public T visit(FieldAccessExp fieldAccessExp) {
    visit(fieldAccessExp.objInstance);
    return null;
  }

  @Override
  public T visit(PrefixExp prefixExp) {
    visit(prefixExp.objInstance);
    return null;
  }

  @Override
  public T visit(SuffixExp suffixExp) {
    visit(suffixExp.objInstance);
    return null;
  }

  @Override
  public T visit(AssignExp assignExp) {
    visit(assignExp.lhs);
    visit(assignExp.rhs);
    return null;
  }

  @Override
  public T visit(FunctCallExp functCallExp) {
    for (int i = 0; i < functCallExp.arguments.args.size(); ++i)
      visit(functCallExp.arguments.args.get(i));
    return null;
  }

  @Override
  public T visit(MethodCallExp methodCallExp) {
    visit(methodCallExp.objInstance);
    for (int i = 0; i < methodCallExp.arguments.args.size(); ++i)
      visit(methodCallExp.arguments.args.get(i));
    return  null;
  }

  @Override
  public T visit(CreationExp creationExp) {
    if (creationExp.varTypeRefDec.getClass() == ArrayTypeRef.class) {
      ArrayTypeRef arrayTypeRef = (ArrayTypeRef) creationExp.varTypeRefDec;
      for (int i = 0; i < arrayTypeRef.dimBound.size(); ++i) {
        if (arrayTypeRef.dimBound.get(i) != null)
          visit(arrayTypeRef.dimBound.get(i));
      }
    }
    return null;
  }
}
