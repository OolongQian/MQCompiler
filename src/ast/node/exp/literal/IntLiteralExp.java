package ast.node.exp.literal;

import ast.typeref.VarTypeRef;
import ast.usage.AstBaseVisitor;

public class IntLiteralExp extends LiteralExp {
  public int val;

  public IntLiteralExp(int value) {
    this.varTypeRef = VarTypeRef.CreatePrimitiveType("int");
    this.val = value;
  }

  @Override
  public String toString() {
    return Integer.toString(val);
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
