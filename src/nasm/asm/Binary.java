package nasm.asm;

import nasm.AsmBB;
import nasm.AsmPrinter;

public class Binary extends Asm {
	
	public enum Op {
		ADD, SUB, IMUL,
		DIV
	}
	public Op op;
	
	public Binary(AsmBB blk) {
		super(blk);
	}
	
	@Override
	public void AcceptPrint(AsmPrinter printer) {
		printer.Print(this);
	}
}
