package ast.node.exp.binary;

import ast.node.exp.Exp;
import ast.usage.AstBaseVisitor;

public class ArithBinaryExp extends BinaryExp {
  public ArithBinaryExp(Exp left, Exp right, String op) {
    super(left, right, op);
  }

  @Override
  public void Accept(AstBaseVisitor visitor) {
    visitor.visit(this);
  }
}
