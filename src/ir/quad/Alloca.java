package ir.quad;

import ir.Printer;
import ir.structure.Reg;

public class Alloca extends Quad {
	public Reg var;
	
	public Alloca(Reg var) {
		this.var = var;
	}
	
	@Override
	public void AcceptPrint(Printer printer) {
		printer.print(this);
	}
}
