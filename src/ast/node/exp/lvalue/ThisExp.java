package ast.node.exp.lvalue;

import ast.usage.AstBaseVisitor;

public class ThisExp extends LValueExp {

  @Override
  public String toString() {
    return "this";
  }

  @Override
  public void Accept(AstBaseVisitor visitor) {
    visitor.visit(this);
  }
}
