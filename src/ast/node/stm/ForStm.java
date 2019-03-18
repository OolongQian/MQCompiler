package ast.node.stm;

import ast.usage.AstBaseVisitor;

public class ForStm extends Stm {
  public ForControl forControl = new ForControl();
  public Stm forBody;

  @Override
  protected String SelfDeclare() {
    return "ForStm: \n";
  }

  @Override
  public String PrettyPrint() {
    return Formatter(this, "for " + forControl.toString() + "\n" + forBody.PrettyPrint());
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
