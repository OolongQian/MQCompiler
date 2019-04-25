package ir.quad;

import ir.Printer;
import ir.structure.BasicBlock;
import ir.structure.IrValue;
import ir.structure.Reg;
import nasm.AsmTranslateVisitor;

import java.util.List;
import java.util.Map;

import static ir.Utility.inlinePrefix;

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
	
	@Override
	public Quad Copy(Map<String, BasicBlock> BBMap) {
		Alloca tmp = new Alloca((Reg) var.Copy());
		tmp.blk = BBMap.get( blk.name + inlinePrefix);
		return tmp;
	}
}
