package ir.quad;

import ir.Printer;
import ir.structure.Constant;
import ir.structure.Reg;

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
	public void ReplaceUse(Reg v, Constant c) {
		assert addr == v;
		throw new RuntimeException("I think programmer cannot load a constant address.");
	}
	
	@Override
  public void AcceptPrint(Printer printer) {
    printer.print(this);
  }
}
