package ast.usage;

import ast.node.Ast;
import ast.node.Prog;
import ast.node.dec.*;
import ast.node.exp.*;
import ast.node.stm.*;
import ast.type.Type;

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
  public T visit(FunctDec functDec) {
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
  public T visit(NullExp nullExp) {
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
  public T visit(PrefixExp prefixExp) {
    return null;
  }

  @Override
  public T visit(SuffixExp suffixExp) {
    return null;
  }
	
	@Override
	public T visit(MethodCallExp node) {
		return null;
	}
	
	@Override
	public T visit(LocalVarDecStm node) {
		return null;
	}
  
  @Override
  public T visit(Type node) {
    return null;
  }
}
