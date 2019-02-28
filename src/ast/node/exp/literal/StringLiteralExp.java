package ast.node.exp.literal;

import ast.typeref.VarTypeRef;
import ast.usage.AstBaseVisitor;

public class StringLiteralExp extends LiteralExp {
  public String value;

  public StringLiteralExp(String value) {
    this.varTypeRefDec = new VarTypeRef("string");
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }

  @Override
  public void Accept(AstBaseVisitor visitor) {
    visitor.visit(this);
  }
}
