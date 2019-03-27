package ir.quad;

import ir.Printer;
import ir.structure.BasicBlock;
import ir.structure.Reg;

import java.util.List;

public class Jump extends Quad {
  public BasicBlock target;

  public Jump(BasicBlock target) {
    this.target = target;
  }
  
  @Override
  public void AcceptPrint(Printer printer) {
    printer.print(this);
  }
}
