package ir.quad;

import ir.Printer;
import ir.structure.BasicBlock;
import ir.structure.IrValue;
import ir.structure.Reg;
import nasm.AsmTranslateVisitor;

import java.util.List;
import java.util.Map;

import static ir.Utility.inlinePrefix;

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
	public void ReplaceUse(Reg v, IrValue c) {
		assert src == v;
		src = c;
	}
	
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
		Mov tmp = new Mov ((Reg) dst.Copy(), src.Copy());
		tmp.blk = BBMap.get(blk.name + inlinePrefix);
		return tmp;
	}
}
