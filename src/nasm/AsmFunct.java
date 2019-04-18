package nasm;

import ir.structure.BasicBlock;
import ir.structure.CFG;
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
				cfg.predesessors.get(curBB).add(GetBBbyName(preBBName));
			}
			for (BasicBlock scs : irCFG.successors.get(bb)) {
				String scsBBName = BasicBlockRenamer(scs);
				cfg.predesessors.get(curBB).add(GetBBbyName(scsBBName));
			}
		}
	}
	
	/***************** Utility ********************/
	private int cnt = 0;
	public Reg GetTmpReg() {
		return new Reg("inst" + cnt++);
	}
	
	private AsmBB GetBBbyName (String bbName) {
		for (AsmBB bb : bbs) {
			if (bb.hintName.equals(bbName))
				return bb;
		}
		assert false;
		return null;
	}
}
