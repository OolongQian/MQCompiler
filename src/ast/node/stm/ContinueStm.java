package ast.node.stm;

import ast.usage.AstBaseVisitor;

public class ContinueStm extends Stm {
  @Override
  protected String SelfDeclare() {
    return "ContinueStm: \n";
  }

  @Override
  public String PrettyPrint() {
    return Formatter(this, "continue;\n");
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
