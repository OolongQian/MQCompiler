package nasm.inst;

import nasm.AsmBB;
import nasm.AsmPrinter;

public class Ret extends Inst {
	
	public Ret(AsmBB blk) {
		super(null, null, blk);
	}
	
	@Override
	public void AcceptPrint(AsmPrinter printer) {
		printer.Print(this);
	}
}
