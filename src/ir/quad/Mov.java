package ir.quad;

import ir.Printer;
import ir.structure.IrValue;
import ir.structure.Reg;

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
	public void AcceptPrint(Printer printer) {
		printer.print(this);
	}
}
