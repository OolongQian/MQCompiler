package nasm.asm;

import nasm.AsmBB;
import nasm.AsmPrinter;
import nasm.reg.AsmReg;

public class Oprt extends Asm {
	
	public enum Op {
		ADD, SUB,
		IMUL, IDIV,
		AND, OR, XOR,
		SHL, SAR,
		
		NEG, BIT_NOT
	}
	public Op op;
	
	public Oprt(AsmReg dst, AsmReg src, AsmBB blk, Op op) {
		super(dst, src, blk);
		this.op = op;
	}
	
	public Oprt(AsmReg dst, AsmBB blk, Op op) {
		super(dst, null, blk);
		assert op == Op.NEG || op == Op.BIT_NOT;
		this.op = op;
	}
	
	@Override
	public void AcceptPrint(AsmPrinter printer) {
		printer.Print(this);
	}
}
