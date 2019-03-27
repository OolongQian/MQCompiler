package ir.quad;


import ir.Printer;
import ir.structure.IrValue;
import ir.structure.Reg;

import java.util.List;

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
  public void AcceptPrint(Printer printer) {
    printer.print(this);
  }
}
