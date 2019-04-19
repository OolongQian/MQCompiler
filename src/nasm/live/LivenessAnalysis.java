package nasm.live;

import nasm.AsmBB;
import nasm.AsmCFG;
import nasm.AsmFunct;
import nasm.inst.*;
import nasm.reg.Reg;

import java.util.*;

import static config.Config.DEBUGPRINT_LIVENESS;
import static nasm.Utils.GetDefs;
import static nasm.Utils.GetUses;

public class LivenessAnalysis {
	
	// this data structure logically belongs to allocation context.
	// map all asmBB in the program to its liveOut variable set.
	// basic blocks for all functs are here.
	public Map<AsmBB, Set<String>> liveOuts;
	
	// see p329 EaC for detail.
	private Map<AsmBB, Set<String>> ueVars = new HashMap<>();
	private Map<AsmBB, Set<String>> varKills = new HashMap<>();
	
	public void ConfigLiveOut(Map<AsmBB, Set<String>> liveOutsCtx) {
		this.liveOuts = liveOutsCtx;
	}
	public void LivenessAnalyze(AsmFunct asmFunct) {
			if (DEBUGPRINT_LIVENESS)
				cacheFunct = asmFunct;
		
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
	

	private AsmFunct cacheFunct;
	public void PrintLiveness() {
		System.out.println(String.format("%s liveness : ", cacheFunct.name));
		for (AsmBB asmBB : liveOuts.keySet()) {
			System.out.print(String.format("%s : ", asmBB.hintName));
			for (String s : liveOuts.get(asmBB))
				System.out.print(String.format(" %s", s));
			System.out.println();
		}
		System.out.println();
	}
}
