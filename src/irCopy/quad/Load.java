package ir.quad;

import ir.builder.Printer;
import ir.reg.Reg;

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
