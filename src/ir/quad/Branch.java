package ir.quad;

import ir.Printer;
import ir.structure.BasicBlock;
import ir.structure.IrValue;
import ir.structure.Reg;
import nasm.AsmTranslateVisitor;

import java.util.List;
import java.util.Map;

import static ir.Utility.inlinePrefix;

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
	
	@Override
	public Quad Copy(Map<String, BasicBlock> BBMap) {
  	// basic block is used by its name, except in SSA construction, thus we leave it alone.
		Branch tmp = new Branch(cond.Copy(), BBMap.get( ifTrue.name + inlinePrefix), BBMap.get( ifFalse.name + inlinePrefix));
		tmp.blk = BBMap.get( blk.name + inlinePrefix);
		return tmp;
	}
}
