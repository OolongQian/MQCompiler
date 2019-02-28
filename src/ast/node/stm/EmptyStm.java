package ast.node.stm;

import ast.usage.AstBaseVisitor;

public class EmptyStm extends Stm {
  @Override
  protected String SelfDeclare() {
    return "EmptyStm: \n";
  }

  @Override
  public String PrettyPrint() {
    return Formatter(this, ";\n");
  }

  @Override
  public void Accept(AstBaseVisitor visitor) {
    visitor.visit(this);
  }
}
