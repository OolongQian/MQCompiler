package nasm.inst;

import nasm.AsmBB;
import nasm.AsmPrinter;

public class Jmp extends Inst {
	
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
	
	@Override
	public void AcceptPrint(AsmPrinter printer) {
		printer.Print(this);
	}
}
