package ir.structure;

import ir.quad.Branch;
import ir.quad.Jump;
import ir.quad.Quad;

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
	 * */
	public void AddFallThroughJump() {
		BasicBlock cur = list.Head();
		// note that here is cur.next because the jump target is cur.next.
		while (cur.next != null) {
			// add default jump if there isn't jump or branch guard the bottom.
			boolean skipJump = false;
			if (!cur.quads.isEmpty()) {
				Quad last = cur.quads.get(cur.quads.size() - 1);
				if (last instanceof Jump || last instanceof Branch)
					skipJump = true;
			}
			if (!skipJump) {
				Jump fallThrough = new Jump(cur.next);
				fallThrough.blk = cur;
				cur.quads.add(fallThrough);
			}
			cur = cur.next;
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
