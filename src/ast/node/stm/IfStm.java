package ast.node.stm;

import ast.node.exp.Exp;
import ast.usage.AstBaseVisitor;
import ir_codegen.util.BasicBlock;

/**
 * Condition and then can't be null.
 * */
public class IfStm extends Stm {
  public Exp condition;
  public Stm thenBody;
  public Stm elseBody;

  public BasicBlock ifTrue;
  public BasicBlock ifFalse;
  public BasicBlock ifMerge;

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
