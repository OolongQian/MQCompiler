package ir.quad;


import ir.Printer;
import ir.structure.IrValue;

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
