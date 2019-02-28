package ast.node.exp;

import ast.typeref.VarTypeRef;
import ast.usage.AstBaseVisitor;

public class NullExp extends Exp {

  public NullExp() {
    varTypeRefDec = new VarTypeRef("null");
  }

  @Override
  public String toString() {
    return "null";
  }

  @Override
  public void Accept(AstBaseVisitor visitor) {
    visitor.visit(this);
  }
}
