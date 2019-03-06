package ir_codegen.quad;

import ir_codegen.usage.IRBaseVisitor;
import ir_codegen.util.BasicBlock;

/**
 * Conditional Jump
 * */
public class Branch extends Quad {
  public Value cond;
  public BasicBlock labelTrue;
  public BasicBlock labelFalse;

  public Branch(Value cond, BasicBlock labelTrue, BasicBlock labelFalse) {
    this.cond = cond;
    this.labelTrue = labelTrue;
    this.labelFalse = labelFalse;
  }

  @Override
  public <T> T Accept(IRBaseVisitor<T> irBaseVisitor) {
    return irBaseVisitor.visit(this);
  }
}
