package ast.node.exp;

import ast.node.Ast;
import ast.typeref.VarTypeRef;
import ir_codegen.util.BasicBlock;

abstract public class Exp extends Ast {

  public BasicBlock ifTrue = null;
  public BasicBlock ifFalse = null;

  public VarTypeRef varTypeRefDec;
}
