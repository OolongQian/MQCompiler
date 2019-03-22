package ir.quad;

import ir.Printer;
import ir.structure.IrValue;
import ir.structure.Reg;

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
