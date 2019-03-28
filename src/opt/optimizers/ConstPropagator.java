package opt.optimizers;

import ir.quad.Mov;
import ir.quad.Phi;
import ir.quad.Quad;
import ir.structure.Constant;
import ir.structure.Reg;
import opt.Defuse;
import java.util.*;

/*************************** CFG structure unchanged **********************************/
/**
 * This optimizer maintains def-use chain.
 * */
public class ConstPropagator {
	
	public void ConstPropagate() {
		Propagate();
	}
	
	/**
	 * 1. Collect all defs.
	 * 2. analyze their format for v <-- c; v <-- phi(c, c, c).
	 * */
	private void Propagate() {
		Queue<Quad> workList = new ArrayDeque<>();
		// traverse DefuseInfo.
		Defuse.ssaVars.values().forEach(x -> workList.addAll(x.useQuads));
		
		while (!workList.isEmpty()) {
			Reg v = null;
			Constant c = null;
			Quad quad = workList.poll();
			
			// v <-- c, replace all uses of v with c.
			if (quad instanceof Mov && ((Mov) quad).src instanceof Constant) {
				v =  quad.GetDefReg();
				c = (Constant) ((Mov) quad).src;
			}
			// v <-- phi(c, c, c), replace all uses of v with c.
			else if (quad instanceof Phi) {
				if (((Phi) quad).CheckAllSameConst()) {
					v = quad.GetDefReg();
					c = (Constant) ((Phi) quad).options.values().iterator().next();
				}
			}
			// FIXME : I think pure constant operanded arithmetic computation quad should also be evaluated by constant propagation.
			// not valid format.
			if (v == null || c == null)
				continue;
			
			// replace v's use in downstream quads by consts, add these quads to workList.
			Set<Quad> vUses = Defuse.GetUseQuads(v);
			for (Quad useQ : vUses) {
				useQ.ReplaceUse(v, c);
				if (!workList.contains(useQ))
					workList.add(useQ);
			}
			// delete current quad.
			quad.blk.TraverseQuad().remove(quad);
			// maintain def-use chain.
			// for this variable v, its def is gone, all its uses have gone, thus all its info is garbage.
			Defuse.ssaVars.remove(v);
		}
	}
}
