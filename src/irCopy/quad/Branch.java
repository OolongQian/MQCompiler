package ir.quad;

import ir.builder.Printer;
import ir.reg.IrValue;
import ir.reg.Reg;
import ir.util.BasicBlock;

public class Branch extends Quad {
  public IrValue cond;
  public BasicBlock ifTrue;
  public BasicBlock ifFalse;

  public Branch(IrValue cond, BasicBlock ifTrue, BasicBlock ifFalse) {
    this.cond = cond;
    this.ifTrue = ifTrue;
    this.ifFalse = ifFalse;
  }

  @Override
  public void AcceptPrint(Printer printer) {
    printer.print(this);
  }

}