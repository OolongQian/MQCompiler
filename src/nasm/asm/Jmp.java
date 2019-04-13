package nasm.asm;

import nasm.AsmBB;
import nasm.AsmPrinter;
import nasm.reg.AsmReg;

public class Jmp extends Asm {
	
	public enum JmpOption {
		JMP, // unconditional.
		JG, // jump if greater than.
		JE, JL, JZ, JNE, JGE, JLE,
	}

	public JmpOption jpOp;
	public String label;
	
	public Jmp(AsmBB blk, JmpOption jpOp, String label) {
		super(null, null, blk);
		this.jpOp = jpOp;
		this.label = label;
	}
	
	public Jmp(AsmBB blk, String label) {
		super(null, null, blk);
		this.label = label;
	}
	
	@Override
	public void AcceptPrint(AsmPrinter printer) {
		printer.Print(this);
	}
}
