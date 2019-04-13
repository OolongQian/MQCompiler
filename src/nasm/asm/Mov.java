package nasm.asm;

import nasm.AsmBB;
import nasm.AsmPrinter;
import nasm.reg.AsmReg;

public class Mov extends Asm {
	
	// movzx rax al.
	public boolean extend = false;
	
	public Mov(AsmReg dst, AsmReg src, AsmBB blk) {
		super(dst, src, blk);
	}
	
	@Override
	public void AcceptPrint(AsmPrinter printer) {
		printer.Print(this);
	}
}
