package ir.quad;

import ir.Printer;
import ir.structure.BasicBlock;
import ir.structure.IrValue;
import ir.structure.Reg;
import nasm.AsmTranslateVisitor;

import java.util.List;

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
	
	public Malloc(BasicBlock blk, Reg memAddr, IrValue size_) {
  	this.blk = blk;
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
}
