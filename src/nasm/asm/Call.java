package nasm.asm;

import nasm.AsmBB;
import nasm.AsmPrinter;
import nasm.reg.AsmReg;

public class Call extends Asm {
	public String functName;
	
	public Call(AsmBB blk, String functName) {
		super(null, null, blk);
		this.functName = functName;
	}
	
	@Override
	public void AcceptPrint(AsmPrinter printer) {
		printer.Print(this);
	}
}
