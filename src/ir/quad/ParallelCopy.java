package ir.quad;

import ir.Printer;
import ir.structure.BasicBlock;
import ir.structure.IrValue;
import ir.structure.Reg;
import nasm.AsmTranslateVisitor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ParallelCopy extends Quad {
	
//	public Set<Store> copies = new HashSet<>();
	
	public Set<Mov> copies = new HashSet<>();
	
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
	
	@Override
	public Quad Copy(Map<String, BasicBlock> BBMap) {
//		ParallelCopy pcopy = new ParallelCopy();
//		copies.forEach(x -> pcopy.copies.add((Mov) x.Copy()));
//		return pcopy;
		assert false;
		return new ParallelCopy();
	}
}
