package nasm.inst;

import nasm.AsmBB;
import nasm.AsmPrinter;
import nasm.reg.AsmReg;
import nasm.reg.StackMem;

// This used for Alloca (ir.quad) allocated local variable.
public class Lea extends Inst {
	
	public Lea(AsmReg dst, AsmReg src, AsmBB blk) {
		super(dst, src, blk);
		assert src instanceof StackMem;
	}
	
	@Override
	public void AcceptPrint(AsmPrinter printer) {
		printer.Print(this);
	}
}
