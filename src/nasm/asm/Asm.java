package nasm.asm;

import nasm.AsmBB;
import nasm.AsmPrinter;
import nasm.reg.AsmReg;

abstract public class Asm {
	public AsmBB blk;
	public AsmReg dst;
	public AsmReg src;
	
	
	public Asm(AsmBB blk) {
		this.blk = blk;
	}
	
	abstract public void AcceptPrint(AsmPrinter printer);
}
