package ir_codegen.quad;

import ir_codegen.usage.IRBaseVisitor;
import ir_codegen.util.HintName;

import static ir_codegen.util.Info.tempTypeName;

public class AllocaQuad extends Quad implements Value {
  public Var var;

  public AllocaQuad(Var var) {
    this.var = var;
  }

  @Override
  public String toString() {
    return var.hintNameIR.hintName + " = " + "alloca " + tempTypeName + ", " + "align " + var.getMemoerySpace() + "\n";
  }

  @Override
  public HintName GetValueName() {
    return var.hintNameIR;
  }

  @Override
  public <T> T Accept(IRBaseVisitor<T> irBaseVisitor) {
    return irBaseVisitor.visit(this);
  }
}
