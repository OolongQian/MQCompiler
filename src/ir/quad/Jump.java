package ir.quad;

import ir.builder.Printer;
import ir.util.BasicBlock;

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
