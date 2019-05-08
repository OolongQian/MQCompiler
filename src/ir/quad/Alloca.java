package ir.quad;

import ir.Printer;
import ir.structure.IrValue;
import ir.structure.Reg;
import nasm.AsmTranslateVisitor;

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
	public void ReplaceUse(Reg v, IrValue val) {
		;
	}
	
	@Override
	public void GetUseRegs(List<Reg> list_) { }
	
	@Override
	public void AcceptPrint(Printer printer) {
		printer.print(this);
	}
	
	@Override
	public void AcceptTranslator(AsmTranslateVisitor translator) {
		translator.visit(this);
	}
}
