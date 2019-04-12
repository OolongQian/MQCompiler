package nasm.asm;

import nasm.AsmBB;
import nasm.AsmPrinter;

public class Cmp extends Asm {
	/** dst is src1, src is src2 */
	
	public Cmp(AsmBB blk) {
		super(blk);
	}
	
	@Override
	public void AcceptPrint(AsmPrinter printer) {
		printer.Print(this);
	}
}
