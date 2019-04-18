package nasm.inst;

import nasm.AsmBB;
import nasm.AsmPrinter;

public class Msg extends Inst {
	
	public String msg;
	
	public Msg(AsmBB blk, String msg) {
		super(null, null, blk);
		this.msg = (msg == null) ? "" : msg;
	}
	
	@Override
	public void AcceptPrint(AsmPrinter printer) {
		printer.Print(this);
	}
}
