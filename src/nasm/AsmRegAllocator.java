package nasm;

import nasm.inst.Inst;
import nasm.live.LivenessAnalysis;
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
	
	private LivenessAnalysis liveAnalyzer = new LivenessAnalysis();
	
	/** Allocate registers for nasm assembly, and record all preeped on stack variables. */
	public void AllocateRegister(AsmFunct asmFunct) {
		liveAnalyzer.LivenessAnalyze(asmFunct);
		
	}
}
