package opt.optimizers;

import ir.Printer;
import ir.quad.Call;
import ir.quad.Quad;
import ir.structure.Reg;
import opt.Defuse;
import opt.DefuseInfo;

import java.util.*;

import static ir.Config.DEADLOG;

/**
 * Dead code eliminator depends on the def-use info in Defuse.java class.
 * */
public class DeadEliminator {
	
	public void DeadCodeEliminate() {
//		PrintDefUse();
		Eliminate();
	}
	
	// for debug
	private void PrintDefUse() {
		Printer printer = new Printer(null);
		for (Reg reg : Defuse.ssaVars.keySet()) {
			System.err.print(reg.name + ": ");
			Defuse.GetDefQuad(reg).AcceptPrint(printer);
			Defuse.GetUseQuads(reg).forEach(x -> x.AcceptPrint(printer));
			System.err.println();
		}
	}
	
	private void Eliminate() {
		Queue<Reg> workList = new ArrayDeque<>();
		workList.addAll(Defuse.ssaVars.keySet());
		
		while (!workList.isEmpty()) {
			Reg var = workList.remove();
			
			// if this var isn't used, eliminate its def.
			// if this var is a function argument, it doesn't have a def explicitly, thus we do nothing.
			if (Defuse.GetUseQuads(var).isEmpty()) {
				Quad def = Defuse.GetDefQuad(var);
				// if this is a function argument.
				if (def == null) break;
				// FIXME : function call will have side effects, we cannot eliminate them.
				if (!(def instanceof Call)) {
					// remove this quad from current program.
					def.blk.TraverseQuad().remove(def);
					
					if (DEADLOG) {
						Printer printer = new Printer(null);
						System.out.println("remove ");
						def.AcceptPrint(printer);
						
					}
					
					List<Reg> uses = new LinkedList<>();
					def.GetUseRegs(uses);
					// these regs aren't used in this quad anymore, because this quad has been eliminated.
					for (Reg use : uses) {
					// FIXME : ugly, generalize it later
						Defuse.GetUseQuads(use).remove(def);
						if (!workList.contains(use))
							workList.add(use);
					}
				}
			}
		}
	}
}
