package nasm.inst;

import nasm.AsmBB;
import nasm.AsmPrinter;
import nasm.reg.AsmReg;
import nasm.reg.Reg;

import java.util.List;

abstract public class Inst {
	public AsmReg dst;
	public AsmReg src;
	public AsmBB blk;
	
	public Inst(AsmReg dst, AsmReg src, AsmBB blk) {
		this.dst = dst;
		this.src = src;
		this.blk = blk;
	}
	
	abstract public void AcceptPrint(AsmPrinter printer);
}
