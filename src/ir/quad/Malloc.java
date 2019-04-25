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
 * Malloc means heap allocate
 * */
public class Malloc extends Quad {
  public Reg memAddr;
  public IrValue size_;
  
  public Malloc(Reg memAddr, IrValue size_) {
    this.memAddr = memAddr;
    this.size_ = size_;
  }
  
  @Override
  public Reg GetDefReg() {
    return memAddr;
  }
  @Override
  public void GetUseRegs(List<Reg> list_) {
  	if (size_ instanceof Reg)
  		list_.add((Reg) size_);
  }
	@Override
	public void ReplaceUse(Reg v, IrValue c) {
		assert size_ == v;
		size_ = c;
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
		Malloc tmp = new Malloc((Reg) memAddr.Copy(), size_.Copy());
		tmp.blk = BBMap.get(blk.name + inlinePrefix);
		return tmp;
	}
}
