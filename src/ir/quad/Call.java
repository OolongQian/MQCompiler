package ir.quad;

import ir.Printer;
import ir.structure.BasicBlock;
import ir.structure.IrValue;
import ir.structure.Reg;
import nasm.AsmTranslateVisitor;

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
	
	public Call(BasicBlock blk, String funcName, Reg ret, List<IrValue> args) {
		this.blk = blk;
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
	public void ReplaceUse(Reg v, IrValue c) {
		boolean replace = false;
		for (int i = 0; i < args.size(); ++i) {
			if (args.get(i) == v) {
				args.set(i, c);
				replace = true;
			}
		}
		assert replace;
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
