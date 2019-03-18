package ir.quad;

import ir.builder.Printer;
import ir.reg.IrValue;

public class Ret extends Quad {
  public IrValue val;

  public Ret(IrValue val) {
    this.val = val;
  }

  @Override
  public void AcceptPrint(Printer printer) {
    printer.print(this);
  }
}
