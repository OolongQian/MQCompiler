package nasm.asm;

import nasm.AsmBB;
import nasm.AsmPrinter;

public class Jmp extends Asm {
	
	public enum JmpOption {
		JMP, // unconditional.
		JG, // jump if greater than.
		JE, JL, JZ, JNE, JGE, JLE,
	}
	
	public JmpOption jpOp;
	public String label;
	
	public Jmp(AsmBB blk) {
		super(blk);
	}
	
	@Override
	public void AcceptPrint(AsmPrinter printer) {
		printer.Print(this);
	}
}
