package nasm;

import nasm.asm.Asm;
import nasm.reg.AsmReg;
import nasm.reg.StackMem;
import nasm.reg.VirtualReg;

import java.util.HashMap;
import java.util.Map;

/**
 * Note that register allocator serves the entire determination of which register goes to mem,
 * including the stack argument passing in stack frame.
 *
 * This temp allocator glues separate registers together, which may be inconsistent.
 * It spills virtual registers by creating StackMem (put it on stack), and mov mem mem is handled
 * by offsetBackFill.
 * */
public class AsmRegAllocator {
	/** map spilled local variable to stack. */
	private Map<String, StackMem> stackSpilled = new HashMap<>();
	/** args locates on stack, i.e. ebp + 8i */
//	private Map<String, StackMem> stackArgs = new HashMap<>();
	
	/** Allocate registers for nasm assembly, and record all preeped on stack variables. */
	public void AllocateRegister(AsmFunct asmFunct) {
		stackSpilled.clear();
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
		// this may be allocated local mem.
		if (asmReg instanceof StackMem) {
			if (!stackSpilled.containsKey(((StackMem) asmReg).varHintName)) {
				stackSpilled.put(((StackMem) asmReg).varHintName, (StackMem) asmReg);
				asmFunct.stackVars.add((StackMem) asmReg);
			}
			return stackSpilled.get(((StackMem) asmReg).varHintName);
		}
		
		// put virtual register into mem.
		else if (asmReg instanceof VirtualReg) {
			String name = ((VirtualReg) asmReg).hintName;
			if (!stackSpilled.containsKey(name))
				stackSpilled.put(name, new StackMem(name));
			asmFunct.stackVars.add(stackSpilled.get(name));
			return stackSpilled.get(name);
		}
		return asmReg;
	}
}
