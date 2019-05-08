package opt.optimizers;

import ir.quad.*;
import ir.structure.BasicBlock;
import ir.structure.IrFunct;
import ir.structure.Reg;
import opt.GraphInfo;

import java.util.*;

// a complicated but comprehensive dead code elimination method in EaC.
// build def use chain from scratch.
// we don't do dead control flow elimination.
public class DeadEliminator {

	// mark live code, refuse to eliminate them.
	private HashSet<Quad> alive = new HashSet<>();
	private Set<BasicBlock> aliveBB = new HashSet<>();
	private Map<Reg, Set<Quad>> defsQuad = new HashMap<>();
	
	private void CollectDefs(IrFunct funct) {
		for (BasicBlock cur = funct.bbs.list.Head(); cur != null; cur = cur.next) {
			for (Quad quad : cur.quads) {
				Reg def = quad.GetDefReg();
				if (!defsQuad.containsKey(def))
					defsQuad.put(def, new HashSet<>());
				defsQuad.get(def).add(quad);
			}
		}
	}
	
	public void EliminateDeadCode (IrFunct funct, HashMap<BasicBlock, GraphInfo> rgInfos) {
		alive.clear();
		aliveBB.clear();
		defsQuad.clear();
		CollectDefs(funct);

		// mark.
		Queue<Quad> worklist = new ArrayDeque<>();
		for (BasicBlock cur = funct.bbs.list.Head(); cur != null; cur = cur.next) {
			for (Quad inst : cur.quads) {
				if (inst.blk != cur) {
					int a=  1;
				}
				// push all useful quads into worklist.
				// keep critical quads alive.
				// iteratively add var's def quad, where the vars are used to
				// define other alive vars.
				if ((inst instanceof Store) ||
						(inst instanceof Call) ||
						(inst instanceof Malloc) ||
						(inst instanceof Ret)) {
					worklist.add(inst);
				}
//				if (!(inst instanceof Unary) &&
//						!(inst instanceof Binary) &&
//						!(inst instanceof Phi) &&
//						!(inst instanceof Mov) &&
//						!(inst instanceof Load)) {
//						 note : store must be maintained, but load can be optimized.
//					worklist.add(inst);
//				}
				alive.addAll(worklist);
			}

			List<Reg> uses = new LinkedList<>();
			while (!worklist.isEmpty()) {
				// get all its uses, iteratively mark them as alive.
				Quad liveq = worklist.remove();
				liveq.GetUseRegs(uses);

				for (Reg use : uses) {
					Set<Quad> defs = defsQuad.getOrDefault(use, null);
					// if this use is a function parameter or global or string. it has no def.
					if (defs == null)
						continue;
					
					for (Quad def : defs) {
						if (!alive.contains(def)) {
							alive.add(def);
							worklist.add(def);
						}
					}
				}
				
				BasicBlock curB = liveq.blk;
//				System.out.println("print dominance frontier...");
//				System.out.println(cur.name);
//				rgInfos.get(curB).domFrontier.forEach(x -> System.out.print(x.name + " "));
//				System.out.print("\n\n");
				if (!rgInfos.containsKey(curB)) {
					int a = 1;
				}
				for (BasicBlock rdf : rgInfos.get(curB).domFrontier) {
					Quad last = rdf.quads.get(rdf.quads.size() - 1);
					if (last instanceof Branch) {
						if (!alive.contains(last)) {
							alive.add(last);
							worklist.add(last);
						}
					}
				}
			}
		}

		alive.forEach(x -> aliveBB.add(x.blk));
		
		// sweep. Eliminate unmarked quads.
		for (BasicBlock cur = funct.bbs.list.Head(); cur != null; cur = cur.next) {
			for (ListIterator<Quad> iter = cur.quads.listIterator(); iter.hasNext(); ) {
				Quad quad = iter.next();
				if (!alive.contains(quad)) {
					if (quad instanceof Branch) {
						BasicBlock nearRdom = rgInfos.get(cur).iDom;
						while (nearRdom != null && !aliveBB.contains(nearRdom))
							nearRdom = rgInfos.get(nearRdom).iDom;
						assert nearRdom != null;
						Jump jmp2alive = new Jump(nearRdom);
						jmp2alive.blk = cur;
						iter.remove();
						iter.add(jmp2alive);
					}
					else if (!(quad instanceof Jump)) {
						iter.remove();
					}
				}
			}
		}
	}
}
