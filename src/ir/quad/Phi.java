package ir.quad;

import ir.Printer;
import ir.structure.BasicBlock;
import ir.structure.Constant;
import ir.structure.IrValue;
import ir.structure.Reg;
import nasm.AsmTranslateVisitor;
import nasm.asm.Asm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Phi function is used for tmp construction optimization.
 *
 * Phi is subtle, at first, it's constructed in such a way that Reg var, and all the IrValue are
 * actually the same var instance. Later we do renaming, make none of them the original identical Reg.
 * */
public class Phi extends Quad {
	public Reg var;
	public Map<BasicBlock, IrValue> options = new HashMap<>();
	
	public Phi(Reg var) {
		this.var = var;
	}
	
	@Override
	public Reg GetDefReg() {
		return var;
	}
	@Override
	public void GetUseRegs(List<Reg> list_) {
		for (IrValue val : options.values()) {
			if (val instanceof Reg)
				list_.add((Reg) val);
		}
	}
	@Override
	public void ReplaceUse(Reg v, IrValue c) {
		boolean replace = false;
		for (BasicBlock key : options.keySet()) {
			if (options.get(key) == v) {
				options.put(key, c);
				replace = true;
			}
		}
		assert replace;
	}
	
	/**
	 * Used by constant propagation.
	 * */
	public boolean CheckAllSameConst() {
		if (!(options.values().iterator().next() instanceof Constant))
			return false;
		
		Constant const_ = (Constant) options.values().iterator().next();
		for (IrValue irVal : options.values()) {
			if (irVal != const_)
				return false;
		}
		return true;
	}
	
	@Override
	public void AcceptPrint(Printer printer) {
		printer.print(this);
	}
	
	@Override
	public void AcceptTranslator(AsmTranslateVisitor translator) {
		assert false;
	}
}
