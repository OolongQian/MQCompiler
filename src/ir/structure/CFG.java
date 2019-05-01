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
	
	// at least have a entry.
	public void PrintCFG () {
		System.out.println("cfg " + predesessors.keySet().iterator().next().parentFunct.name);
		for (BasicBlock key : predesessors.keySet()) {
			System.out.println(key.name);
			System.out.print(">> predecessor ");
			for (BasicBlock pred : predesessors.get(key))
				System.out.print(pred.name + " ");
			System.out.println();
			System.out.print(">> successor ");
			for (BasicBlock scs : successors.get(key))
				System.out.print(scs.name + " ");
			System.out.println();
			System.out.println();
		}
	}
}
