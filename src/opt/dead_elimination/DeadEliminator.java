package opt.dead_elimination;

import ir.Printer;
import ir.quad.Call;
import ir.quad.Quad;
import ir.structure.BasicBlock;
import ir.structure.Reg;
import opt.Usedef;
import opt.ssa_build.Info;
import opt.ssa_build.SSA;

import java.util.*;

import static opt.Usedef.IsSSA;

public class DeadEliminator {
	


	// entry BB for current function.
	SSA ctx;
	private Map<BasicBlock, Set<BasicBlock>> dominanceTree = new HashMap<>();
	public void Config(SSA ctx) {
		// construct dominance tree from SSA's analysis
		this.ctx = ctx;
		Map<BasicBlock, Info> infos = ctx.GetInfos();
		for (BasicBlock blk : infos.keySet()) {
			dominanceTree.put(blk, infos.get(blk).domTree);
		}
	}
	
	public void EliminateDeadCode() {
		CollectDefuse(ctx.GetCfgEntry());
//		PrintDefUse();
		Eliminate();
	}
	/**
	 * Traverse dominant tree to collect def and use.
	 * */
	private void CollectDefuse(BasicBlock blk) {
		List<Quad> quads = blk.TraverseQuad();
		quads.forEach(Usedef::DefUseRecord);
		dominanceTree.get(blk).forEach(this::CollectDefuse);
	}
	
	// for debug
	private void PrintDefUse() {
		Printer printer = new Printer(null);
		for (Reg reg : Usedef.ssaVars.keySet()) {
			System.err.print(reg.name + ": ");
			Usedef.GetDefQuad(reg).AcceptPrint(printer);
			Usedef.GetUseQuads(reg).forEach(x -> x.AcceptPrint(printer));
			System.err.println();
		}
	}
	
	private void Eliminate() {
		Queue<Reg> workList = new ArrayDeque<>();
		workList.addAll(Usedef.ssaVars.keySet());
		
		while (!workList.isEmpty()) {
			Reg var = workList.remove();
			// if this var isn't used.
			if (Usedef.GetUseQuads(var).isEmpty()) {
				Quad def = Usedef.GetDefQuad(var);
				// FIXME : function call will have side effects, we cannot eliminate them.
				if (!(def instanceof Call)) {
					// remove this quad from current program.
					def.blk.TraverseQuad().remove(def);
					
					List<Reg> uses = new LinkedList<>();
					def.GetUseRegs(uses);
					// these regs aren't used in this quad anymore, because this quad has been eliminated.
					for (Reg use : uses) {
						// FIXME : ugly, generalize it later
						if (IsSSA(use)) {
							Usedef.GetUseQuads(use).remove(def);
							if (!workList.contains(use))
								workList.add(use);
						}
					}
				}
			}
		}
	}
}
