package ast.node.exp.unary;


import ast.node.exp.Exp;
import ast.usage.AstBaseVisitor;

public class SuffixExp extends Exp {
  public Exp objInstance;
  public String op;

  public SuffixExp(Exp instance, String op) {
    objInstance = instance;
    this.op = op;
  }

  @Override
  public String toString() {
    return objInstance.toString() + op;
  }

  @Override
  public void Accept(AstBaseVisitor visitor) {
    visitor.visit(this);
  }
}
