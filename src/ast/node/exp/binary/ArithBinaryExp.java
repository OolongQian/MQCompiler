package ast.node.exp.binary;

import ast.node.exp.Exp;
import ast.usage.AstBaseVisitor;

public class ArithBinaryExp extends BinaryExp {
  public ArithBinaryExp(Exp left, Exp right, String op) {
    super(left, right, op);
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
