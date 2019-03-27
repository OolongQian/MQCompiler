package ir.quad;

import ir.Printer;
import ir.structure.Constant;
import ir.structure.IrValue;
import ir.structure.Reg;

import java.util.List;

/**
 * A Quad used by tmp construction.
 * */
public class Mov extends Quad {
	public Reg dst;
	public IrValue src;
	
	public Mov(Reg dst, IrValue src) {
		this.dst = dst;
		this.src = src;
	}
	
	@Override
	public Reg GetDefReg() {
		return dst;
	}
	@Override
	public void GetUseRegs(List<Reg> list_) {
		if (src instanceof Reg)
			list_.add((Reg) src);
	}
	@Override
	public void ReplaceUse(Reg v, Constant c) {
		assert src == v;
		src = c;
	}
	
	@Override
	public void AcceptPrint(Printer printer) {
		printer.print(this);
	}
}
