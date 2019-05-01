package opt.optimizers;

import ir.IrProg;
import ir.Printer;
import ir.quad.Branch;
import ir.quad.Jump;
import ir.quad.Quad;
import ir.quad.Ret;
import ir.structure.BasicBlock;
import ir.structure.CFG;
import ir.structure.IrFunct;

public class CFGCleaner {
	
	private boolean change = true;
	
	public int changeCnt = 0;
	
	public void CFGclean(IrProg ir, boolean skipPreheader) {
		CheckCodeCFG(ir);
		ir.BuildCFG();
		
		for (IrFunct funct : ir.functs.values()) {
			do {
				change = false;
				
				BasicBlock cur = funct.bbs.list.Head();
				while (cur != null) {
					assert !cur.quads.isEmpty();

					// don't move preheaders.
					if (cur.name.startsWith("$pre") && skipPreheader) {
						cur = cur.next;
						continue;
					}

					Quad cntl = cur.quads.get(cur.quads.size() - 1);
					// combine redundant branch
					if (cntl instanceof Branch) {
						Branch branch = (Branch) cntl;
						if (branch.ifFalse == branch.ifTrue) {
							Jump jmp = new Jump(branch.ifTrue);
							jmp.blk = cur;
							cur.quads.remove(branch);
							cur.quads.add(jmp);
							change = true;
							++changeCnt;
						}
					}
					else if (cntl instanceof Jump && ((Jump) cntl).target != cur) {
						// if current bb contains only one jump.
						if (cur != funct.bbs.list.Head() && cur.quads.size() == 1) {
							cur = DeleteEmptyBB(cur);
							change = true;
							++changeCnt;
							continue;
						}
						CFG cfg = cur.parentFunct.bbs.cfg;
						BasicBlock target = ((Jump) cntl).target;
						if (target.name.startsWith("$pre")) {
							cur = cur.next;
							continue;
						}
						assert cfg.successors.get(cur).size() == 1 && cfg.successors.get(cur).iterator().next() == target;
						if (target != funct.bbs.list.Head() && cfg.predesessors.get(target).size() == 1) {
							cur = CombineBB(cur);
							change = true;
							++changeCnt;
							continue;
						}
					}
					cur = cur.next;
				}
			} while (change);
		}
	}
	
	// return blk's prev in bblist.
	private BasicBlock DeleteEmptyBB(BasicBlock blk) {
		BasicBlock ret = blk.next;
		// transfer control from predecessor to next.
		Jump jmp = (Jump) blk.quads.get(0);
		BasicBlock target = jmp.target;
		
		CFG cfg = blk.parentFunct.bbs.cfg;
		for (BasicBlock pre : cfg.predesessors.get(blk)) {
			Quad last = pre.quads.get(pre.quads.size() - 1);
			assert last instanceof Jump || last instanceof Branch;
			ReplaceBranchJump(last, blk, target);
			// maintain cfg.
			cfg.successors.get(pre).remove(blk);
			cfg.successors.get(pre).add(target);
			cfg.predesessors.get(target).add(pre);
		}
		cfg.predesessors.get(target).remove(blk);
		cfg.predesessors.get(blk).clear(); cfg.successors.get(blk).clear();
		blk.parentFunct.bbs.list.Remove(blk);
		
		return ret;
	}
	
	// combine current bb with its next bb.
	// return current bb actually
	private BasicBlock CombineBB (BasicBlock blk) {
		BasicBlock ret = blk;

		Jump jmp = (Jump) blk.quads.get(blk.quads.size() - 1);
		BasicBlock next = jmp.target;
		
		// copy code.
		blk.quads.remove(jmp);
		blk.quads.addAll(next.quads);
		// maintain cfg.
		CFG cfg = blk.parentFunct.bbs.cfg;
		cfg.successors.get(blk).addAll(cfg.successors.get(next));
		cfg.successors.get(blk).remove(next);
		for (BasicBlock scs : cfg.successors.get(next)) {
			cfg.predesessors.get(scs).remove(next);
			cfg.predesessors.get(scs).add(blk);
		}
		// remove next from list.
		blk.parentFunct.bbs.list.Remove(next);
		return ret;
	}
	
	private void ReplaceBranchJump(Quad jmp, BasicBlock old, BasicBlock target) {
		if (jmp instanceof Jump) {
			assert ((Jump) jmp).target == old;
			((Jump) jmp).target = target;
		} else {
			// jmp is br.
			boolean replace = false;
			Branch br = (Branch) jmp;
			if (br.ifTrue == old) {
				br.ifTrue = target;
				replace = true;
			}
			if (br.ifFalse == old) {
				br.ifFalse = target;
				replace = true;
			}
			assert replace;
		}
	}
	
	
	private void CheckCodeCFG(IrProg ir) {
		for (IrFunct funct : ir.functs.values()) {
			for (BasicBlock cur = funct.bbs.list.Head(); cur != null; cur = cur.next) {
				for (int i = 0; i < cur.quads.size(); ++i) {
					Quad cq = cur.quads.get(i);
					if (cq instanceof Jump || cq instanceof Branch || cq instanceof Ret) {
						assert i == cur.quads.size() - 1;
					}
				}
			}
		}
	}
}
