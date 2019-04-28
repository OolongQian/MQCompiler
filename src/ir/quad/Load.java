package ir.quad;


import ir.Printer;
import ir.structure.IrValue;
import ir.structure.Reg;
import nasm.AsmTranslateVisitor;

import java.util.List;

public class Load extends Quad {
  public Reg val;
  public Reg addr;

  public Load(Reg val, Reg addr) {
    this.val = val;
    this.addr = addr;
  }
	
  // define, although no use.
	@Override
	public Reg GetDefReg() {
		return val;
	}
	@Override
	public void GetUseRegs(List<Reg> list_) {
		list_.add(addr);
	}
	@Override
	public void ReplaceUse(Reg v, IrValue c) {
		assert addr == v;
		addr = v;
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
