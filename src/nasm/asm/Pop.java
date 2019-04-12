package nasm.asm;

import nasm.AsmBB;
import nasm.AsmPrinter;

public class Pop extends Asm {
	
	public Pop(AsmBB blk) {
		super(blk);
	}
	
	@Override
	public void AcceptPrint(AsmPrinter printer) {
		printer.Print(this);
	}
}
