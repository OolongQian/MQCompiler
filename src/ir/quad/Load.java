package ir.quad;

import ir.Printer;
import ir.structure.Reg;

public class Load extends Quad {
  public Reg val;
  public Reg addr;

  public Load(Reg val, Reg addr) {
    this.val = val;
    this.addr = addr;
  }

  @Override
  public void AcceptPrint(Printer printer) {
    printer.print(this);
  }
}
