package ir.quad;


import ir.Printer;
import ir.structure.IrValue;
import ir.structure.Reg;

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
