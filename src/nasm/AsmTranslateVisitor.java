package nasm;


import ir.quad.*;
import ir.structure.*;
import nasm.asm.*;
import nasm.asm.Call;
import nasm.asm.Mov;
import nasm.asm.Ret;
import nasm.reg.*;

import java.util.*;

import static ir.quad.Binary.Op.DIV;
import static ir.quad.Binary.Op.SHL;
import static nasm.asm.Oprt.Op.*;

/**
 * Note : all registers are separate in current implementation.
 * */
public class AsmTranslateVisitor {
	
	/** current AsmBB handler */
	private AsmBB cur;
	
	// for global variable, @a -> _G_a.
	// for stringLiteral, *1 -> _S_1.
	private Map<String, GlobalMem> globals;
	
	/********************* data structure config **************************/
	// must be set before translation.
	public void ConfigTranslateContext (Map<String, GlobalMem> globals) {
		this.globals = globals;
	}
	
	// set before translation for each basic block.
	public void ConfigAsmBB (AsmBB bb) {
		cur = bb;
	}

	/******************** translation starter and utility *********************/
	/** need flexibility to get next quad or skip one quad. */
	public void TranslateQuadList (List<Quad> quads) {
		quads.forEach(x -> x.AcceptTranslator(this));
	}
	
	/******************** calling convention *************************/
	public static List<String> callArgRegs = new LinkedList<>();
	{
		callArgRegs.add("rdi");
		callArgRegs.add("rsi");
		callArgRegs.add("rdx");
		callArgRegs.add("rcx");
		callArgRegs.add("r8");
		callArgRegs.add("r9");
	}
	
	/******************* concrete visit actions ***********************/
	private void visit (ir.quad.Quad quad) {
		quad.AcceptTranslator(this);
	}
	
	/** move can be mem to mem */
	public void visit (ir.quad.Mov quad) {
		Mov mov = new Mov (GetAsmReg(quad.dst), GetAsmReg(quad.src), cur);
		cur.asms.add(mov);
	}

	// FIXME : bin cannot mem to mem.
	public void visit (ir.quad.Binary quad) {
		switch (quad.op) {
			case DIV: case MOD:
				TransDivMod(quad);
				break;
				
			case SHL: case SHR:
				TransShift(quad);
				break;
				
				
			case EQ: case NE:
			case GT: case LT:
			case GE: case LE:
				TransCmp (quad);
				break;
				
			default:
				Oprt.Op arithOp = null;
				switch (quad.op) {
					case ADD: arithOp = ADD; break;
					case SUB: arithOp = SUB; break;
					case MUL: arithOp = IMUL; break;
					case AND: arithOp = AND; break;
					case OR: arithOp = OR; break;
					case XOR: arithOp = XOR; break;
					default:
						System.err.println("binary quad has unhandled operation type");
						assert false;
				}
				cur.asms.add(new Mov (GetAsmReg(quad.ans), GetAsmReg(quad.src1), cur));
				cur.asms.add(new Oprt(GetAsmReg(quad.ans), GetAsmReg(quad.src2), cur, arithOp));
				break;
		}
	}
	
	private void TransDivMod (Binary quad) {
		// rax is dividend,
		cur.asms.add(new Mov(GetPReg("rax"), GetAsmReg(quad.src1), cur));
		// rdx is cleared by cqo.
//		cur.asms.add(new Special("cqo", cur));
		cur.asms.add(new Mov (GetPReg("rdx"), new Imm(0), cur));
		// issue idiv divider, to conduct idiv.
		cur.asms.add(new Oprt(null, GetAsmReg(quad.src2), cur, Oprt.Op.IDIV));
		// quotient is stored in rax, remainder is in rdx
		cur.asms.add(new Mov(GetAsmReg(quad.ans),
						(quad.op == DIV) ? GetPReg("rax") : GetPReg("rdx"),
						cur));
	}
	
	private void TransShift (Binary quad) {
		// *** : shift value must be in rcx's cl or constant.
		// move src1 to dst
		cur.asms.add(new Mov (GetAsmReg(quad.ans), GetAsmReg(quad.src1), cur));
		// shift ans
		Oprt.Op shiftOp = (quad.op == SHL) ? SAR : Oprt.Op.SHL;
		if (quad.src2 instanceof Constant)
			// shift ans directly by constant
			cur.asms.add(new Oprt(GetAsmReg(quad.ans), GetAsmReg(quad.src2), cur, shiftOp));
		else {
			// shift ans by put shift amount into rcx.
			cur.asms.add(new Mov (GetPReg("rcx"), GetAsmReg(quad.src2), cur));
			cur.asms.add(new Oprt(GetAsmReg(quad.ans), GetPReg("cl"), cur, shiftOp));
		}
	}
	
	// FIXME : may cannot cmp mem to mem
	private void TransCmp (Binary quad) {
		cur.asms.add(new Cmp (GetAsmReg(quad.src1), GetAsmReg(quad.src2), cur));
		String set = "";
		switch (quad.op) {
			case EQ: set = "sete"; break;
			case NE: set = "setne"; break;
			case LT: set = "setl"; break;
			case GT: set = "setg"; break;
			case LE: set = "setle"; break;
			case GE: set = "setge"; break;
			default: assert false;
		}
		// FIXME : here don't have to be al, but we require currently.
		// set conditional flag.
		Special setFlag = new Special(set + " al", cur);
		setFlag.defs.add(new PhysicalReg("rax"));
		cur.asms.add(setFlag);
		// extend rax
		Mov ext = new Mov(GetPReg("rax"), GetPReg("rax"), cur);
		ext.extend = true;
		cur.asms.add(ext);
	}
	
	public void visit (Unary quad) {
		Oprt.Op uniOp = null;
		switch (quad.op) {
			case NEG: uniOp = NEG; break;
			case BITNOT: uniOp = BIT_NOT; break;
			default:
				assert false;
		}
		cur.asms.add(new Mov (GetAsmReg(quad.ans), GetAsmReg(quad.src), cur));
		cur.asms.add(new Oprt(GetAsmReg(quad.ans), cur, uniOp));
	}

	// FIXME : we haven't cared about caller-save and callee-save registers.
	public void visit (ir.quad.Call quad) {
		// follow the calling convention to put the args into suitable registers.
		int i;
		for (i = 0; i < 6 && i < quad.args.size(); ++i)
			cur.asms.add(new Mov (GetPReg(callArgRegs.get(i)), GetAsmReg(quad.args.get(i)), cur));
		// if there are leftover args, push them onto stack from right to left.
		// also, record the stack argument offset.
		int stackArgsOffset = 0;
		// must subtract rsp before push stack args.
		int subRspPos = cur.asms.size();
		for (int j = quad.args.size() - 1; i < quad.args.size(); ++i, --j) {
			cur.asms.add(new Push (GetAsmReg(quad.args.get(i)), cur));
			// each push goes in 8 bytes
			stackArgsOffset += 8;
		}
		// add another 8 stackArgOffset for alignment. Assume we already make 16 alignment in prologue.
		// NOTE : we have to subtract rsp before pushing, otherwise the callee cannot get args on stack.
		if (stackArgsOffset % 16 != 0) {
			cur.asms.add(subRspPos, new Oprt (GetPReg("rsp"), new Imm(8), cur, SUB));
			stackArgsOffset += 8;
		} else {
			// NOTE : for clarity.
			cur.asms.add(subRspPos, new Oprt (GetPReg("rsp"), new Imm(0), cur, SUB));
		}
		// if is built-in function, do translation.
		String asmFunctName = (quad.funcName.startsWith("~")) ? BuiltinRenamer(quad.funcName) : quad.funcName;
		cur.asms.add(new nasm.asm.Call(cur, asmFunctName));
		// add rsp for reset.
		cur.asms.add(new Oprt (GetPReg("rsp"), new Imm(stackArgsOffset), cur, ADD));
		
		// move rax result back into the return register.
		if (!quad.ret.name.equals("@null"))
			cur.asms.add(new Mov (GetAsmReg(quad.ret), GetPReg("rax"), cur));
	}
	
	// FIXME : cmp cannot for mem to mem.
	public void visit (Branch quad) {
		// test cond's truth value
		cur.asms.add(new Cmp(GetAsmReg(quad.cond), new Imm(0), cur));
		// jump if cond isn't 0.
		cur.asms.add(new Jmp(cur, Jmp.JmpOption.JNE, BasicBlockRenamer(quad.ifTrue)));
		// else jump to ifFalse.
		cur.asms.add(new Jmp(cur, Jmp.JmpOption.JMP, BasicBlockRenamer(quad.ifFalse)));
	}
	
	public void visit (Jump quad) {
		cur.asms.add(new Jmp(cur, Jmp.JmpOption.JMP, BasicBlockRenamer(quad.target)));
	}
	
	// FIXME : need to care about null ret value problem.
	public void visit (ir.quad.Ret quad) {
		if (quad.val != null)
			cur.asms.add(new Mov (GetPReg("rax"), GetAsmReg(quad.val), cur));
		AddEpilogue();
	}
	
	public void visit (Alloca quad) {
		cur.parentFunct.stackVars.put(quad.var.name, null);
	}
	
	public void visit (Store quad) {
		cur.asms.add(new Mov (new StackMem(quad.dst.name), GetAsmReg(quad.src), cur));
	}
	
	public void visit (Load quad) {
		cur.asms.add(new Mov (GetAsmReg(quad.val), GetAsmReg(quad.addr), cur));
	}
	
	public void visit (Malloc quad) {
		cur.asms.add(new Mov (GetPReg("rdi"), GetAsmReg(quad.size_), cur));
		cur.asms.add(new Call(cur, "malloc"));
		cur.asms.add(new Mov (GetAsmReg(quad.memAddr), GetPReg("rax"), cur));
	}
	
	public void visit (Comment quad) {
		System.err.println(quad.content);
	}
	
	/** Quads that shouldn't appear have already assertion failure in their accept method. */
	
	/*************** Utility ****************/
	public void AddEpilogue () {
		// assign rsp's value back to rbp.
		cur.asms.add(new Mov (GetPReg("rsp"), GetPReg("rbp"), cur));
		// pop previous rbp (on stack) to rbp register.
		cur.asms.add(new Pop(GetPReg("rbp"), cur));
		// ret
		cur.asms.add(new Ret(cur));
	}
	
	/** a comprehensive translator from ir-reg/constant/stringLiteral to the corresponding data structure in nasm,
	 * renaming is also implemented. */
	public AsmReg GetAsmReg (IrValue irReg) {
		if (irReg instanceof Constant)
			return new Imm(((Constant) irReg).GetConstant());
		if (irReg instanceof StringLiteral) {
			GlobalMem str = new GlobalMem(StringLiteralRenamer(((StringLiteral) irReg).name));
			str.isString = true;
			return str;
		}
		assert irReg instanceof Reg;
		if (((Reg) irReg).name.startsWith("@"))
			return new GlobalMem(GlobalRenamer(((Reg) irReg).name));
		
		// then, for temp register and allocated register `logi. because incomplete ssa destruction.
		// since virtual registers are about to be allocated, it doesn't matter.
		if (((Reg) irReg).name.startsWith("!$"))
			return new VirtualReg(irReg.getText());
		
		// since stackMem will be shadowed by offset, name doesn't matter.
		assert cur.parentFunct.stackVars.containsKey(((Reg) irReg).name);
		return new StackMem(((Reg) irReg).name);
	}
	
	public static AsmReg GetPReg (String phyName) {
		return new PhysicalReg(phyName);
	}
	
	int cnt = 0;
	private VirtualReg GetTmpReg () {
		return new VirtualReg("asm" + cnt++);
	}
	
	// turn *0 into __0
	public static String StringLiteralRenamer (String strId) {
		return "_S_" + strId.substring(1);
	}
	
	// rename basic block's name to be AsmBB, attach parentFunction to it.
	// and again, rename Basic block name in jump.
	public static String BasicBlockRenamer (BasicBlock bb) {
		return String.format("_B_%s_%s", bb.name.substring(1), bb.parentFunct.name);
	}
	
	// change @a to _G_a.
	public static String GlobalRenamer (String gName) {
		return String.format("_G_%s", gName.substring(1));
	}
	
	private static Map<String, String> builtin2Extern = new HashMap<>();
	{
		builtin2Extern.put("~print", "printf");
		builtin2Extern.put("~println", "put");
		builtin2Extern.put("~getString", "getString");
		builtin2Extern.put("~getInt", "getInt");
		builtin2Extern.put("~toString", "toString");
		builtin2Extern.put("~-string#length", "_stringLength");
		builtin2Extern.put("~-string#substring", "_stringSubstring");
		builtin2Extern.put("~-string#parseInt", "_stringParseInt");
		builtin2Extern.put("~-string#ord", "_stringOrd");
		builtin2Extern.put("~-string#add", "_stringAdd");
		builtin2Extern.put("~--array#size", "_arraySize");
		builtin2Extern.put("~-string#lt", "_stringEq");
		builtin2Extern.put("~-string#gt", "_stringNeq");
		builtin2Extern.put("~-string#le", "_stringLt");
		builtin2Extern.put("~-string#ge", "_stringGt");
		builtin2Extern.put("~-string#eq", "_stringLe");
		builtin2Extern.put("~-string#ne", "_stringGe");
	}
	public static String BuiltinRenamer (String fName) {
		assert builtin2Extern.containsKey(fName);
		return builtin2Extern.get(fName);
	}
}
