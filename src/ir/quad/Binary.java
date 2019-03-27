package ir.quad;

import ir.Printer;
import ir.structure.IrValue;
import ir.structure.Reg;

import java.util.List;

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
  public Reg GetDefReg() {
    return ans;
  }
	
	@Override
	public void GetUseRegs(List<Reg> list_) {
  	if (src1 instanceof Reg) list_.add((Reg) src1);
		if (src2 instanceof Reg) list_.add((Reg) src2);
	}
	
	@Override
  public void AcceptPrint(Printer printer) {
    printer.print(this);
  }
}
