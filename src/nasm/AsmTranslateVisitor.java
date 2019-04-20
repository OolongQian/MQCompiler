package nasm;


import ir.quad.*;
import ir.quad.Load;
import ir.quad.Store;
import ir.structure.*;
import nasm.inst.*;
import nasm.inst.Call;
import nasm.inst.Mov;
import nasm.inst.Ret;
import nasm.reg.*;
import nasm.reg.Reg;

import java.util.*;

import static config.Config.CALLEESAVE;
import static config.Config.CALLERSAVE;
import static ir.quad.Binary.Op.DIV;
import static ir.quad.Binary.Op.SHL;
import static nasm.Utils.*;
import static nasm.inst.Oprt.Op.*;
import static nasm.reg.PhysicalReg.PhyRegType.*;

/**
 * Note : all registers are separate in current implementation.
 * */
public class AsmTranslateVisitor {
	
	/** current AsmBB handler */
	private AsmBB cur;
	private AsmFunct curf;
	
	// if allocated, make it stackMem.
//	private Set<String> allocated = new HashSet<>();
	
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
	
	public void ConfigAsmFunct (AsmFunct funct) {
		curf = funct;
	}

	/******************** translation starter and utility *********************/
	/** need flexibility to get next quad or skip one quad. */
	public void TranslateQuadList (List<Quad> quads) {
//		allocated.clear();
		quads.forEach(x -> x.AcceptTranslator(this));
	}
	
	/******************* concrete visit actions ***********************/
	private void visit (Quad quad) {
		quad.AcceptTranslator(this);
	}
	
	/** move can be mem to mem */
	public void visit (ir.quad.Mov quad) {
		Mov mov = new Mov (GetAsmReg(quad.dst), GetAsmReg(quad.src), cur);
		cur.insts.add(mov);
	}

	// FIXME : bin cannot mem to mem.
	public void visit (Binary quad) {
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
				cur.insts.add(new Mov (GetAsmReg(quad.ans), GetAsmReg(quad.src1), cur));
				cur.insts.add(new Oprt (GetAsmReg(quad.ans), GetAsmReg(quad.src2), cur, arithOp));
				break;
		}
	}
	
	// these registers are fixed.
	private void TransDivMod (Binary quad) {
		// rax is dividend,
		cur.insts.add(new Mov(GetPReg(rax), GetAsmReg(quad.src1), cur));
		// rdx is cleared by cqo.
//		cur.insts.add(new Special("cqo", cur));
		cur.insts.add(new Mov (GetPReg(rdx), new Imm(0), cur));
		// issue idiv divider, to conduct idiv.
		cur.insts.add(new Oprt(null, GetAsmReg(quad.src2), cur, Oprt.Op.IDIV));
		// FIXME : separate assign for isDiv is ugly.
		((Oprt) cur.insts.get(cur.insts.size() - 1)).isDiv = quad.op == DIV;
		// quotient is stored in rax, remainder is in rdx
		cur.insts.add(new Mov(GetAsmReg(quad.ans),
						(quad.op == DIV) ? GetPReg(rax) : GetPReg(rdx),
						cur));
	}
	
	//
	private void TransShift (Binary quad) {
		// *** : shift value must be in rcx's cl or constant.
		// move src1 to dst
		cur.insts.add(new Mov (GetAsmReg(quad.ans), GetAsmReg(quad.src1), cur));
		// shift ans
		Oprt.Op shiftOp = (quad.op == SHL) ? Oprt.Op.SHL : Oprt.Op.SAR;
		if (quad.src2 instanceof Constant)
			// shift ans directly by constant
			cur.insts.add(new Oprt(GetAsmReg(quad.ans), GetAsmReg(quad.src2), cur, shiftOp));
		else {
			// shift ans by put shift amount into rcx.
			cur.insts.add(new Mov (GetPReg(rcx), GetAsmReg(quad.src2), cur));
			cur.insts.add(new Oprt(GetAsmReg(quad.ans), GetPReg("cl", rcx), cur, shiftOp));
		}
	}
	
	private void TransCmp (Binary quad) {
		// FIXME : cmp inst is strange, promote them into registers consistently.
		AsmReg src1 = GetAsmReg(quad.src1);
		if (!(src1 instanceof Reg)) {
			Reg tmp1 = GetTmpReg();
			cur.insts.add(new Mov (tmp1, src1, cur));
			src1 = tmp1;
		}
		AsmReg src2 = GetAsmReg(quad.src2);
		// NOTE : src2 can be immediate.
		
		// FIXME : set cmp.flagReg rax temporarily.
		Cmp cmp = new Cmp(src1, src2, quad.op, cur);
		cmp.flagReg = (Reg) GetPReg(rax);
		cur.insts.add(cmp);
		// move compared flag reg to ans. Maybe coalesce.
		cur.insts.add (new Mov (GetAsmReg(quad.ans), cmp.flagReg, cur));
	}
	
	public void visit (Unary quad) {
		Oprt.Op uniOp = null;
		switch (quad.op) {
			case NEG: uniOp = NEG; break;
			case BITNOT: uniOp = BIT_NOT; break;
			default:
				assert false;
		}
		cur.insts.add(new Mov (GetAsmReg(quad.ans), GetAsmReg(quad.src), cur));
		cur.insts.add(new Oprt(GetAsmReg(quad.ans), cur, uniOp));
	}

	// FIXME : we haven't cared about caller-save and callee-save registers.
	public void visit (ir.quad.Call quad) {
		// follow the calling convention to put the args into suitable registers.
		int subRspPos = cur.insts.size();
		Say(cur, "BEGIN args pass\n");
		int i;
		for (i = 0; i < 6 && i < quad.args.size(); ++i)
			cur.insts.add(new Mov (GetPReg(PhysicalReg.regArgsPass.get(i)), GetAsmReg(quad.args.get(i)), cur));
		// if there are leftover args, push them onto stack from right to left.
		// also, record the stack argument offset.
		int stackArgsOffset = 0;
		// must subtract rsp before push stack args.
		for (int j = quad.args.size() - 1; i < quad.args.size(); ++i, --j) {
			cur.insts.add(new Push (GetAsmReg(quad.args.get(i)), cur));
			// each push goes in 8 bytes
			stackArgsOffset += 8;
		}
		Say(cur, "END args pass\n");
		
		// add another 8 stackArgOffset for alignment. Assume we already make 16 alignment in prologue.
		// NOTE : we have to subtract rsp before pushing, otherwise the callee cannot get args on stack.
		if (stackArgsOffset % 16 != 0) {
			cur.insts.add(subRspPos, new Oprt (GetPReg(rsp), new Imm(8), cur, SUB));
			stackArgsOffset += 8;
		} else {
			// NOTE : for clarity.
			cur.insts.add(subRspPos, new Oprt (GetPReg(rsp), new Imm(0), cur, SUB));
		}
		// if is built-in function, do translation.
		String asmFunctName = (quad.funcName.startsWith("~")) ? BuiltinRenamer(quad.funcName) : quad.funcName;
		cur.insts.add(new Call(cur, asmFunctName, !quad.ret.name.equals("@null")));
		// add rsp for reset.
		cur.insts.add(new Oprt (GetPReg(rsp), new Imm(stackArgsOffset), cur, ADD));
		
		// move rax result back into the return register.
		if (!quad.ret.name.equals("@null"))
			cur.insts.add(new Mov (GetAsmReg(quad.ret), GetPReg(rax), cur));
	}
	
	public void visit (Branch quad) {
		// test cond's truth value
		// cannot cmp imm directly.
		AsmReg asmCond = GetAsmReg(quad.cond);
		if (asmCond instanceof Imm) {
			Reg tmpCond = GetTmpReg();
			cur.insts.add(new Mov (tmpCond, asmCond, cur));
			asmCond = tmpCond;
		}
		cur.insts.add(new Cmp(asmCond, new Imm(0), cur));
		// jump if cond isn't 0.
		cur.insts.add(new Jmp(cur, Jmp.JmpOption.JNE, BasicBlockRenamer(quad.ifTrue)));
		// else jump to ifFalse.
		cur.insts.add(new Jmp(cur, Jmp.JmpOption.JMP, BasicBlockRenamer(quad.ifFalse)));
	}
	
	public void visit (Jump quad) {
		cur.insts.add(new Jmp(cur, Jmp.JmpOption.JMP, BasicBlockRenamer(quad.target)));
	}
	
	// FIXME : need to care about null ret value problem.
	public void visit (ir.quad.Ret quad) {
		if (quad.val != null && !quad.val.getText().equals("@null"))
			cur.insts.add(new Mov (GetPReg(rax), GetAsmReg(quad.val), cur));
		// add a temporary placeholder here.
		// back fill ret epilogue later.
		cur.insts.add(new Ret(cur));
	}
	
	// load effective stack address to allocated virtual register.
	int alloc_num = 0;
	public void visit (Alloca quad) {
//		allocated.add(quad.var.name);
		cur.insts.add(new Lea(GetAsmReg(quad.var), new StackMem(String.format("%s_alloca_%s", curf.name, Integer.toString(++alloc_num))), cur));
	}
	
	public void visit (Store quad) {
		// we need to dererence.
		cur.insts.add(new nasm.inst.Store(GetAsmReg(quad.dst), GetAsmReg(quad.src), cur));
	}
	
	public void visit (Load quad) {
		cur.insts.add(new nasm.inst.Load(GetAsmReg(quad.val), GetAsmReg(quad.addr), cur));
	}
	
	// this seems to be a redundant quad. can be replaced by a call for 'malloc'.
	public void visit (Malloc quad) {
		List<IrValue> args = new LinkedList<>(); args.add(quad.size_);
		ir.quad.Call malloc = new ir.quad.Call("malloc", quad.memAddr, args);
		visit (malloc);
	}
	
	public void visit (Comment quad) {
		System.err.println(quad.content);
	}
	
	/** Quads that shouldn't appear have already assertion failure in their accept method. */
	
	/*************** Utility ****************/
	public void AddEpilogue () {
		for (AsmBB bb : curf.bbs) {
			for (int i = 0; i < bb.insts.size(); ++i) {
				if (bb.insts.get(i) instanceof Ret) {
					Say (bb, i++, "BEGIN epilogue\n");

					if (CALLEESAVE) {
						Say (bb, i++, "BEGIN restore callee save\n");
						// restore callee-save registers.
						for (int j = PhysicalReg.calleeSave.size() - 1; j >= 0; --j)
							bb.insts.add(i++, new Pop (GetPReg(PhysicalReg.calleeSave.get(j)), bb));
						Say (bb, i++, "END restore callee save\n");
					}
					
					if (curf.stackLocalOffset != null) {
						// align stack frame by adding offset divisible by 16.
						assert curf.stackLocalOffset % 16 == 0;
						bb.insts.add(i++, new Oprt(GetPReg(rsp), new Imm(curf.stackLocalOffset), bb, Oprt.Op.ADD));
					} else {
						// to print nasm before register allocation.
						bb.insts.add(i++, new Oprt(GetPReg(rsp), GetPReg("not allocated rsp offset", dummy), bb, Oprt.Op.ADD));
					}
					
					// assign rsp's value back to rbp.
					bb.insts.add(i++, new Mov (GetPReg(rsp), GetPReg(rbp), bb));
					// pop previous rbp (on stack) to rbp register.
					bb.insts.add(i++, new Pop(GetPReg(rbp), bb));
					// ret has already been there like a sentinel
//					bb.insts.add(new Ret(bb));
					Say (bb, i++, "END epilogue\n");
				}
			}
		}
		
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
		assert irReg instanceof ir.structure.Reg;
		if (((ir.structure.Reg) irReg).name.startsWith("@"))
			return new GlobalMem(GlobalRenamer(((ir.structure.Reg) irReg).name));
		
		// return the register with effective address of assigned stackMem.
//		if (allocated.contains(((ir.structure.Reg) irReg).name))
//			return new Reg(((ir.structure.Reg) irReg).name);
		
		// then, for temp register and allocated register `logi. because incomplete ssa destruction.
		// since virtual registers are about to be allocated, it doesn't matter.
		return new Reg(((ir.structure.Reg) irReg).name);
	}
	

	private Reg GetTmpReg () {
		return cur.parentFunct.GetTmpReg();
	}
	
	
	
	/**************************** Function args passing, prologue... *******************************/
	// associate absolute args stack address with local args.
	public void ArgsVirtualize () {
		// assign physical regs to local temp registers.
		AsmBB head = curf.bbs.get(0);
		
		// start from 0 because prologue hasn't been added.
		int cnt = 0;

		Say (head, cnt++, "BEGIN args virtual:\n");
		
		for (int i = 0; i < 6 && i < curf.argNames.size(); ++i)
			head.insts.add(cnt++, new Mov(new Reg(curf.argNames.get(i)), GetPReg(PhysicalReg.regArgsPass.get(i)), head));
		
		
		int ofstCnt = 0;
		for (int i = curf.argNames.size() - 1; i >= 6; --i) {
			StackMem laterArg = new StackMem(head.parentFunct.name + "_arg" + Integer.toString(i));
			laterArg.ebpOffset = - 8 * (++ofstCnt) - 8;
			head.insts.add(cnt++, new nasm.inst.Load (new Reg(curf.argNames.get(i)), laterArg, head));
		}
		
		Say (head, cnt++, "END args virtual\n\n");
	}
	
	/** Change inst code based on the result of register allocation, conduct the modifications
	 * required by the constraint imposed on register and memory assignment. */
	public void x86_FormCheck() {
		for (AsmBB bb : curf.bbs) {
			for (int i = 0; i < bb.insts.size(); ++i) {
				
				Inst asm = bb.insts.get(i);
				// rdx <-- 0, rax <-- dividend.
				// why not use rdx to hold divider ?
				if (asm instanceof Oprt && ((Oprt) asm).op == Oprt.Op.IDIV) {
					assert asm.dst == null;
					if (asm.src instanceof Imm) {
						Reg tmpVreg = GetTmpReg();
						bb.insts.add(i, new Mov(tmpVreg, asm.src, bb));
						asm.src = tmpVreg;
						++i;
					}
				}
			}
		}
	}
	
	
	public void CallerCalleeSave() {
		if (CALLERSAVE) {
			for (AsmBB bb : curf.bbs) {
				for (int i = 0; i < bb.insts.size(); ++i) {
					if (!(bb.insts.get(i) instanceof Call)) continue;
					
					int savePos = i;
					while (true) {
						if (bb.insts.get(savePos) instanceof Msg && ((Msg) bb.insts.get(savePos)).msg.equals("BEGIN args pass\n"))
							break;
						savePos--;
					}
					
					Say(bb, savePos++, "BEGIN caller save\n");
					++i;
					// push caller-save registers
					for (int j = 0; j < PhysicalReg.callerSave.size(); ++j) {
						bb.insts.add(savePos++, new Push(GetPReg(PhysicalReg.callerSave.get(j)), cur));
						++i;
					}
					Say(bb, savePos++, "END caller save\n");
					++i;
					
					// skip 'ADD rsp, offset'.
					++i;
					Say(bb, ++i, "BEGIN caller restore\n");
					// pop caller-save registers out.
					for (int j = PhysicalReg.callerSave.size() - 1; j >= 0; --j)
						bb.insts.add(++i, new Pop(GetPReg(PhysicalReg.callerSave.get(j)), cur));
					
					Say(bb, ++i, "END caller restore\n");
				}
			}
		}
		
	}
		/*
		// change inst when register allocation gives invalid operations.
		for (AsmBB bb : curf.bbs) {
			for (int i = 0; i < bb.insts.size(); ++i) {
				Inst asm = bb.insts.get(i);
				// memory mov is not OK.
				if (asm instanceof Mov || asm instanceof Oprt) {
					if ((asm.dst instanceof StackMem || asm.dst instanceof GlobalMem) &&
									(asm.src instanceof StackMem || asm.src instanceof GlobalMem)) {
//						 create a tmpMov as transactor.
//						 insert a MOV transact behind original double-mem MOVE
						bb.insts.add(i + 1, new Mov (asm.dst, GetPReg(rax), bb));
						asm.dst = GetPReg(rax);
						++i;
					}
				}
				
				// fixme : for safety.
				if (asm instanceof nasm.inst.Load) {
					if (asm.dst instanceof StackMem) {
						bb.insts.add(i + 1, new Mov (asm.dst, GetPReg(rax), cur));
						asm.dst = GetPReg(rax);
						++i;
					}
				}
				
				if (asm instanceof nasm.inst.Store) {
					// if src is stackMem, it is mem-to-mem, which is forbidden.
					if (asm.src instanceof StackMem) {
						bb.insts.add(i, new Mov (GetPReg(rax), asm.src, cur));
						asm.src = GetPReg(rax);
						++i;
					}
					// we cannot directly access memory pointed by stackMem content.
					// use register as a proxy.
					// Because the data stored may be carried by register rax, the dst may be assigned to rdx.
					if (asm.dst instanceof StackMem) {
						bb.insts.add(i, new Mov (GetPReg(rdx), asm.dst, cur));
						asm.dst = GetPReg(rdx);
						++i;
					}
				}
				
				// mul's dst cannot be stackMem or Imm, so change it to rax
				if (asm instanceof Oprt && ((Oprt) asm).op == Oprt.Op.IMUL) {
					if (asm.dst instanceof StackMem || asm.dst instanceof Imm) {
						// recall we do this in translating Binary.
						// cur.insts.add(new Mov (GetAsmReg(quad.ans), GetAsmReg(quad.src1), cur));
						// cur.insts.add(new Oprt(GetAsmReg(quad.ans), GetAsmReg(quad.src2), cur, arithOp));
						
						// since rax may be used as multiplier carrier, use rcx as a proxy.
						// rcx usage is unique for this trivial allocator.
						assert bb.insts.get(i-1) instanceof Mov;
						bb.insts.add(i + 1, new Mov (asm.dst, GetPReg(rcx), bb));
						bb.insts.get(i-1).dst = GetPReg(rcx);
						bb.insts.get(i).dst = GetPReg(rcx);
						++i;
					}
				}
				
				// rdx <-- 0, rax <-- dividend.
				// why not use rdx to hold divider ?
				if (asm instanceof Oprt && ((Oprt) asm).op == Oprt.Op.IDIV) {
					assert asm.dst == null;
					if (asm.src instanceof Imm) {
						bb.insts.add(i, new Mov(GetPReg(rdx), asm.src, bb));
						asm.src = GetPReg(rdx);
						++i;
					}
				}
				
				if (asm instanceof Cmp) {
					if ((asm.dst instanceof StackMem || asm.dst instanceof GlobalMem) &&
									(asm.src instanceof StackMem || asm.src instanceof GlobalMem)) {
						bb.insts.add(i, new Mov (GetPReg(rax), asm.src, bb));
						asm.src = GetPReg(rax);
						++i;
					}
				}
//		 push a mem is not OK.
				if (asm instanceof Push) {
					if (asm.src instanceof StackMem) {
						bb.insts.add(i, new Mov (GetPReg(rax), asm.src, bb));
						asm.src = GetPReg(rax);
						++i;
					}
				}
				
				// lea must load effective address into register.
				if (asm instanceof Lea) {
					assert asm.src instanceof StackMem;
					if (asm.dst instanceof StackMem) {
						bb.insts.add(i + 1, new Mov (asm.dst, GetPReg(rax), cur));
						asm.dst = GetPReg(rax);
						++i;
					}
				}
			}
		}
	}
	*/
	
	public void AddPrologue () {
		AsmBB head = curf.bbs.get(0);
		int cnt = 0;

		Say (head, cnt++, "BEGIN prologue\n");
		
		// push rbp to stack
		// push rbp.
		head.insts.add(cnt++, new Push (GetPReg(rbp), head));
		// rbp = rsp. make rsp our new rbp
		// mov rbp rsp.
		head.insts.add(cnt++, new Mov (GetPReg(rbp), GetPReg(rsp), head));
		
		// align stack frame by adding offset divisible by 16.
		if (curf.stackLocalOffset != null) {
			// after register allocation, and backfill rewriting, rspOffset shouldn't be null
			curf.stackLocalOffset = (curf.stackLocalOffset + 15) / 16 * 16;
			head.insts.add(cnt++, new Oprt(GetPReg(rsp), new Imm(curf.stackLocalOffset), head, Oprt.Op.SUB));
		}
		else {
			// to print nasm before register allocation.
			head.insts.add(cnt++, new Oprt(GetPReg(rsp), GetPReg("not allocated rsp offset", dummy), head, Oprt.Op.SUB));
		}
		
		if (CALLEESAVE) {
			// push callee save registers.
			Say (head, cnt++, "BEGIN callee save\n");
			for (int j = 0; j < PhysicalReg.calleeSave.size(); ++j)
				head.insts.add(cnt++, new Push (GetPReg(PhysicalReg.calleeSave.get(j)), head));
			Say (head, cnt++, "END callee save\n");
		}

		Say (head, cnt++, "END prologue\n\n");
	}
}
