package ir.quad;

import ir.Printer;
import ir.structure.BasicBlock;
import ir.structure.Reg;

import java.util.HashMap;
import java.util.Map;

/**
 * Phi function is used for tmp construction optimization.
 *
 * Phi is subtle, at first, it's constructed in such a way that Reg var, and all the IrValue are
 * actually the same var instance. Later we do renaming, make none of them the original identical Reg.
 * */
public class Phi extends Quad {
	public Reg var;
	public Map<BasicBlock, Reg> options = new HashMap<>();
	
	public Phi(Reg var) {
		this.var = var;
	}
	
	@Override
	public void AcceptPrint(Printer printer) {
		printer.print(this);
	}
}
