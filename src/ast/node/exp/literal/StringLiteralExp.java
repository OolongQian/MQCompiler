package ast.node.exp.literal;

import ast.typeref.VarTypeRef;
import ast.usage.AstBaseVisitor;

public class StringLiteralExp extends LiteralExp {
  public String value;

  public StringLiteralExp(String value) {
    this.varTypeRef = VarTypeRef.CreatePrimitiveType("string");
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
