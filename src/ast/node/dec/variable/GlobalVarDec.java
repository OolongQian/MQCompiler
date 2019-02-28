package ast.node.dec.variable;

import ast.usage.AstBaseVisitor;

public class GlobalVarDec extends VarDecList {
  public GlobalVarDec(VarDecList varDecList) {
    super(varDecList);
  }

  @Override
  protected String SelfDeclare() {
    return "GlobalVarDec: \n";
  }

  @Override
  public String PrettyPrint() {
    return Formatter(this, super.toString() + ";\n");
  }

  @Override
  public void Accept(AstBaseVisitor visitor) {
    visitor.visit(this);
  }
}
