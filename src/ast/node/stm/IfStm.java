package ast.node.stm;

import ast.node.exp.Exp;
import ast.usage.AstBaseVisitor;

/**
 * Condition and then can't be null.
 * */
public class IfStm extends Stm {
  public Exp condition;
  public Stm thenBody;
  public Stm elseBody;


  @Override
  protected String SelfDeclare() {
    return "IfStm: \n";
  }

  @Override
  public String PrettyPrint() {
    String selfie = "if (" + condition.toString() + ")\n";
    selfie += thenBody.PrettyPrint();
    if (elseBody != null)
      selfie += TabShift("else\n") + elseBody.PrettyPrint();
    return Formatter(this, selfie);
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
