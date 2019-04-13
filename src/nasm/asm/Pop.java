package nasm.asm;

import nasm.AsmBB;
import nasm.AsmPrinter;
import nasm.reg.AsmReg;

public class Pop extends Asm {
	
	public Pop(AsmReg dst, AsmBB blk) {
		super(dst, null, blk);
	}
	
	@Override
	public void AcceptPrint(AsmPrinter printer) {
		printer.Print(this);
	}
}
