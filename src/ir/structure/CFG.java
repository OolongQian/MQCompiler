package ir.structure;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * maintain the cfg information.
 * */
public class CFG {

	public HashMap<BasicBlock, Set<BasicBlock>> predesessors = new HashMap<>();
	public HashMap<BasicBlock, Set<BasicBlock>> successors = new HashMap<>();
	
	// initialization
	public void ConfigBasicBlock(ListBB list) {
		BasicBlock cur = list.Head();
		while (cur != null) {
			predesessors.put(cur, new HashSet<>());
			successors.put(cur, new HashSet<>());
			cur = cur.next;
		}
	}
	
	public void JumpTo(BasicBlock from, BasicBlock to) {
		successors.get(from).add(to);
		predesessors.get(to).add(from);
	}
}
