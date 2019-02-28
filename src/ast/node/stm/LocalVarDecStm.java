package ast.node.stm;

import ast.node.dec.variable.LocalVarDec;
import ast.usage.AstBaseVisitor;

public class LocalVarDecStm extends Stm {
  public LocalVarDec localVarDec;

  public LocalVarDecStm(LocalVarDec localVarDec) {
    this.localVarDec = localVarDec;
  }

  @Override
  public String PrettyPrint() {
    return localVarDec.PrettyPrint();
  }

  @Override
  public void Accept(AstBaseVisitor visitor) {
    visitor.visit(this);
  }
}
