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
	// stackVars records each stack variable's position in stack.
	// record variable names that is assigned to stack memory.
	public Map<String, Integer> stackVars = new HashMap<>();
	private Integer rspOffset = null;
	
	public AsmFunct(String name, List<Reg> regArgs) {
		this.name = name;
		regArgs.forEach(x -> argNames.add(x.name));
	}
	
	// associate absolute args stack address with local args.
	public void MovAllocateArgs () {
		// assign physical regs to local temp registers.
		AsmBB head = bbs.get(0);
		for (int i = 0; i < 6 && i < argNames.size(); ++i)
			head.asms.add(new Mov(new VirtualReg(argNames.get(i)), new PhysicalReg(callArgRegs.get(i)), head));
		// decide to put it into stackMem, but need to replace virtual register by stackMem.
		// these args have no alloca, and thus their types are still VRegs.
		int cnt = 0;
		for (int i = 6; i < argNames.size(); ++i) {
			stackVars.put(argNames.get(i), - 8 * (++cnt) - 8);
		}
	}
	
	/**
	 * Change asm code based on the result of register allocation, conduct the modifications
	 * required by the constraint imposed on register and memory assignment. */
//	public void RewriteBackfill() {
//		// assign stack offset to each stack variable.
//		rspOffset = 0;
//		for (StackMem var : stackVars)
//			if (var.ebpOffset == null)
//				var.ebpOffset = (rspOffset += 8);
//		// change asm when register allocation gives invalid operations.
//		for (AsmBB bb : bbs) {
//			for (int i = 0; i < bb.asms.size(); ++i) {
////				if (bb.asms.get(i) instanceof Mov) {
////					Mov mov = (Mov) bb.asms.get(i);
////					if ((mov.dst instanceof StackMem || mov.dst instanceof GlobalMem) &&
////									(mov.src instanceof StackMem || mov.src instanceof GlobalMem)) {
////						 create a tmpMov as transactor.
////						Mov tmpMov = new Mov (bb);
////						tmpMov.dst = mov.dst;
////						mov.dst = new PhysicalReg("rax");
////						tmpMov.src = new PhysicalReg("rax");
////						 insert a MOV transact behind original double-mem MOVE
////						bb.asms.add(i + 1, tmpMov);
////						++i;
////					}
//				Asm asm = bb.asms.get(i);
//				// memory mov is not OK.
//				if (asm instanceof Mov || asm instanceof Oprt) {
////				if (asm instanceof Oprt) {
//					//					Mov mov = (Mov) bb.asms.get(i);
//					if ((asm.dst instanceof StackMem || asm.dst instanceof GlobalMem) &&
//									(asm.src instanceof StackMem || asm.src instanceof GlobalMem)) {
////						 create a tmpMov as transactor.
//						Mov tmpMov = new Mov (bb);
//						tmpMov.dst = asm.dst;
//						asm.dst = new PhysicalReg("rax");
//						tmpMov.src = new PhysicalReg("rax");
////						 insert a MOV transact behind original double-mem MOVE
//						bb.asms.add(i + 1, tmpMov);
//						++i;
//					}
//				}
//				else if (asm instanceof Cmp) {
//					if ((asm.dst instanceof StackMem || asm.dst instanceof GlobalMem) &&
//									(asm.src instanceof StackMem || asm.src instanceof GlobalMem)) {
//						Mov tmpMov = new Mov (bb);
//						tmpMov.src = asm.src;
//						tmpMov.dst = new PhysicalReg("rax");
//						asm.src = new PhysicalReg("rax");
//						bb.asms.add(i, tmpMov);
//						++i;
//					}
//				}
				// push a mem is not OK.
//				else if (asm instanceof Push) {
//					if (asm.src instanceof StackMem) {
//						Mov tmpMov = new Mov (bb);
//						tmpMov.dst = new PhysicalReg("rax");
//						tmpMov.src = asm.src;
//						asm.src = new PhysicalReg("rax");
//						bb.asms.add(i, tmpMov);
//						++i;
//					}
//				}
//			}
//		}
//	}

	public void AddPrologue () {
		AsmBB head = bbs.get(0);
		// push rbp to stack
		// push rbp.
		head.asms.add(0, new Push (GetPReg("rbp"), head));
		// rbp = rsp. make rsp our new rbp
		// mov rbp rsp.
		head.asms.add(1, new Mov (GetPReg("rbp"), GetPReg("rsp"), head));
		// align stack frame by adding offset divisible by 16.
		if (rspOffset != null) {
			// after register allocation, and backfill rewriting, rspOffset shouldn't be null
			rspOffset = (rspOffset + 15) / 16 * 16;
			head.asms.add(2, new Oprt(GetPReg("rsp"), new Imm(rspOffset), head, Oprt.Op.SUB));
		}
		else {
			// to print nasm before register allocation.
			head.asms.add(2, new Oprt(GetPReg("rsp"), GetPReg("not allocated rsp offset"), head, Oprt.Op.SUB));
		}
	}
}
