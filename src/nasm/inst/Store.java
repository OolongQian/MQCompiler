package nasm.inst;

import nasm.AsmBB;
import nasm.AsmPrinter;
import nasm.reg.AsmReg;
import nasm.reg.GlobalMem;
import nasm.reg.StackMem;

// stringLiteral cannot be stored.
// store dst's value is the address that is stored, which means dst cannot be StackMem.
public class Store extends Inst {
	
	public Store(AsmReg dst, AsmReg src, AsmBB blk) {
		super(dst, src, blk);
		assert !(dst instanceof GlobalMem && ((GlobalMem) dst).isString);
		assert !(dst instanceof StackMem);
	}
	
	@Override
	public void AcceptPrint(AsmPrinter printer) {
		printer.Print(this);
	}
}
