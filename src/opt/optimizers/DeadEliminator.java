package opt.optimizers;

import ir.IrProg;
import ir.quad.*;
import ir.structure.BasicBlock;
import ir.structure.IrFunct;
import ir.structure.Reg;

import java.util.*;

// a complicated but comprehensive dead code elimination method in EaC.
// build def use chain from scratch.
// we don't do dead control flow elimination.
public class DeadEliminator {

	// mark live code, refuse to eliminate them.
	private HashSet<Quad> alive = new HashSet<>();

	public void EliminateDeadCode (IrProg ir) {
		for (IrFunct funct : ir.functs.values()) {
			Defuse.CollectFunctDefuse(funct);

			// mark.
			Queue<Quad> worklist = new ArrayDeque<>();
			for (BasicBlock cur = funct.bbs.list.Head(); cur != null; cur = cur.next) {
				for (Quad inst : cur.quads) {
					// push all useful quads into worklist.
					// keep critical quads alive.
					// iteratively add var's def quad, where the vars are used to
					// define other alive vars.
					if (!(inst instanceof Unary) &&
							!(inst instanceof Binary) &&
							!(inst instanceof Phi) &&
							!(inst instanceof Mov)) {
						worklist.add(inst);
					}
					alive.addAll(worklist);
				}

				List<Reg> uses = new LinkedList<>();
				while (!worklist.isEmpty()) {
					// get all its uses, iteratively mark them as alive.
					Quad liveq = worklist.remove();
					liveq.GetUseRegs(uses);

					for (Reg use : uses) {
						Quad def = Defuse.GetDefQuad(use);
						// if this use is a function parameter or global or string. it has no def.
						if (def == null)
							continue;

						if (!alive.contains(def)) {
							alive.add(def);
							worklist.add(def);
						}
					}
				}
			}

			// sweep. Eliminate unmarked quads.
			for (BasicBlock cur = funct.bbs.list.Head(); cur != null; cur = cur.next) {
				cur.quads.removeIf(x -> !alive.contains(x));
			}
		}
	}
}
