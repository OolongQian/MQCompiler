package opt.optimizers;

import ir.List_;
import ir.quad.Branch;
import ir.quad.Jump;
import ir.quad.Quad;
import ir.structure.BasicBlock;
import ir.structure.Function;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

/****************************** CFG structure changes ************************************/
/**
 * Control flow cleaner transforms CFG, the Defuse info needs to be recalculated
 * after it.
 *
 * Since SSA has been constructed, dominance relation changes doesn't matter at all.
 *
 * Anyway, this optimizer cannot be used in SSA form, because of the combined influence between constant propagation and phi node.
 * */
public class ControlFlowCleaner {
	
	private boolean cfgChange = true;
	private List_ list = null;
	
	public void CleanControlFlow(Function funct) {
		list = funct.bbs;
		while (cfgChange) {
			cfgChange = false;
			
			Queue<BasicBlock> workList = new ArrayDeque<>();
			PostOrder(funct.bbs.GetHead(), workList);
			while (!workList.isEmpty()) {
				OneBlock(workList.remove());
			}
			
		}
	}
	
	private void PostOrder(BasicBlock blk, Queue<BasicBlock> workList) {
		// do it in post order.
		for (Iterator<BasicBlock> iter = blk.successor.iterator(); iter.hasNext(); ) {
			BasicBlock cur = iter.next();
			PostOrder(cur, workList);
		}
		if (!workList.contains(blk))
			workList.add(blk);
	}
	
	private void OneBlock(BasicBlock blk) {
		
		int endIndex = blk.TraverseQuad().size() - 1;
		Quad end = blk.GetLastQuad();
		if (end instanceof Branch && ((Branch) end).ifTrue == ((Branch) end).ifFalse) {
			assert blk.successor.size() == 1;
			Jump jump = new Jump(((Branch) end).ifTrue);
			jump.blk = blk;
			blk.TraverseQuad().set(endIndex, jump);
			
			cfgChange = true;
		}
		
		// if current basic block is empty.
		end = blk.GetLastQuad();
		if (end instanceof Jump) {
			BasicBlock nextJ = blk.successor.iterator().next();
			assert nextJ == ((Jump) end).target;
			assert blk.successor.size() == 1;
			if (blk.TraverseQuad().size() == 1) {
				// delete empty current blk.
				// note : basic block is linked in linked list and cfg.
				for (BasicBlock prev : blk.predecessor) {
					prev.successor.add(nextJ);
					prev.successor.remove(blk);
					ReplaceTarget(prev, blk, nextJ);
				}
				nextJ.predecessor.addAll(blk.predecessor);
				nextJ.predecessor.remove(blk);
				blk.successor.clear();
				blk.predecessor.clear();
				list.Remove(blk);
				
				cfgChange = true;
			}
			// if j has only one predecessor, just combine i and j.
			// combine j into i.
			else if (nextJ.predecessor.size() == 1) {
				assert nextJ.predecessor.iterator().next() == blk;
				// remove end.
				blk.TraverseQuad().remove(end);
				// quad modification.
				blk.TraverseQuad().addAll(nextJ.TraverseQuad());
				// control flow modification.
				for (BasicBlock scs : nextJ.successor) {
					scs.predecessor.add(blk);
					scs.predecessor.remove(nextJ);
				}
				blk.successor.addAll(nextJ.successor);
				blk.successor.remove(nextJ);
				list.Remove(nextJ);
				
				cfgChange = true;
			}
			// if nextJ is empty and only has a branch quad.
			else if (nextJ.TraverseQuad().size() == 1 && nextJ.GetLastQuad() instanceof Branch) {
				Branch branch = (Branch) nextJ.GetLastQuad();
				Branch branchCopy = new Branch(branch.cond, branch.ifTrue, branch.ifFalse);
				branchCopy.blk = blk;
				blk.TraverseQuad().set(endIndex, branchCopy);
				blk.successor.add(branchCopy.ifTrue);
				blk.successor.add(branchCopy.ifFalse);
				branchCopy.ifTrue.predecessor.add(blk);
				branchCopy.ifFalse.predecessor.add(blk);
				
				cfgChange = true;
			}
		}
		
	}
	
	private void ReplaceTarget(BasicBlock blk, BasicBlock oldTarget, BasicBlock newTarget) {
		Quad control = blk.TraverseQuad().get(blk.TraverseQuad().size() - 1);
		if (control instanceof Jump) {
			assert ((Jump) control).target == oldTarget;
			((Jump) control).target = newTarget;
		}
		else if (control instanceof Branch) {
			boolean replace = false;
			if (((Branch) control).ifTrue == oldTarget) {
				((Branch) control).ifTrue = newTarget;
				replace = true;
			} else if (((Branch) control).ifFalse == oldTarget){
				((Branch) control).ifFalse = newTarget;
				replace = true;
			}
			assert replace;
		}
	}
}
