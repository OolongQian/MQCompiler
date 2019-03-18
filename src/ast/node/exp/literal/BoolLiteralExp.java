package ast.node.exp.literal;

import ast.typeref.VarTypeRef;
import ast.usage.AstBaseVisitor;

public class BoolLiteralExp extends LiteralExp {
  public boolean val;

  public BoolLiteralExp() {
    this.varTypeRef = VarTypeRef.CreatePrimitiveType("bool");
  }

  public BoolLiteralExp(boolean value) {
    this.varTypeRef = VarTypeRef.CreatePrimitiveType("bool");
      this.val = value;
  }

  @Override
  public String toString() {
    if (val)
      return "true";
    else
      return "false";
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
