package ir.quad;

import ir.builder.Printer;
import ir.reg.IrValue;
import ir.reg.Reg;

public class Unary extends Quad {
  public enum Op {
    BITNOT, NEG
  }

  // ans is val instead of addr.
  public Reg ans;
  public Op op;
  public IrValue src;


  public Unary(Reg ans, Op op, IrValue src) {
    this.ans = ans;
    this.op = op;
    this.src = src;
  }

  @Override
  public void AcceptPrint(Printer printer) {
    printer.print(this);
  }
}
