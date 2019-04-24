package opt.optimizers;

import ir.quad.Quad;
import ir.structure.BasicBlock;
import ir.structure.IrFunct;
import ir.structure.Reg;

import java.util.*;

public class Defuse {
	public static HashMap<Reg, DefuseInfo> ssaVars = new HashMap<>();
	public static BasicBlock entry;
	
	/*************************** info collect methods ***********************/
	public static void CollectFunctDefuse(IrFunct funct) {
		entry = funct.bbs.list.Head();
		BasicBlock curBB = entry;
		while (curBB != null) {
			for (Quad quad : curBB.quads) {
				// record def
				Reg defReg = quad.GetDefReg();
				if (defReg != null) {
					// phy node may have def after use.
					if (!ssaVars.containsKey(defReg))
						ssaVars.put(defReg, new DefuseInfo());
					ssaVars.get(defReg).defQuad = quad;
				}
				// update usage
				List<Reg> useRegs = new LinkedList<>();
				quad.GetUseRegs(useRegs);
				for (Reg reg : useRegs) {
					if (!ssaVars.containsKey(reg))
						ssaVars.put(reg, new DefuseInfo());
					ssaVars.get(reg).useQuads.add(quad);
				}
			}
			curBB = curBB.next;
		}
	}
	
	/***************************** info query methods ***********************************/
	/**
	 * Get a list of quads where the input reg is used.
	 */
	public static Set<Quad> GetUseQuads(Reg reg) {
		return ssaVars.get(reg).useQuads;
	}
	
	/**
	 * If an SSA reg is about to be eliminated, find its def and wipe its use
	 * from all other SSA regs used.
	 */
	public static Quad GetDefQuad(Reg reg) {
		return ssaVars.get(reg).defQuad;
	}
}


class DefuseInfo {
	public Set<Quad> useQuads = new HashSet<>();
	public Quad defQuad;
}
