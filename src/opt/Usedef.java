package opt;

import ir.quad.Quad;
import ir.structure.Reg;

import java.util.*;

/**
 * This is a class recording SSA register info.
 * */
public class Usedef {
	public static HashMap<Reg, UsedefReg> ssaVars = new HashMap<>();
	
	/*************************** info collect methods ***********************/
	/**
	 * Collect all live SSA registers
	 */
	public static void DefUseRecord(Quad quad) {
		// record def
		Reg defReg = quad.GetDefReg();
		if (defReg != null && IsSSA(defReg)) {
			
			// phi node may have def after use.
			if (!ssaVars.containsKey(defReg))
				ssaVars.put(defReg, new UsedefReg());
			
			UsedefReg defInfo = ssaVars.get(defReg);
			defInfo.defQuad = quad; // record def quad.
			ssaVars.put(defReg, defInfo);
		}
		
		// update usage.
		List<Reg> useRegs = new LinkedList<>();
		quad.GetUseRegs(useRegs);
		for (Reg reg : useRegs) {
			if (IsSSA(reg)) {
				
				if (!ssaVars.containsKey(reg))
					ssaVars.put(reg, new UsedefReg());
				
				UsedefReg useInfo = ssaVars.get(reg);
				useInfo.useQuads.add(quad);
			}
		}
	}
	
	/****************************** info query methods ***********************************/
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
	
	
	/***************************** utility methods ***********************************/
	public static boolean IsSSA(Reg reg) {
		return reg.name.startsWith("!");
//		return true;
	}
}

/**
 * used to store ssa register profile.
 * About def and use.
 * */
class UsedefReg {
	public Set<Quad> useQuads = new HashSet<>();
	public Quad defQuad;
}