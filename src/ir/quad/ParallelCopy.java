package ir.quad;

import ir.Printer;
import ir.structure.IrValue;
import ir.structure.Reg;
import nasm.AsmTranslateVisitor;

import java.util.HashSet;
import java.util.Set;

public class ParallelCopy extends Quad {
	
	public Set<Store> copies = new HashSet<>();
	
	@Override
	public void ReplaceUse(Reg v, IrValue val) {
		assert false;
	}
	
	@Override
	public void AcceptPrint(Printer printer) {
		;
	}
	
	@Override
	public void AcceptTranslator(AsmTranslateVisitor translator) {
		assert false;
	}
}
