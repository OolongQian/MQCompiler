package opt;

import ir.quad.Quad;
import ir.structure.BasicBlock;
import ir.structure.Function;
import ir.structure.Reg;

import java.util.*;

/**
 * This is a class recording SSA register info.
 * */
public class Defuse {
	public static HashMap<Reg, DefuseInfo> ssaVars = new HashMap<>();
	
	/*************************** info collect methods ***********************/
	public static void CollectFunctDefuse(Function funct) {
		BasicBlock curBB = funct.bbs.GetHead();
		while (curBB != null) {
			DefuseBlock(curBB);
			curBB = (BasicBlock) curBB.next;
		}
		DefuseBlock(funct.bbs.GetHead());
	}
	private static void DefuseBlock(BasicBlock blk) {
		List<Quad> quads = blk.TraverseQuad();
		quads.forEach(Defuse::DefuseQuad);
	}
	/**
	 * Used before another function's def-use collection.
	 * */
	public static void ClearDefuse() {
		ssaVars.clear();
	}
	
	/**
	 * Collect all live SSA registers
	 */
	private static void DefuseQuad(Quad quad) {
		// record def
		Reg defReg = quad.GetDefReg();
		if (defReg != null) {
			
			// phi node may have def after use.
			if (!ssaVars.containsKey(defReg))
				ssaVars.put(defReg, new DefuseInfo());
			
			ssaVars.get(defReg).defQuad = quad;
		}
		
		// update usage.
		List<Reg> useRegs = new LinkedList<>();
		quad.GetUseRegs(useRegs);
		for (Reg reg : useRegs) {
			if (!ssaVars.containsKey(reg))
				ssaVars.put(reg, new DefuseInfo());
			
			ssaVars.get(reg).useQuads.add(quad);
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
}

