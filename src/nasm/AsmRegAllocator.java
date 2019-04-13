package nasm;

import nasm.asm.Asm;
import nasm.reg.*;

/**
 * Note that register allocator serves the entire determination of which register goes to mem,
 * including the stack argument passing in stack frame.
 *
 * This temp allocator glues separate registers together, which may be inconsistent.
 * It spills virtual registers by creating StackMem (put it on stack), and mov mem mem is handled
 * by offsetBackFill.
 * */
public class AsmRegAllocator {
	/** Allocate registers for nasm assembly, and record all preeped on stack variables. */
	public void AllocateRegister(AsmFunct asmFunct) {
		for (AsmBB bb : asmFunct.bbs)
			for (Asm asm : bb.asms) {
				asm.dst = AllocaAsmReg(asmFunct, asm.dst);
				asm.src = AllocaAsmReg(asmFunct, asm.src);
			}
	}
	
	/** record stack var information, return the allocated StackMem.
	 * if decide to make it a stackMem, replace virtual register by stackmem,
	 * and put stackMem function's stackVars for later refill. */
	private AsmReg AllocaAsmReg (AsmFunct asmFunct, AsmReg asmReg) {
		// no need to allocate.
		if (asmReg == null || asmReg instanceof Imm || asmReg instanceof GlobalMem || asmReg instanceof PhysicalReg)
			return asmReg;
		// has been allocated.
		if (asmReg instanceof StackMem) {
			assert asmFunct.stackVars.containsKey(((StackMem) asmReg).varHintName);
			return asmReg;
		}
		// allocate vReg to stackMem.
		assert asmReg instanceof VirtualReg;
		String name = ((VirtualReg) asmReg).hintName;
		asmFunct.stackVars.put(name, null);
		return new StackMem(name);
	}
}
