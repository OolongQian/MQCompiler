package nasm.inst;

import nasm.AsmBB;
import nasm.AsmPrinter;
import nasm.reg.AsmReg;
import nasm.reg.GlobalMem;
import nasm.reg.Imm;

// load dst cannot be global.
public class Load extends Inst {
	public Load(AsmReg dst, AsmReg src, AsmBB blk) {
		super(dst, src, blk);
		assert !(dst instanceof GlobalMem) && !(dst instanceof Imm);
	}
	
	@Override
	public void AcceptPrint(AsmPrinter printer) {
		printer.Print(this);
	}
}
