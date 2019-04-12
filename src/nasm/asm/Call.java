package nasm.asm;

import nasm.AsmBB;
import nasm.AsmPrinter;

public class Call extends Asm {
	public String functName;
	
	public Call(AsmBB blk) {
		super(blk);
	}
	
	@Override
	public void AcceptPrint(AsmPrinter printer) {
		printer.Print(this);
	}
}
