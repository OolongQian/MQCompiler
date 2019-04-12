package opt.optimizers;

import ir.quad.Mov;
import ir.quad.Quad;
import ir.structure.IrValue;
import ir.structure.Reg;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;

/**
 * In copy propagation, we literally destroy an SSA variable.
 * Eliminate its def, and wipe out all of its use.
 * Note, we have to maintain Def-use chain during this process.
 *
 * Copy propagation for phi node requires concurrent phi node evaluation,
 * which is not supported by our interpreter currently.
 *
 * Copy propagator cannot be used in SSA form.
 *
 * This is very bad because one use could have multiple defs.
 * */
public class CopyPropagator {

	/*
	We need to remove its definition... It's full of bugs.
	public void PropagateCopy() {
		Queue<Reg> workList = new ArrayDeque<>(Defuse.ssaVars.keySet());
		while (!workList.isEmpty()) {
			Reg var = workList.poll();
			Quad defQuad = Defuse.GetDefQuad(var);
			// if var is a argument passed, it has no explicit definition.
			if (defQuad instanceof Mov) {
				Set<Quad> useQuads = Defuse.GetUseQuads(var);
				for (Quad useQ : useQuads) {
					useQ.ReplaceUse(var, ((Mov) defQuad).src);
				}
				// add uses of dst all to the uses of src.
				if (((Mov) defQuad).src instanceof Reg) {
					Defuse.GetUseQuads((Reg) ((Mov) defQuad).src).addAll(Defuse.GetUseQuads(((Mov) defQuad).dst));
				}
				// remove dst from ssaVars.
				Defuse.ssaVars.remove(((Mov) defQuad).dst);
			}
		}
	}
	*/
	
	/************************* Need concurrent phi evaluation, CFG unchange. ********************************/
	public void PropagateCopy() {
		Queue<Reg> workList = new ArrayDeque<>(Defuse.ssaVars.keySet());
		
		while (!workList.isEmpty()) {
			Reg var = workList.poll();

			Quad defQuad = Defuse.GetDefQuad(var);
			// if var is a argument passed, it has no explicit definition.
			if (defQuad != null && defQuad instanceof Mov) {
				Set<Quad> useQuads = Defuse.GetUseQuads(var);
				IrValue srcVal = ((Mov) defQuad).src;
				
				for (Iterator<Quad> iter = useQuads.iterator(); iter.hasNext();) {
					Quad useQ = iter.next();
					// don't move phi node.
					// replace propagated copy by srcVal.
					useQ.ReplaceUse(var, srcVal);
					// then useQ isn't a use of var.
					iter.remove();
					// useQ is a ise of srcVar
					if (srcVal instanceof Reg)
						Defuse.GetUseQuads((Reg) srcVal).add(useQ);
				}
				// remove dst from ssaVars if it is no longer used.
				// remove definition quad.
				defQuad.blk.quads.remove(defQuad);
				// remove var from ssaVars
				Defuse.ssaVars.remove(((Mov) defQuad).dst);
			}
		}
	}
}
