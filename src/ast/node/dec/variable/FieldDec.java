package ast.node.dec.variable;

import ast.usage.AstBaseVisitor;

public class FieldDec extends VarDecList {
  public FieldDec(VarDecList varDecList) {
    super(varDecList);
  }

  @Override
  protected String SelfDeclare() {
    return "FieldVarDec: \n";
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