package nasm.inst;

import nasm.AsmBB;
import nasm.AsmPrinter;
import nasm.reg.AsmReg;
import nasm.reg.Reg;

import java.util.LinkedList;
import java.util.List;

/** Cmp inst consists of compare two source registers, and it's also
 * responsible for flag setting and later extension. */
public class Cmp extends Inst {
	
	/** dst is src1, src is src2 */
	public Cmp(AsmReg dst, AsmReg src, AsmBB blk) {
		super(dst, src, blk);
	}
	
	public Reg flagReg;
	
//	String set = "";
//		switch (quad.op) {
//		case EQ: set = "sete"; break;
//		case NE: set = "setne"; break;
//		case LT: set = "setl"; break;
//		case GT: set = "setg"; break;
//		case LE: set = "setle"; break;
//		case GE: set = "setge"; break;
//		default: assert false;
//	}
//	// FIXME : here don't have to be al, but we require currently.
//	// set conditional flag.
//	Special setFlag = new Special(set + " al", cur);
//		setFlag.defs.add(GetPReg(rax));
//		cur.insts.add(setFlag);
//	// extend rax
//	Mov ext = new Mov(GetPReg(rax), GetPReg(rax), cur);
//	ext.extend = true;
//		cur.insts.add(ext);
	
	@Override
	public void AcceptPrint(AsmPrinter printer) {
		printer.Print(this);
	}
}
