package nasm;

import ir.structure.Reg;
import nasm.asm.*;
import nasm.reg.*;

import java.util.*;

/**
 * Need to maintain CFG structure for liveliness analysis.
 * */
public class AsmFunct {
	public String name;
	public List<AsmBB> bbs = new LinkedList<>();
	public AsmCFG cfg = new AsmCFG();
	public List<String> argNames = new LinkedList<>();
	/** stackVars records variables locating on stack in current function, it is filled by register allocator. */
	// use this list to assign offset.
	public List<StackMem> stackVars = new LinkedList<>();
	private Integer rspOffset = null;
	
	public AsmFunct(String name, List<Reg> regArgs) {
		this.name = name;
		regArgs.forEach(x -> argNames.add(x.name));
	}
	
	// associate absolute args stack address with local args.
	public void HandleArgs () {
		// assign physical regs to local temp registers.
		for (int i = 0; i < 6 && i < argNames.size(); ++i) {
			AsmBB head = bbs.get(0);
			Mov regArgPass = new Mov (head);
			regArgPass.dst = new VirtualReg(argNames.get(i));
			regArgPass.src = new PhysicalReg(AsmTranslateVisitor.callArgRegs.get(i));
			head.asms.add(i, regArgPass);
		}
		// decide to put it into stackMem, but need to replace virtual register by stackMem.
		int cnt = 0;
		for (int i = 6; i < argNames.size(); ++i) {
			StackMem memArgPass = new StackMem (argNames.get(i));
			stackVars.add(memArgPass);
			// note here is the empirical value. we are in 64-bit machine.
			memArgPass.ebpOffset = - 8 * (++cnt) - 8;
			// replace vReg by stackMem
			for (AsmBB bb : bbs) {
				for (Asm asm : bb.asms) {
					if (asm.dst instanceof VirtualReg && ((VirtualReg) asm.dst).hintName.equals(memArgPass.varHintName))
						asm.dst = memArgPass;
					if (asm.src instanceof VirtualReg && ((VirtualReg) asm.src).hintName.equals(memArgPass.varHintName))
						asm.src = memArgPass;
				}
			}
		}
	}
	
	/**
	 * Change asm code based on the result of register allocation, conduct the modifications
	 * required by the constraint imposed on register and memory assignment. */
	public void RewriteBackfill() {
		// assign stack offset to each stack variable.
		rspOffset = 0;
		for (StackMem var : stackVars)
			if (var.ebpOffset == null)
				var.ebpOffset = (rspOffset += 8);
		// change asm when register allocation gives invalid operations.
		for (AsmBB bb : bbs) {
			for (int i = 0; i < bb.asms.size(); ++i) {
//				if (bb.asms.get(i) instanceof Mov) {
//					Mov mov = (Mov) bb.asms.get(i);
//					if ((mov.dst instanceof StackMem || mov.dst instanceof GlobalMem) &&
//									(mov.src instanceof StackMem || mov.src instanceof GlobalMem)) {
//						 create a tmpMov as transactor.
//						Mov tmpMov = new Mov (bb);
//						tmpMov.dst = mov.dst;
//						mov.dst = new PhysicalReg("rax");
//						tmpMov.src = new PhysicalReg("rax");
//						 insert a MOV transact behind original double-mem MOVE
//						bb.asms.add(i + 1, tmpMov);
//						++i;
//					}
				Asm asm = bb.asms.get(i);
				// memory mov is not OK.
				if (asm instanceof Mov || asm instanceof Binary) {
//				if (asm instanceof Binary) {
					//					Mov mov = (Mov) bb.asms.get(i);
					if ((asm.dst instanceof StackMem || asm.dst instanceof GlobalMem) &&
									(asm.src instanceof StackMem || asm.src instanceof GlobalMem)) {
//						 create a tmpMov as transactor.
						Mov tmpMov = new Mov (bb);
						tmpMov.dst = asm.dst;
						asm.dst = new PhysicalReg("rax");
						tmpMov.src = new PhysicalReg("rax");
//						 insert a MOV transact behind original double-mem MOVE
						bb.asms.add(i + 1, tmpMov);
						++i;
					}
				}
				else if (asm instanceof Cmp) {
					if ((asm.dst instanceof StackMem || asm.dst instanceof GlobalMem) &&
									(asm.src instanceof StackMem || asm.src instanceof GlobalMem)) {
						Mov tmpMov = new Mov (bb);
						tmpMov.src = asm.src;
						tmpMov.dst = new PhysicalReg("rax");
						asm.src = new PhysicalReg("rax");
						bb.asms.add(i, tmpMov);
						++i;
					}
				}
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
			}
		}
	}
	
	public void AddPrologue () {
		AsmBB head = bbs.get(0);
		// push rbp to stack
		// push rbp.
		Push push = new Push (head);
		push.src = new PhysicalReg("rbp");
		head.asms.add(0, push);
		// rbp = rsp. make rsp our new rbp
		// mov rbp rsp.
		Mov rsp2rbp = new Mov (head);
		rsp2rbp.dst = new PhysicalReg("rbp");
		rsp2rbp.src = new PhysicalReg("rsp");
		head.asms.add(1, rsp2rbp);
		
		// align stack frame by adding offset divisible by 16.
		assert rspOffset != null;
		rspOffset = (rspOffset + 15) / 16 * 16;
		Binary sub = new Binary (head);
		sub.op = Binary.Op.SUB;
		sub.dst = new PhysicalReg("rsp");
		sub.src = new Imm (rspOffset);
		head.asms.add(2, sub);
	}
	
	public void AddEpilogue () {
		// assign rsp's value back to rbp.
		AsmBB tail = bbs.get(bbs.size() - 1);
		Mov rbp2rsp = new Mov (tail);
		rbp2rsp.dst = new PhysicalReg("rsp");
		rbp2rsp.src = new PhysicalReg("rbp");
		tail.asms.add(rbp2rsp);
		// pop previous rbp (on stack) to rbp register.
		Pop pop = new Pop (tail);
		pop.dst = new PhysicalReg("rbp");
		tail.asms.add(pop);
		// ret
		Ret ret = new Ret (tail);
		tail.asms.add(ret);
	}
	
}
