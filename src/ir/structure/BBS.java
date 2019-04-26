package ir.structure;

import ir.quad.Branch;
import ir.quad.Jump;
import ir.quad.Quad;
import ir.quad.Ret;

/**
 * This data structure manage the complexity of the linear and CFG basic blocks.
 * */
public class BBS {
	// this is a linear IR model.
	public ListBB list = new ListBB();
	// this is a graph model constructed based on ListBB.
	public CFG cfg = new CFG();
	
	/**
	 * Construct CFG, add default jump.
	 * first, clean CFG.
	 * */
	public void AddFallThroughJump() {
		
		for (BasicBlock cur = list.Head(); cur != null; cur = cur.next) {
			for (Quad quad : cur.quads) {
				if (quad instanceof Ret && cur.quads.indexOf(quad) != cur.quads.size() - 1)
					throw new RuntimeException("clean quads before CFG!");
			}
		}
		
		for (BasicBlock cur = list.Head(); cur != null; cur = cur.next) {
			boolean skipJump = false;
			if (!cur.quads.isEmpty()) {
				Quad last = cur.quads.get(cur.quads.size() - 1);
				if (last instanceof Jump || last instanceof Branch || last instanceof Ret)
					skipJump = true;
			}
			if (!skipJump) {
				Jump fallThrough = new Jump(cur.next);
				fallThrough.blk = cur;
				cur.quads.add(fallThrough);
			}
		}
	}

	public void BuildCFG() {
		cfg.predesessors.clear();
		cfg.successors.clear();
		cfg.ConfigBasicBlock(list);
		
		BasicBlock cur = list.Head();
		while (cur != null) {
			for (Quad quad : cur.quads) {
				if (quad instanceof Jump) {
					cfg.JumpTo(cur, ((Jump) quad).target);
				}
				else if (quad instanceof Branch) {
					cfg.JumpTo(cur, ((Branch) quad).ifTrue);
					cfg.JumpTo(cur, ((Branch) quad).ifFalse);
				}
			}
			cur = cur.next;
		}
	}
}
