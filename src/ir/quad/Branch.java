package ir.quad;

import ir.Printer;
import ir.structure.BasicBlock;
import ir.structure.Constant;
import ir.structure.IrValue;
import ir.structure.Reg;

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

}
