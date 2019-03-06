package ast.node.stm;

import ast.node.exp.Exp;
import ast.usage.AstBaseVisitor;
import ir_codegen.util.BasicBlock;

/**
 * Condition can't be null.
 * */
public class WhileStm extends Stm {
  public Exp condition;
  public Stm whileBody;

  public BasicBlock cond;
  public BasicBlock step; // run into next loop cycle
  public BasicBlock after; // the piece of code

  @Override
  public BasicBlock GetLoopAfter() {
    return after;
  }

  @Override
  protected String SelfDeclare() {
    return "WhileStm: \n";
  }

  @Override
  public String PrettyPrint() {
    String selfie = "while (" + condition.toString() + ")\n";
    selfie += whileBody.PrettyPrint();
    return Formatter(this, selfie);
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
