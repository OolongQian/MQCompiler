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
import ast.typeref.VarTypeRef;
import ast.util.DecArgs;

public interface AstVisitor<T> {

  T visit(Ast ast);

  T visit (Dec dec);

  T visit (LiteralExp literalExp);

  T visit (Exp exp);

  T visit (Stm stm);

  T visit (Prog prog);

  T visit (ClassDec classDec);

  T visit (ConstructDec constructDec);

  T visit (FunctDec functDec);

  T visit (MethodDec methodDec);

  T visit (FieldDec fieldDec);

  T visit (GlobalVarDec globalVarDec);

  T visit (LocalVarDec localVarDec);

  T visit (VarDec varDec);

  T visit (VarDecList varDecList);

  T visit (ArithBinaryExp arithBinaryExp);

  T visit (LogicBinaryExp logicBinaryExp);

  T visit (BoolLiteralExp boolLiteralExp);

  T visit (IntLiteralExp intLiteralExp);

  T visit (StringLiteralExp stringLiteralExp);

  T visit (ArrayAccessExp arrayAccessExp);

  T visit (FieldAccessExp fieldAccessExp);

  T visit (ThisExp thisExp);

  T visit (VarExp varExp);

  T visit (AssignExp assignExp);

  T visit (CreationExp creationExp);

  T visit (FunctCallExp functCallExp);

  T visit (MethodCallExp methodCallExp);

  T visit (NullExp nullExp);

  T visit (VarTypeRef varTypeRef);

  T visit (BlockStm blockStm);

  T visit (BreakStm breakStm);

  T visit (ContinueStm continueStm);

  T visit (EmptyStm emptyStm);

  T visit (ExpStm expStm);

  T visit (ForStm forStm);

  T visit (IfStm ifStm);

  T visit (LocalVarDecStm localVarDecStm);

  T visit (ReturnStm returnStm);

  T visit (WhileStm whileStm);

  T visit (ForControl forControl);

  T visit (DecArgs decArgs);

  T visit (PrefixExp prefixExp);

  T visit (SuffixExp suffixExp);
}
