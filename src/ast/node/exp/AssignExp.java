package ast.node.exp;

import ast.usage.AstBaseVisitor;

public class AssignExp extends Exp {
  public Exp lhs;
  public Exp rhs;

  public AssignExp(Exp left, Exp right) {
    lhs = left;
    rhs = right;
  }

  @Override
  public String toString() {
    return lhs.toString() + " = " + rhs.toString();
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
