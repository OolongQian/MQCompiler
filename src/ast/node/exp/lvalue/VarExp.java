package ast.node.exp.lvalue;

import ast.node.dec.variable.VarDec;
import ast.usage.AstBaseVisitor;
import ir_codegen.quad.IrValue;


import static ir_codegen.util.Namer.GetHintName;

/**
 * Why not attach information to ast ?
 * Because varDec thread is attached to ast.
 * */
public class VarExp extends LValueExp implements IrValue {
  public String varName;
  public VarDec varDec;

  private String tmpVirReg;

  public String GetIrVar() {
    return varDec.hintName;
  }

  @Override
  public void AssignIrTmp(String hintName) {
    tmpVirReg = GetHintName(hintName);
  }

  @Override
  public String GetIrTmp() {
    return tmpVirReg;
  }

  public VarExp(String varName) {
    this.varName = varName;
  }

  @Override
  public String toString() {
    return varName;
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
