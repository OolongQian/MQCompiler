package ir.quad;

import ir.Printer;
import ir.structure.BasicBlock;
import ir.structure.Constant;
import ir.structure.IrValue;
import ir.structure.Reg;

import java.util.List;

abstract public class Quad {
	abstract public void AcceptPrint(Printer printer);
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
	public void ReplaceUse(Reg v, IrValue val) {}
}
