package nasm.inst;

import nasm.AsmBB;
import nasm.AsmPrinter;
import nasm.reg.AsmReg;

public class Mov extends Inst {
	
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
