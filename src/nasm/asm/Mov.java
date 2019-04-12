package nasm.asm;

import nasm.AsmBB;
import nasm.AsmPrinter;

public class Mov extends Asm {
	
	public Mov(AsmBB blk) {
		super(blk);
	}
	
	@Override
	public void AcceptPrint(AsmPrinter printer) {
		printer.Print(this);
	}
}
