package ast.node.exp.literal;

import ast.typeref.VarTypeRef;
import ast.usage.AstBaseVisitor;

public class IntLiteralExp extends LiteralExp {
  public int value;

  public IntLiteralExp(int value) {
    this.varTypeRefDec = new VarTypeRef("int");
    this.value = value;
  }

  @Override
  public String toString() {
    return Integer.toString(value);
  }

  @Override
  public void Accept(AstBaseVisitor visitor) {
    visitor.visit(this);
  }
}
