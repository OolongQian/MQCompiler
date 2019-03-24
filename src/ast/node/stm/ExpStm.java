package ast.node.stm;


import ast.node.exp.Exp;
import ast.usage.AstBaseVisitor;

public class ExpStm extends Stm {
  public Exp exp;
  
  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
