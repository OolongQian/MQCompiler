package ast.node.exp.unary;


import ast.node.exp.Exp;
import ast.usage.AstBaseVisitor;

/**
 * ++a is a left value.
 * */
public class PrefixExp extends Exp {
  // NOTE : objInstance is a LValue Actually.
  public Exp objInstance;
  public String op;

  public PrefixExp(Exp instance, String op) {
    objInstance = instance;
    this.op = op;
  }

  @Override
  public String toString() {
    return op + objInstance.toString();
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
