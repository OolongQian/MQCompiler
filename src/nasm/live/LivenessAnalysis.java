package nasm.live;

import nasm.AsmBB;
import nasm.AsmCFG;
import nasm.AsmFunct;
import nasm.inst.*;
import nasm.reg.Reg;

import java.util.*;

public class LivenessAnalysis {
	
	// map all asmBB in the program to its liveOut variable set.
	// basic blocks for all functs are here.
	public Map<AsmBB, Set<String>> liveOuts = new HashMap<>();
	
	// see p329 EaC for detail.
	private Map<AsmBB, Set<String>> ueVars = new HashMap<>();
	private Map<AsmBB, Set<String>> varKills = new HashMap<>();
	
	public void LivenessAnalyze(AsmFunct asmFunct) {
		asmFunct.bbs.forEach(this::CollectInfo);
		
		asmFunct.bbs.forEach(x -> liveOuts.put(x, new HashSet<>()));
		// for fixed-point algorithm
		boolean changed = true;
		while (changed) {
			changed = false;
			for (AsmBB bb : asmFunct.bbs)
				changed = changed | RecomputeLiveOut(bb);
		}
	}
	
	// note : think about pre-colored registers and non-registers.
	// note : think about all different use cases in isntructions.
	private void CollectInfo (AsmBB asmBB) {
		Set<String> ueVar = new HashSet<>();
		Set<String> varKill = new HashSet<>();
		
		ueVars.put(asmBB, ueVar);
		varKills.put(asmBB, varKill);
		
		List<Reg> tmp;
		for (Inst inst : asmBB.insts) {
			tmp = GetUses(inst);
			for (Reg use : tmp)
				if (!varKill.contains(use.hintName))
					ueVar.add(use.hintName);
			
			tmp = GetDefs(inst);
			for (Reg def : tmp)
				varKill.add(def.hintName);
		}
	}
	
	private boolean RecomputeLiveOut (AsmBB asmBB) {
		AsmCFG cfg = asmBB.parentFunct.cfg;
		Set<String> liveOut = liveOuts.get(asmBB);
		
		int oldSize = liveOut.size();
		for (AsmBB scs : cfg.successors.get(asmBB)) {
			// liveout if used afterwards.
			liveOut.addAll(ueVars.get(scs));
			// liveout if liveout afterwards but not due to redefine. 
			for (String s : liveOuts.get(scs)) {
				if (!varKills.get(scs).contains(s))
					liveOut.add(s);
			}
		}
		// if size increment, liveout set changes.
		return liveOut.size() != oldSize;
	}
	
	public List<Reg> GetUses (Inst inst) {
		List<Reg> uses = new LinkedList<>();
		
		if (inst instanceof Call) { }
		else if (inst instanceof Cmp) {
			if (inst.dst instanceof Reg) uses.add((Reg) inst.dst);
			if (inst.src instanceof Reg) uses.add((Reg) inst.src);
		}
		else if (inst instanceof Jmp) { }
		else if (inst instanceof Lea) {
			if (inst.dst instanceof Reg) uses.add((Reg) inst.dst);
			if (inst.src instanceof Reg) uses.add((Reg) inst.src);
		}
		else if (inst instanceof Mov) {
			if (inst.src instanceof Reg) uses.add((Reg) inst.src);
		}
		else if (inst instanceof Oprt) {
			if (inst.dst instanceof Reg) uses.add((Reg) inst.dst);
			if (inst.src instanceof Reg) uses.add((Reg) inst.src);
		}
		else if (inst instanceof Msg) { }
		else if (inst instanceof Pop) { }
		else if (inst instanceof Push) {
			if (inst.src instanceof Reg) uses.add((Reg) inst.src);
		}
		else if (inst instanceof Load) {
			if (inst.src instanceof Reg) uses.add((Reg) inst.src);
		}
		else if (inst instanceof Store) {
			if (inst.src instanceof Reg) uses.add((Reg) inst.src);
		}
		else if (inst instanceof Ret) { }
		else {
			assert false;
		}
		
		return uses;
	}
	
	public List<Reg> GetDefs (Inst inst) {
		List<Reg> defs = new LinkedList<>();
		
		if (inst instanceof Call) { }
		else if (inst instanceof Cmp) {
			if (((Cmp) inst).flagReg instanceof Reg) defs.add(((Cmp) inst).flagReg);
		}
		else if (inst instanceof Jmp) { }
		else if (inst instanceof Lea) {
			if (inst.dst instanceof Reg) defs.add((Reg) inst.dst);
		}
		else if (inst instanceof Mov) {
			if (inst.dst instanceof Reg) defs.add((Reg) inst.dst);
		}
		else if (inst instanceof Oprt) {
			if (inst.dst instanceof Reg) defs.add((Reg) inst.dst);
		}
		else if (inst instanceof Msg) { }
		else if (inst instanceof Pop) {
			if (inst.dst instanceof Reg) defs.add((Reg) inst.dst);
		}
		else if (inst instanceof Push) { }
		else if (inst instanceof Load) {
			if (inst.dst instanceof Reg) defs.add((Reg) inst.dst);
		}
		else if (inst instanceof Store) {
			if (inst.dst instanceof Reg) defs.add((Reg) inst.dst);
		}
		else if (inst instanceof Ret) { }
		else {
			assert false;
		}
		
		return defs;
	}
}
