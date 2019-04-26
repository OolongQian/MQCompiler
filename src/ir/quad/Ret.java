package ir.quad;


import ir.Printer;
import ir.structure.BasicBlock;
import ir.structure.IrValue;
import ir.structure.Reg;
import nasm.AsmTranslateVisitor;

import java.util.List;
import java.util.Map;

import static ir.Utility.inlinePrefix;

public class Ret extends Quad {
  public IrValue val;

  public Ret(IrValue val) {
    this.val = val;
  }
	
	@Override
	public void GetUseRegs(List<Reg> list_) {
		if (val instanceof Reg)
			list_.add((Reg) val);
	}
	@Override
	public void ReplaceUse(Reg v, IrValue c) {
		assert val == v;
		val = c;
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
		Ret tmp = new Ret(val.Copy());
		tmp.blk = BBMap.get( blk.name + inlinePrefix);
		return tmp;
	}
}
