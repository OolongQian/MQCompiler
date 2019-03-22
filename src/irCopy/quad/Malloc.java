package ir.quad;

import ir.builder.Printer;
import ir.reg.IrValue;
import ir.reg.Reg;

/**
 * Malloc means heap allocate
 * */
public class Malloc extends Quad {
  public Reg memAddr;
  public IrValue size_;
  
  public Malloc(Reg memAddr, IrValue size_) {
    this.memAddr = memAddr;
    this.size_ = size_;
  }
  
  @Override
  public void AcceptPrint(Printer printer) {
    printer.print(this);
  }
}
