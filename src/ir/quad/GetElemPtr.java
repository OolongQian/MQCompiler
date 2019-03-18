package ir.quad;

import ir.builder.Printer;
import ir.reg.IrValue;
import ir.reg.Reg;

/**
 * This is basically a add operation, I distinguish them just for clarity.
 * */
public class GetElemPtr extends Quad {
  public Reg elem;
  public Reg base;
  public IrValue offset;

  public GetElemPtr(Reg elem, Reg base, IrValue offset) {
    this.elem = elem;
    this.base = base;
    this.offset = offset;
  }

  @Override
  public void AcceptPrint(Printer printer) {
    printer.print(this);
  }
}
