package nasm.asm;

import nasm.AsmBB;
import nasm.AsmPrinter;

public class Ret extends Asm {
	
	public Ret(AsmBB blk) {
		super(blk);
	}
	
	@Override
	public void AcceptPrint(AsmPrinter printer) {
		printer.Print(this);
	}
}
