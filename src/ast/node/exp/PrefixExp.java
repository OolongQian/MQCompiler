package ast.node.exp;

import ast.usage.AstBaseVisitor;

/**
 * ++a is a left value.
 * */
public class PrefixExp extends Exp {
  public Exp obj;
  public String op;
  
  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
