package ir_codegen.quad;

import ir_codegen.usage.IRBaseVisitor;
import ir_codegen.util.BasicBlock;

/***
 * Unconditional jump
 */
public class Jump extends Quad {
  public BasicBlock labelTarget;

  public Jump(BasicBlock labelTarget) {
    this.labelTarget = labelTarget;
  }

  @Override
  public <T> T Accept(IRBaseVisitor<T> irBaseVisitor) {
    return irBaseVisitor.visit(this);
  }
}
