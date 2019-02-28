package ast.node.dec.function;

import ast.usage.AstBaseVisitor;

public class MethodDec extends FunctDec {
  public MethodDec() {}
  public MethodDec(FunctDec functDec) {
    super(functDec);
  }

  @Override
  protected String SelfDeclare() {
    return "MethodDec: \n";
  }

  @Override
  public void Accept(AstBaseVisitor visitor) {
    visitor.visit(this);
  }

}
