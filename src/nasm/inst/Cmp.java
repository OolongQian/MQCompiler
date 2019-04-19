package nasm.inst;

import ir.quad.Binary;
import nasm.AsmBB;
import nasm.AsmPrinter;
import nasm.reg.AsmReg;
import nasm.reg.Reg;

import java.util.LinkedList;
import java.util.List;

/** Cmp inst consists of compare two source registers, and it's also
 * responsible for flag setting and later extension. */
public class Cmp extends Inst {

	public Binary.Op cmpType;
	
	// if jump directly, no need to explicitly restore cmp's value.
	public boolean jmp;
	
	/** dst is src1, src is src2 */
	public Cmp(AsmReg dst, AsmReg src, AsmBB blk) {
		super(dst, src, blk);
		this.jmp = true;
	}
	public Cmp(AsmReg dst, AsmReg src, Binary.Op cmpType, AsmBB blk) {
		super(dst, src, blk);
		this.cmpType = cmpType;
		this.jmp = false;
	}
	
	public Reg flagReg;
	
	public String Cmp2Set() {
		switch (cmpType) {
			case EQ : return "sete";
			case NE : return "setne";
			case LT : return "setl";
			case GT : return "setg";
			case LE : return "setle";
			case GE : return "setge";
			default: assert false;
		}
		return null;
	}

	
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
