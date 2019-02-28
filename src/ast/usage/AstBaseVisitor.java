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
    ast.Accept(this);
    return null;
  }

  @Override
  public T visit(Dec dec) {
    dec.Accept(this);
    return null;
  }

  @Override
  public T visit(LiteralExp literalExp) {
    literalExp.Accept(this);
    return null;
  }

  @Override
  public T visit(LValueExp lValueExp) {
    lValueExp.Accept(this);
    return null;
  }

  @Override
  public T visit(Exp exp) {
    exp.Accept(this);
    return null;
  }

  @Override
  public T visit(Stm stm) {
    stm.Accept(this);
    return null;
  }

  @Override
  public T visit(Prog prog) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(ClassDec classDec) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(ConstructDec constructDec) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(FunctDec functDec) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(MethodDec methodDec) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(FieldDec fieldDec) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(GlobalVarDec globalVarDec) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(LocalVarDec localVarDec) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(VarDec varDec) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(VarDecList varDecList) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(ArithBinaryExp arithBinaryExp) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(LogicBinaryExp logicBinaryExp) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(BoolLiteralExp boolLiteralExp) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(IntLiteralExp intLiteralExp) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(StringLiteralExp stringLiteralExp) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(ArrayAccessExp arrayAccessExp) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(FieldAccessExp fieldAccessExp) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(ThisExp thisExp) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(VarExp varExp) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(AssignExp assignExp) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(CreationExp creationExp) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(FunctCallExp functCallExp) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(MethodCallExp methodCallExp) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(NullExp nullExp) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(ArrayTypeRef arrayTypeRef) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(FunctTypeRef functTypeRef) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(VarTypeRef varTypeRef) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(BlockStm blockStm) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(BreakStm breakStm) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(ContinueStm continueStm) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(EmptyStm emptyStm) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(ExpStm expStm) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(ForStm forStm) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(IfStm ifStm) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(LocalVarDecStm localVarDecStm) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(ReturnStm returnStm) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(WhileStm whileStm) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(ForControl forControl) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(DecArgs decArgs) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(PrefixExp prefixExp) {
    System.out.println("default visit error\n");
    return null;
  }

  @Override
  public T visit(SuffixExp suffixExp) {
    System.out.println("default visit error\n");
    return null;
  }
}
