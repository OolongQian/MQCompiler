package ast.node.exp;

import ast.usage.AstBaseVisitor;

public class CreationExp extends Exp {

  @Override
  public String toString() {
    return "new " + varTypeRefDec.toString();
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
