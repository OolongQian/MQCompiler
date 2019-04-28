package ir.quad;


import ir.Printer;
import ir.structure.IrValue;
import ir.structure.Reg;
import nasm.AsmTranslateVisitor;

import java.util.List;

public class Ret extends Quad {
  public IrValue val;

  public Ret(IrValue val) {
    this.val = val;
  }
	
	@Override
	public void GetUseRegs(List<Reg> list_) {
		if (val instanceof Reg && !((Reg) val).name.equals("@null"))
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
}
