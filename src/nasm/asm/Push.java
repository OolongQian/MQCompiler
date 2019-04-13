package nasm.asm;

import nasm.AsmBB;
import nasm.AsmPrinter;
import nasm.reg.AsmReg;

public class Push extends Asm {
	
	public Push(AsmReg src, AsmBB blk) {
		super(null, src, blk);
	}
	
	@Override
	public void AcceptPrint(AsmPrinter printer) {
		printer.Print(this);
	}
}
