package ast.node.stm;

import ast.usage.AstBaseVisitor;

public class BreakStm extends Stm {
  @Override
  protected String SelfDeclare() {
    return "BreakStm: \n";
  }

  @Override
  public String PrettyPrint() {
    return Formatter(this, "break;\n");
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
