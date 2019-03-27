package ir.quad;

import ir.Printer;
import ir.structure.Reg;

import java.util.LinkedList;
import java.util.List;

public class Alloca extends Quad {
	public Reg var;
	
	public Alloca(Reg var) {
		this.var = var;
	}
	
	@Override
	public Reg GetDefReg() {
		return var;
	}
	
	@Override
	public void GetUseRegs(List<Reg> list_) { }
	
	@Override
	public void AcceptPrint(Printer printer) {
		printer.print(this);
	}
}
