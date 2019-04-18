package nasm.asm;

import nasm.AsmBB;
import nasm.AsmPrinter;
import nasm.reg.AsmReg;

abstract public class Asm {
	public AsmReg dst;
	public AsmReg src;
	public AsmBB blk;
	
	public Asm(AsmReg dst, AsmReg src, AsmBB blk) {
		this.dst = dst;
		this.src = src;
		this.blk = blk;
	}
	
	abstract public void AcceptPrint(AsmPrinter printer);
}
