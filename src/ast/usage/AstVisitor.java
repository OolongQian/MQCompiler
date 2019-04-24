package ast.usage;

import ast.node.Ast;
import ast.node.Prog;
import ast.node.dec.*;
import ast.node.exp.*;
import ast.node.stm.*;
import ast.type.Type;

public interface AstVisitor<T> {

  T visit(Ast ast);

  T visit(Dec dec);

  T visit(LiteralExp literalExp);

  T visit(Exp exp);

  T visit(Stm stm);

  T visit(Prog prog);

  T visit(ClassDec classDec);

  T visit(FunctDec functDec);

  T visit(VarDec varDec);

  T visit(VarDecList varDecList);

  T visit(ArithBinaryExp arithBinaryExp);

  T visit(LogicBinaryExp logicBinaryExp);

  T visit(BoolLiteralExp boolLiteralExp);

  T visit(IntLiteralExp intLiteralExp);

  T visit(StringLiteralExp stringLiteralExp);

  T visit(ArrayAccessExp arrayAccessExp);

  T visit(FieldAccessExp fieldAccessExp);

  T visit(ThisExp thisExp);

  T visit(VarExp varExp);

  T visit(AssignExp assignExp);

  T visit(CreationExp creationExp);

  T visit(FunctCallExp functCallExp);

  T visit(NullExp nullExp);

  T visit(BlockStm blockStm);

  T visit(BreakStm breakStm);

  T visit(ContinueStm continueStm);

  T visit(EmptyStm emptyStm);

  T visit(ExpStm expStm);

  T visit(ForStm forStm);

  T visit(IfStm ifStm);

  T visit(ReturnStm returnStm);

  T visit(WhileStm whileStm);

  T visit(ForControl forControl);

  T visit(PrefixExp prefixExp);

  T visit(SuffixExp suffixExp);
  
  T visit(MethodCallExp node);
  
  T visit(LocalVarDecStm node);
  
  T visit(Type node);
}
