package ast.node.exp.lvalue;

import ast.usage.AstBaseVisitor;

public class VarExp extends LValueExp {
  public String varName;

  public VarExp(String varName) {
    this.varName = varName;
  }

  @Override
  public String toString() {
    return varName;
  }

  @Override
  public void Accept(AstBaseVisitor visitor) {
    visitor.visit(this);
  }
}
