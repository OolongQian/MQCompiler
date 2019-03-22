package ir.quad;

import ir.builder.Printer;
import ir.reg.IrValue;
import ir.reg.Reg;

public class Store extends Quad {
  public Reg dst;
  public IrValue src;

  public Store(Reg dst, IrValue src) {
    this.dst = dst;
    this.src = src;
  }

  @Override
  public void AcceptPrint(Printer printer) {
    printer.print(this);
  }
}
