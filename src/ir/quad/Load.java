package ir.quad;


import ir.Printer;
import ir.structure.BasicBlock;
import ir.structure.IrValue;
import ir.structure.Reg;
import nasm.AsmTranslateVisitor;

import java.util.List;
import java.util.Map;

import static ir.Utility.inlinePrefix;

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
		throw new RuntimeException("I think programmer cannot load a constant address.");
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
		Load tmp = new Load ((Reg) val.Copy(), (Reg) addr.Copy());
		tmp.blk = BBMap.get( blk.name + inlinePrefix);
		return tmp;
	}
}
