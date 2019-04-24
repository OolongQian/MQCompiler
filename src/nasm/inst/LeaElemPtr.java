package nasm.inst;

import nasm.AsmBB;
import nasm.AsmPrinter;
import nasm.reg.AsmReg;

public class LeaElemPtr extends Inst {
	
	public AsmReg base;
	public AsmReg index;
	
	public LeaElemPtr(AsmReg dst, AsmReg base, AsmReg index, AsmBB blk) {
		super(dst, null, blk);
		this.base = base;
		this.index = index;
	}
	
	@Override
	public void AcceptPrint(AsmPrinter printer) {
		printer.Print(this);
	}
}
