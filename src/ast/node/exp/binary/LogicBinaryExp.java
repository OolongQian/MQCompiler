package ast.node.exp.binary;

import ast.node.exp.Exp;
import ast.usage.AstBaseVisitor;

public class LogicBinaryExp extends BinaryExp {


  public LogicBinaryExp(Exp left, Exp right, String op) {
      super(left, right, op);
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
