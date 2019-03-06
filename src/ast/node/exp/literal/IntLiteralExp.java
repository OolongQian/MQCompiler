package ast.node.exp.literal;

import ast.typeref.VarTypeRef;
import ast.usage.AstBaseVisitor;
import ir_codegen.quad.IrValue;

public class IntLiteralExp extends LiteralExp {
  public int value;

  @Override
  public String GetIrTmp() {
    return Integer.toString(value);
  }

  public IntLiteralExp(int value) {
    this.varTypeRefDec = new VarTypeRef("int");
    this.value = value;
  }

  @Override
  public String toString() {
    return Integer.toString(value);
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
