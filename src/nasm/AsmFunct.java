package nasm;

import ir.structure.Reg;
import nasm.asm.*;
import nasm.reg.*;

import java.util.*;

import static nasm.AsmTranslateVisitor.GetPReg;
import static nasm.AsmTranslateVisitor.callArgRegs;

/**
 * Need to maintain CFG structure for liveliness analysis.
 * */
public class AsmFunct {
	public String name;
	public List<AsmBB> bbs = new LinkedList<>();
	public AsmCFG cfg = new AsmCFG();
	public List<String> argNames = new LinkedList<>();
	/** stackVars records variables locating on stack in current function, it is filled by register allocator. */
	// set by register allocator.
	public Integer stackLocalOffset = null;
	
	public AsmFunct(String name, List<Reg> regArgs) {
		this.name = name;
		regArgs.forEach(x -> argNames.add(x.name));
	}
	
	// associate absolute args stack address with local args.
	public void MovAllocateArgs () {
		// assign physical regs to local temp registers.
		AsmBB head = bbs.get(0);
		for (int i = 0; i < 6 && i < argNames.size(); ++i)
			head.asms.add(0, new Mov(new VirtualReg(argNames.get(i)), new PhysicalReg(callArgRegs.get(i)), head));
		
		int cnt = 0;
		for (int i = 6; i < argNames.size(); ++i) {
			StackMem laterArg = new StackMem(head.parentFunct.name + "_arg" + Integer.toString(i));
			laterArg.ebpOffset = - 8 * (++cnt) - 8;
			head.asms.add(0, new Mov (new VirtualReg(argNames.get(i)), laterArg, head));
		}
	}
	
	/** Change asm code based on the result of register allocation, conduct the modifications
	 * required by the constraint imposed on register and memory assignment. */
	public void Backfill() {
		// change asm when register allocation gives invalid operations.
		for (AsmBB bb : bbs) {
			for (int i = 0; i < bb.asms.size(); ++i) {
				Asm asm = bb.asms.get(i);
				// memory mov is not OK.
				if (asm instanceof Mov || asm instanceof Oprt) {
					if ((asm.dst instanceof StackMem || asm.dst instanceof GlobalMem) &&
									(asm.src instanceof StackMem || asm.src instanceof GlobalMem)) {
//						 create a tmpMov as transactor.
//						 insert a MOV transact behind original double-mem MOVE
						bb.asms.add(i + 1, new Mov (asm.dst, new PhysicalReg("rax"), bb));
						asm.dst = new PhysicalReg("rax");
						++i;
					}
				}
				else if (asm instanceof Cmp) {
					if ((asm.dst instanceof StackMem || asm.dst instanceof GlobalMem) &&
									(asm.src instanceof StackMem || asm.src instanceof GlobalMem)) {
						bb.asms.add(i, new Mov (new PhysicalReg("rax"), asm.src, bb));
						asm.src = new PhysicalReg("rax");
						++i;
					}
				}
//		 push a mem is not OK.
				else if (asm instanceof Push) {
					if (asm.src instanceof StackMem) {
						bb.asms.add(i, new Mov (new PhysicalReg("rax"), asm.src, bb));
						asm.src = new PhysicalReg("rax");
						++i;
					}
				}
			}
		}
	}

	public void AddPrologue () {
		AsmBB head = bbs.get(0);
		// push rbp to stack
		// push rbp.
		head.asms.add(0, new Push (GetPReg("rbp"), head));
		// rbp = rsp. make rsp our new rbp
		// mov rbp rsp.
		head.asms.add(1, new Mov (GetPReg("rbp"), GetPReg("rsp"), head));
		// align stack frame by adding offset divisible by 16.
		if (stackLocalOffset != null) {
			// after register allocation, and backfill rewriting, rspOffset shouldn't be null
			stackLocalOffset = (stackLocalOffset + 15) / 16 * 16;
			head.asms.add(2, new Oprt(GetPReg("rsp"), new Imm(stackLocalOffset), head, Oprt.Op.SUB));
		}
		else {
			// to print nasm before register allocation.
			head.asms.add(2, new Oprt(GetPReg("rsp"), GetPReg("not allocated rsp offset"), head, Oprt.Op.SUB));
		}
	}
}
