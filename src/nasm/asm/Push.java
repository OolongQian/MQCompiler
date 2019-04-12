package nasm.asm;

import nasm.AsmBB;
import nasm.AsmPrinter;

public class Push extends Asm {
	
	public Push(AsmBB blk) {
		super(blk);
	}
	
	@Override
	public void AcceptPrint(AsmPrinter printer) {
		printer.Print(this);
	}
}
