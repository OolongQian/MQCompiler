package nasm;

import ir.structure.BasicBlock;
import ir.structure.CFG;
import nasm.inst.Cmp;
import nasm.inst.Inst;
import nasm.reg.*;

import java.util.*;

import static nasm.Utils.BasicBlockRenamer;

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
	
	public AsmFunct(String name, List<ir.structure.Reg> regArgs) {
		this.name = name;
		regArgs.forEach(x -> argNames.add(x.name));
	}
	
	public void InitCFG(CFG irCFG) {
		for (BasicBlock bb : irCFG.predesessors.keySet()) {
			AsmBB curBB = GetBBbyName(BasicBlockRenamer(bb));
			
			cfg.predesessors.put(curBB, new HashSet<>());
			cfg.successors.put(curBB, new HashSet<>());
			
			for (BasicBlock pre : irCFG.predesessors.get(bb)) {
				String preBBName = BasicBlockRenamer(pre);
//				Set<AsmBB> preSet = cfg.predesessors.get(curBB);
//				preSet.add(GetBBbyName(preBBName));
				cfg.predesessors.get(curBB).add(GetBBbyName(preBBName));
			}
			for (BasicBlock scs : irCFG.successors.get(bb)) {
				String scsBBName = BasicBlockRenamer(scs);
				cfg.successors.get(curBB).add(GetBBbyName(scsBBName));
			}
		}
		
		// some basic blocks have no predesessors or successors. initialize them as an empty hashset.
		for (AsmBB bb : bbs) {
			if (!cfg.predesessors.containsKey(bb))
				cfg.predesessors.put(bb, new HashSet<>());
			if (!cfg.successors.containsKey(bb))
				cfg.successors.put(bb, new HashSet<>());
		}
	}
	
	//
	public void CalcStackOffset() {
		assert stackLocalOffset == null;
		stackLocalOffset = 0;
		
		Set<StackMem> worklist = new HashSet<>();
		
		for (AsmBB bb : bbs) {
			for (Inst inst : bb.insts) {
				
				if (inst.dst instanceof StackMem && ((StackMem) inst.dst).ebpOffset == null)
					worklist.add((StackMem) inst.dst);
				
				if (inst.src instanceof StackMem && ((StackMem) inst.src).ebpOffset == null)
					worklist.add((StackMem) inst.src);
				
				// Cmp's extra is Reg. Don't consider.
			}
		}
		
		Map<String, Integer> processed = new HashMap<>();
		
		for (Iterator<StackMem> iter = worklist.iterator(); iter.hasNext(); ) {
			StackMem work = iter.next();
			
			if (!processed.containsKey(work.varHintName))
				processed.put(work.varHintName, (stackLocalOffset += 8));
			
			work.ebpOffset = processed.get(work.varHintName);
		}
	}
	
	/***************** Utility ********************/
	private int cnt = 0;
	public Reg GetTmpReg() {
		return new Reg(String.format("%s_tmp%d", name, ++cnt));
	}
	
	private AsmBB GetBBbyName (String bbName) {
		for (AsmBB bb : bbs) {
			if (bb.hintName.equals(bbName))
				return bb;
		}
		int a = 1;
		assert false;
		return null;
	}
}
