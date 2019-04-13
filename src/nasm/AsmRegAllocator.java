package nasm;

import nasm.asm.Asm;
import nasm.reg.*;

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
	/** Allocate registers for nasm assembly, and record all preeped on stack variables. */
	public void AllocateRegister(AsmFunct asmFunct) {
		// decide which virtual regs should be spilled to stackMem.
		for (AsmBB bb : asmFunct.bbs)
			for (Asm asm : bb.asms) {
				if (asm.dst instanceof VirtualReg)
					asm.dst = new StackMem(((VirtualReg) asm.dst).hintName);
				if (asm.src instanceof VirtualReg)
					asm.src = new StackMem(((VirtualReg) asm.src).hintName);
			}
		// assign actual stack positions to stackMem.
		Map<String, Integer> stackPos = new HashMap<>();
		for (AsmBB bb : asmFunct.bbs) {
			for (Asm asm : bb.asms) {
				
				if (asm.dst instanceof StackMem && ((StackMem) asm.dst).ebpOffset == null) {
					StackMem stack = (StackMem) asm.dst;
					if (!stackPos.containsKey(stack.varHintName))
						stackPos.put(stack.varHintName, 8 * (stackPos.size() + 1));
					stack.ebpOffset = stackPos.get(stack.varHintName);
				}
				
				if (asm.src instanceof StackMem && ((StackMem) asm.src).ebpOffset == null) {
					StackMem stack = (StackMem) asm.src;
					if (!stackPos.containsKey(stack.varHintName))
						stackPos.put(stack.varHintName, 8 * (stackPos.size() + 1));
					stack.ebpOffset = stackPos.get(stack.varHintName);
				}
			}
		}
		asmFunct.stackLocalOffset = 8 * stackPos.size();
	}
}
