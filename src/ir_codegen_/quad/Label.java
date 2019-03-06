package ir_codegen.quad;

import ir_codegen.usage.IRBaseVisitor;
import ir_codegen.util.HintName;

public class Label extends Quad implements Value {
  public Virtual virtual;

  public Label(Virtual virtual) {
    this.virtual = virtual;
  }


  @Override
  public HintName GetValueName() {
    return virtual.hintNameIR;
  }

  @Override
  public <T> T Accept(IRBaseVisitor<T> irBaseVisitor) {
    return irBaseVisitor.visit(this);
  }

}
