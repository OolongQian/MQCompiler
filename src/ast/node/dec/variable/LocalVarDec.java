package ast.node.dec.variable;

import ast.usage.AstBaseVisitor;

public class LocalVarDec extends VarDecList {
  public LocalVarDec(VarDecList varDecList) {
    super(varDecList);
  }

  @Override
  protected String SelfDeclare() {
    return "LocalVarDec: \n";
  }

  @Override
  public String PrettyPrint() {
    return Formatter(this, super.toString() + ";\n");
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
