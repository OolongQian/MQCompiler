package ast.node.exp.literal;

import ast.typeref.VarTypeRef;
import ast.usage.AstBaseVisitor;

public class BoolLiteralExp extends LiteralExp {
  public boolean value;

  @Override
  public String GetIrTmp() {
    return Boolean.toString(value);
  }

  public BoolLiteralExp() {
    this.varTypeRefDec = new VarTypeRef("bool");
  }

  public BoolLiteralExp(boolean value) {
    this.varTypeRefDec = new VarTypeRef("bool");
    this.value = value;
  }

  @Override
  public String toString() {
    if (value)
      return "true";
    else
      return "false";
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
