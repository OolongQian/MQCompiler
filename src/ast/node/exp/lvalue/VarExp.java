package ast.node.exp.lvalue;

import ast.node.dec.variable.VarDec;
import ast.node.exp.Exp;
import ast.usage.AstBaseVisitor;

/**
 * Why not attach information to ast ?
 * Because varDec thread is attached to ast.
 * */
public class VarExp extends Exp {
  public String varName;
  public VarDec varDec;

  public VarExp(String varName) {
    this.varName = varName;
  }

  @Override
  public String toString() {
    return varName;
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
