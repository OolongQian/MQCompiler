package ir_codegen.quad;

import ir_codegen.usage.IRBaseVisitor;
import ir_codegen.util.BasicBlock;
import ir_codegen.util.HintName;

import java.util.Hashtable;

public class Phi extends Quad implements Value {
  public Virtual phiVirtual;

  public Hashtable<BasicBlock, Value> phiOption = new Hashtable<>();

  public Phi(Virtual phiVirtual) {
    this.phiVirtual = phiVirtual;
  }

  @Override
  public <T> T Accept(IRBaseVisitor<T> irBaseVisitor) {
    return irBaseVisitor.visit(this);
  }

  @Override
  public HintName GetValueName() {
    return phiVirtual.hintNameIR;
  }
}
