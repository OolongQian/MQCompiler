package ast.node.stm;

import ast.node.exp.Exp;
import ast.usage.AstBaseVisitor;

public class ExpStm extends Stm {
  public Exp exp;

  public ExpStm(Exp exp) {
    this.exp = exp;
  }

  @Override
  protected String SelfDeclare() {
    return "ExpStm: \n";
  }

  @Override
  public String PrettyPrint() {
    return Formatter(this, exp.toString() + ";\n");
  }

  @Override
  public void Accept(AstBaseVisitor visitor) {
    visitor.visit(this);
  }
}
