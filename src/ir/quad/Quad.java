package ir.quad;

import ir.Printer;
import ir.structure.BasicBlock;
import ir.structure.IrValue;
import ir.structure.Reg;
import nasm.AsmTranslateVisitor;

import java.util.List;

abstract public class Quad {
	public BasicBlock blk;
	
	/**
	 * Get a reg which is defined by this quad.
	 * */
	public Reg GetDefReg() { return null; }
	/**
	 * Get a list of regs which are used by current quad.
	 * */
	public void GetUseRegs(List<Reg> list_) {}
	
	/**
	 * Applied in ConstantPropagation and CopyPropagation.
	 * */
	abstract public void ReplaceUse(Reg v, IrValue val);
	
	abstract public void AcceptPrint(Printer printer);
	
	abstract public void AcceptTranslator (AsmTranslateVisitor translator);
	
}
