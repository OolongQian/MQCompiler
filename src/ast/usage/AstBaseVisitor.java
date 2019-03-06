package ast.usage;

import ast.node.Ast;
import ast.node.dec.Dec;
import ast.node.dec.class_.ClassDec;
import ast.node.dec.function.ConstructDec;
import ast.node.dec.function.FunctDec;
import ast.node.dec.function.MethodDec;
import ast.node.dec.variable.*;
import ast.node.exp.*;
import ast.node.exp.binary.ArithBinaryExp;
import ast.node.exp.binary.LogicBinaryExp;
import ast.node.exp.literal.BoolLiteralExp;
import ast.node.exp.literal.IntLiteralExp;
import ast.node.exp.literal.LiteralExp;
import ast.node.exp.literal.StringLiteralExp;
import ast.node.exp.lvalue.*;
import ast.node.exp.unary.PrefixExp;
import ast.node.exp.unary.SuffixExp;
import ast.node.prog.Prog;
import ast.node.stm.*;
import ast.typeref.ArrayTypeRef;
import ast.typeref.FunctTypeRef;
import ast.typeref.VarTypeRef;
import ast.util.DecArgs;

public class AstBaseVisitor<T> implements AstVisitor<T> {
  /**
   * Here, Java doesn't care about the actual type of input node.
   * Its closest type can only be revealed by calling Accept() method.
   * */

  @Override
  public T visit(Ast ast) {
    return ast.Accept(this);
  }

  @Override
  public T visit(Dec dec) {
    return dec.Accept(this);
  }

  @Override
  public T visit(LiteralExp literalExp) {
    return literalExp.Accept(this);
  }

  @Override
  public T visit(LValueExp lValueExp) {
    return lValueExp.Accept(this);
    
  }

  @Override
  public T visit(Exp exp) {
    return exp.Accept(this);
    
  }

  @Override
  public T visit(Stm stm) {
    return stm.Accept(this);
    
  }

  @Override
  public T visit(Prog prog) {
    return null;
  }

  @Override
  public T visit(ClassDec classDec) {
    return null;
  }

  @Override
  public T visit(ConstructDec constructDec) {
    return null;
  }

  @Override
  public T visit(FunctDec functDec) {
    return null;
  }

  @Override
  public T visit(MethodDec methodDec) {
    return null;
  }

  @Override
  public T visit(FieldDec fieldDec) {
    return null;
  }

  @Override
  public T visit(GlobalVarDec globalVarDec) {
    return null;
  }

  @Override
  public T visit(LocalVarDec localVarDec) {
    return null;
  }

  @Override
  public T visit(VarDec varDec) {
    return null;
  }

  @Override
  public T visit(VarDecList varDecList) {
    return null;
  }

  @Override
  public T visit(ArithBinaryExp arithBinaryExp) {
    return null;
  }

  @Override
  public T visit(LogicBinaryExp logicBinaryExp) {
    return null;
  }

  @Override
  public T visit(BoolLiteralExp boolLiteralExp) {
    return null;
  }

  @Override
  public T visit(IntLiteralExp intLiteralExp) {
    return null;
  }

  @Override
  public T visit(StringLiteralExp stringLiteralExp) {
    return null;
  }

  @Override
  public T visit(ArrayAccessExp arrayAccessExp) {
    return null;
  }

  @Override
  public T visit(FieldAccessExp fieldAccessExp) {
    return null;
  }

  @Override
  public T visit(ThisExp thisExp) {
    return null;
  }

  @Override
  public T visit(VarExp varExp) {
    return null;
  }

  @Override
  public T visit(AssignExp assignExp) {
    return null;
  }

  @Override
  public T visit(CreationExp creationExp) {
    return null;
  }

  @Override
  public T visit(FunctCallExp functCallExp) {
    return null;
  }

  @Override
  public T visit(MethodCallExp methodCallExp) {
    return null;
  }

  @Override
  public T visit(NullExp nullExp) {
    return null;
  }

  @Override
  public T visit(ArrayTypeRef arrayTypeRef) {
    return null;
  }

  @Override
  public T visit(FunctTypeRef functTypeRef) {
    return null;
  }

  @Override
  public T visit(VarTypeRef varTypeRef) {
    return null;
  }

  @Override
  public T visit(BlockStm blockStm) {
    return null;
  }

  @Override
  public T visit(BreakStm breakStm) {
    return null;
  }

  @Override
  public T visit(ContinueStm continueStm) {
    return null;
  }

  @Override
  public T visit(EmptyStm emptyStm) {
    return null;
  }

  @Override
  public T visit(ExpStm expStm) {
    return null;
  }

  @Override
  public T visit(ForStm forStm) {
    return null;
  }

  @Override
  public T visit(IfStm ifStm) {
    return null;
  }

  @Override
  public T visit(LocalVarDecStm localVarDecStm) {
    return null;
  }

  @Override
  public T visit(ReturnStm returnStm) {
    return null;
  }

  @Override
  public T visit(WhileStm whileStm) {
    return null;
  }

  @Override
  public T visit(ForControl forControl) {
    return null;
  }

  @Override
  public T visit(DecArgs decArgs) {
    return null;
  }

  @Override
  public T visit(PrefixExp prefixExp) {
    return null;
  }

  @Override
  public T visit(SuffixExp suffixExp) {
    return null;
  }
}
