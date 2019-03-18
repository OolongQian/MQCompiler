package ast.node.exp.lvalue;

import ast.node.exp.Exp;
import ast.usage.AstBaseVisitor;

public class ThisExp extends Exp {

  @Override
  public String toString() {
    return "this";
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
