package ast.node.exp.lvalue;

import ast.usage.AstBaseVisitor;

public class ThisExp extends LValueExp {

  @Override
  public String toString() {
    return "this";
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
