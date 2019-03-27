package ir.quad;

import ir.Printer;
import ir.structure.IrValue;
import ir.structure.Reg;

import java.util.List;

public class Call extends Quad {
  public String funcName;
  public Reg ret;
  public List<IrValue> args;
	
	public Call(String funcName, Reg ret, List<IrValue> args) {
		this.funcName = funcName;
		this.ret = ret;
		this.args = args;
	}
	
	@Override
	public Reg GetDefReg() {
		return ret;
	}
	
	@Override
	public void GetUseRegs(List<Reg> list_) {
		for (IrValue arg : args) {
			if (arg instanceof Reg)
				list_.add((Reg) arg);
		}
	}
	
	@Override
  public void AcceptPrint(Printer printer) {
    printer.print(this);
  }
}
