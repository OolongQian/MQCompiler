package nasm.inst;

import nasm.AsmBB;
import nasm.AsmPrinter;
import nasm.reg.Reg;

import java.util.LinkedList;
import java.util.List;

public class Call extends Inst {
	public String functName;
	
	public Call(AsmBB blk, String functName) {
		super(null, null, blk);
		this.functName = functName;
	}
	
	@Override
	public void AcceptPrint(AsmPrinter printer) {
		printer.Print(this);
	}
	
}
