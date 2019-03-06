package ast.node.exp.literal;

import ast.node.exp.Exp;
import ir_codegen.quad.IrValue;

abstract public class LiteralExp extends Exp implements IrValue {

  @Override
  public void AssignIrTmp(String hintName) {
    System.out.println("immediate shouldn't be assign tmp\n");
  }

  @Override
  public String GetIrTmp() {
    return null;
  }
}
