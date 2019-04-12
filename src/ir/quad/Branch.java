package ir.quad;

import ir.Printer;
import ir.structure.BasicBlock;
import ir.structure.IrValue;
import ir.structure.Reg;
import nasm.AsmTranslateVisitor;
import nasm.asm.Asm;

import java.util.List;

public class Branch extends Quad {
  public IrValue cond;
  public BasicBlock ifTrue;
  public BasicBlock ifFalse;

  public Branch(IrValue cond, BasicBlock ifTrue, BasicBlock ifFalse) {
    this.cond = cond;
    this.ifTrue = ifTrue;
    this.ifFalse = ifFalse;
  }
  
  @Override
  public void GetUseRegs(List<Reg> list_) {
  	if (cond instanceof Reg)
  		list_.add((Reg) cond);
  }
  
  @Override
  public void ReplaceUse(Reg v, IrValue c) {
		assert cond == v;
	  cond = c;
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
