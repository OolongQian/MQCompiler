package ast.node.dec.function;

import ast.usage.AstBaseVisitor;

public class ConstructDec extends MethodDec {

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
