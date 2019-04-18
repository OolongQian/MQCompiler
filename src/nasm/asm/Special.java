package nasm.asm;

import nasm.AsmBB;
import nasm.AsmPrinter;
import nasm.reg.AsmReg;

import java.util.LinkedList;
import java.util.List;

public class Special extends Asm {
	
	public String gossip;
	
	public List<AsmReg> uses = new LinkedList<>();
	public List<AsmReg> defs = new LinkedList<>();
	
	public Special(String str, AsmBB blk) {
		super(null, null, blk);
		gossip = str;
	}
	
	@Override
	public void AcceptPrint(AsmPrinter printer) {
		printer.Print(this);
	}
}
