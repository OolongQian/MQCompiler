package ir.quad;

import ir.builder.Printer;
import ir.reg.IrValue;
import ir.reg.Reg;

public class Binary extends Quad {
  public enum Op {
    ADD, SUB, MUL, DIV, MOD,
    SHL, SHR,
    AND, XOR, OR,
    GT, GE, LT, LE,
	  EQ, NE,
    LAND, LOR, // logical AND & OR.
    CONCAT
  }

  // ans is val instead of addr.
  public Reg ans;
  public Op op;
  public IrValue src1;
  public IrValue src2;

  public Binary(Reg ans, Op op, IrValue src1, IrValue src2) {
    this.ans = ans;
    this.op = op;
    this.src1 = src1;
    this.src2 = src2;
  }

  @Override
  public void AcceptPrint(Printer printer) {
    printer.print(this);
  }
}
