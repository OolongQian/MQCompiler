package opt;

import ir.structure.BasicBlock;
import opt.ssa_build.Info;

import java.util.HashMap;
import java.util.Set;

public class DominanceForest {
	private static HashMap<BasicBlock, Set<BasicBlock>> dominanceForest = new HashMap<>();
	
	/**
	 * Extract dominanceTree info for a single function based on ssa_build.Info
	 */
	public static void BuildDominanceForest(HashMap<BasicBlock, Info> infos) {
		for (BasicBlock blk : infos.keySet()) {
			dominanceForest.put(blk, infos.get(blk).domTree);
		}
	}
	
	public static Set<BasicBlock> GetiDoms(BasicBlock blk) {
		return dominanceForest.get(blk);
	}
	
}
