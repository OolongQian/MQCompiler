package nasm.asm;

import nasm.AsmBB;
import nasm.AsmPrinter;
import nasm.reg.AsmReg;

public class Cmp extends Asm {
	/** dst is src1, src is src2 */
	public Cmp(AsmReg dst, AsmReg src, AsmBB blk) {
		super(dst, src, blk);
	}
	
	@Override
	public void AcceptPrint(AsmPrinter printer) {
		printer.Print(this);
	}
}
