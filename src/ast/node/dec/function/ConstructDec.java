package ast.node.dec.function;

import ast.usage.AstBaseVisitor;

public class ConstructDec extends MethodDec {

  @Override
  public void Accept(AstBaseVisitor visitor) {
    visitor.visit(this);
  }
}
