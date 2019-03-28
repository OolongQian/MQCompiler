package opt.ssa_build;

import ir.BuilderContext;
import ir.quad.*;
import ir.structure.*;
import opt.Defuse;
import opt.DominanceForest;
import opt.optimizers.CommonExprDeleter;
import opt.optimizers.ConstPropagator;
import opt.optimizers.CopyPropagator;
import opt.optimizers.DeadEliminator;

import java.util.*;

import static ir.Config.DUMMY;
import static ir.Config.SSALOG;
import static opt.ssa_build.Info.*;

public class SSA {

	/**
	 * Functions and interfaces need redesign.
	 * */
	public void ConstructSSA(BuilderContext ctx) {
		// construct dominance tree.
		for (Function funct : ctx.GetFuncts().values()) {
			Config(funct);
			BuildDominance();
			StrictDominance();
			BuildImmediateDominance();
			DominanceFrontier();
			UseDefCollection();
			PhiPlacement();
			Renaming();
			CheckRename();
			
			DominanceForest.BuildDominanceForest(infos);
			
			// construct def-use chain in current function.
			int cnt = 3;
			while (cnt-- != 0) {
				Defuse.ClearDefuse();
				Defuse.CollectFunctDefuse(funct);
				
				// depends on Defuse
				DeadEliminator eliminator = new DeadEliminator();
				ConstPropagator constProper = new ConstPropagator();
				CommonExprDeleter exprDeleter = new CommonExprDeleter();
				CopyPropagator copyProper = new CopyPropagator();
//			 eliminator performs optim based on Defuse data.
				eliminator.DeadCodeEliminate();
				constProper.ConstPropagate();
				copyProper.PropagateCopy();
				exprDeleter.WipeCommonExpr();
			}
			
		}
	}
	
	/************************ Config a CFG to optimize ************************/
	private BasicBlock cfgEntry;
	private String functName; // for debug
	public void Config(Function funct) {
		this.cfgEntry = funct.bbs.GetHead();
		this.functName = funct.name;
		
		// note that static data structure needs explicit reset.
//		this.infos.clear();
		vars.clear();
		
		BasicBlock curB = cfgEntry;
		while (curB != null) {
			infos.put(curB, new Info());
			curB = (BasicBlock) curB.next;
		}
	}
	
	/**
	 * Each basic block has a set of associated info.
	 * vars is global information, which needs a static data structure in Info.
	 * */
	private HashMap<BasicBlock, Info> infos = new HashMap<>();
	
	/**
	 * ************************* DOMINANT TREE **********************************
	 * Build dominant tree for a List_ of basic blocks.
	 * NOTE : this tree is strictly dominant.
	 */
	public void BuildDominance() {
		infos.values().forEach(x -> x.inferiorTo.clear());
		infos.values().forEach(x -> x.superiorTo.clear());
		infos.values().forEach(x -> x.vis = false);
		Set<BasicBlock> empty = new HashSet<>();
		
		BasicBlock curB = cfgEntry;
		while (curB != null) {
			// clear each BB.
			Set<BasicBlock> in = curB.predecessor;
			Set<BasicBlock> out = curB.successor;
			curB.predecessor = curB.successor = empty;
			// reset marker.
			infos.values().forEach(x -> x.vis = false);
			
			// check connectivity.
			DfsDT(cfgEntry);
			// those cannot be reached BBs are dominated by curB.
			for (BasicBlock blk : infos.keySet()) {
				// make DT strictly dominant.
				if (blk == curB) continue;
				// if cannot visit blk except curB, curB dominates blk.
				if (!infos.get(blk).vis) {
					// curB's inferior is blk.
					infos.get(curB).superiorTo.add(blk);
					// blk is superior to curB.
					infos.get(blk).inferiorTo.add(curB);
				}
			}
			curB.predecessor = in;
			curB.successor = out;
			curB = (BasicBlock) curB.next;
		}
		
		// debug
		if (SSALOG) {
			System.err.println(functName);
			print(infos, "inferiorTo");
			System.err.println();
			print(infos, "superiorTo");
			System.err.println();
		}
	}
	
	private void DfsDT(BasicBlock curB) {
		if (infos.get(curB).vis) return;
		infos.get(curB).vis = true;
		curB.successor.forEach(this::DfsDT);
	}
	
	/**
	 * Prune dominance tree, make it strictly dominance
	 */
	private void StrictDominance() {
	
	}
	
	/**
	 * ************************* IMMEDIATE DOMINANCE ***********************************/
	public void BuildImmediateDominance() {
		infos.values().forEach(x -> x.iDom = null);
		infos.values().forEach(x -> x.domTree.clear());
		
		BasicBlock curB = cfgEntry;
		while (curB != null) {
			// pick one idom out of a list of dominance.
			Set<BasicBlock> dominance = infos.get(curB).inferiorTo;
			BasicBlock iDomTmp = null;
			for (BasicBlock dom : dominance) {
				if (iDomTmp == null) {
					iDomTmp = dom;
					continue;
				}
				// idom should be inferior to any other dom.
				// dominance is a total ordering.
				if (dom == iDomTmp) continue;
				if (infos.get(iDomTmp).superiorTo.contains(dom)) {
					// if current idomTmp dominates dom, where dom dominates curB -> iDomTmp is not iDom.
					// we can safely find iDom given dominance is a total ordering.
					iDomTmp = dom;
				}
			}
			// set iDom. A BB may have no iDom.
			BasicBlock iDom = iDomTmp;
			if (iDom != null) {
				infos.get(curB).iDom = iDom;
				// construct dominance tree along the way.
				infos.get(iDom).domTree.add(curB);
			}
			// update current BB cursor.
			curB = (BasicBlock) curB.next;
		}
		
		if (SSALOG) {
			System.err.println(functName);
			print(infos, "iDom");
			System.err.println();
			print(infos, "domTree");
			System.err.println();
		}
	}
	
	/**
	 * ************************* DOMINANT FRONTIER ***********************************/
	public void DominanceFrontier() {
		infos.values().forEach(x -> x.domFrontier.clear());
		BasicBlock curB = cfgEntry;
		
		while (curB != null) {
			for (BasicBlock fath : curB.predecessor) {
				BasicBlock t = fath;
				while (t != infos.get(curB).iDom) {
					infos.get(t).domFrontier.add(curB);
					t = infos.get(t).iDom;
				}
			}
			curB = (BasicBlock) curB.next;
		}
		if (SSALOG) {
			System.err.println(functName);
			print(infos, "domFrontier");
			System.err.println();
		}
	}
	
	/**
	 * ************************* Var Use-Def collection ************
	 * Attach use-def information on Reg.
	 * Attach phi-function information on BasicBlock.
	 * */
	public void UseDefCollection() {
		BasicBlock curB = cfgEntry;
		while (curB != null) {
			for (Quad quad : curB.TraverseQuad()) {
				if (quad instanceof Alloca) {
					Reg var = ((Alloca) quad).var;
					vars.add(var);
					var.alloca = true;
					var.defsBB.add(curB);
					var.defsQuad.add(quad);
				}
				else if (quad instanceof Store) {
					// def. check whether an allocated quad.
					if (vars.contains(((Store) quad).dst)) {
						Reg var = ((Store) quad).dst;
						var.defsBB.add(curB);
						var.defsQuad.add(quad);
					}
				}
				else if (quad instanceof Load) {
					if (vars.contains(((Load) quad).addr)) {
						Reg var = ((Load) quad).addr;
						var.usesBB.add(curB);
						var.usesQuad.add(quad);
					}
				}
			}
			curB = (BasicBlock) curB.next;
		}
	}
	
	/**
	 * ************************* Phi placement *************/
	public void PhiPlacement() {
		Queue<BasicBlock> workList = new ArrayDeque<>();
		
		// insert phi function for each var.
		for (Reg var : vars) {
			// a mark for whether processed.
			infos.values().forEach(x -> x.vis = false);
			
			for (BasicBlock defB : var.defsBB) {
				// if a var is defined in the BB, we must process it.
				infos.get(defB).vis = true;
				workList.add(defB);
			}
			
			while (!workList.isEmpty()) {
				BasicBlock defB = workList.poll();
				// traverse current BB's dominant frontier, where its phi function should reside.
				// FIXME : what about phi-function pruning ?
				for (BasicBlock domFront : infos.get(defB).domFrontier) {
					// check phi-function in DF
					Map<Reg, Phi> phis = infos.get(domFront).phis;
					// if phi hasn't been added in dominance frontier.
					// add phi, and add defs.
					if (!phis.containsKey(var)) {
						// add definitions.
						Phi phi = new Phi(var);
						phis.put(var, phi);
						var.defsBB.add(domFront);
						var.defsQuad.add(phi);
						
						if (!infos.get(domFront).vis) {
							infos.get(domFront).vis = true;
							workList.add(domFront);
						}
					}
				}
			}
		}
		
		// insert phi-quad into the head of each basic block
		BasicBlock curB = cfgEntry;
		while (curB != null) {
			Map<Reg, Phi> phis = infos.get(curB).phis;
			for (Phi phi : phis.values()) {
				// FIXME : ugly
				phi.blk = curB;
				curB.PushfrontPhi(phi);
				// each phi-node is a def of var.
//				phi.var.defsQuad.add(phi);
//				phi.var.defsBB.add(curB);
			}
			curB = (BasicBlock) curB.next;
		}
	}
	
	/**
	 * ************************* VARIABLE RENAMING ************
	 * Pay attention that all versions of variable are essentially pointers
	 * to the same object instance. We have to create new copies of them for
	 * renaming purposes.
	 * */
	public void Renaming() {
		for (Reg var : vars) {
			versionStack.clear();
			ConfigNamingVar(var);
			versionStack.push(NewVersion());
			RenameVariable(var, cfgEntry);
		}
		
		if (SSALOG) {
			System.err.println(functName);
			print(infos, "phiOpts");
			System.err.println();
		}
	}
	
	/**
	 * Record SSA info during variable renaming.
	 * */
	private void RenameVariable(Reg var, BasicBlock blk) {
		List<Quad> quads = blk.TraverseQuad();
		// for def-use in this basic block.
		for (ListIterator<Quad> iter = quads.listIterator(); iter.hasNext(); ) {
			Quad quad = iter.next();
			// traverse quad for def and use.
			if (quad instanceof Load && var.usesQuad.contains(quad)) {
				Reg loaded = ((Load) quad).val;
				// remove load in both data structures.
				iter.remove();
				var.usesQuad.remove(quad);
				// update move in both data structures.
				Mov use = new Mov(loaded, versionStack.peek());
				// FIXME : ugly
				use.blk = blk;
				iter.add(use);
				var.usesQuad.add(use);
			}
			else if (quad instanceof Store && var.defsQuad.contains(quad)) {
				versionStack.push(NewVersion());
				IrValue storing = ((Store) quad).src;
				// remove
				iter.remove();
				var.defsQuad.remove(quad);
				// update
				Mov def = new Mov(versionStack.peek(), storing);
				// FIXME : ugly
				def.blk = blk;
				iter.add(def);
				var.defsQuad.add(def);
			}
			else if (quad instanceof Phi && var.defsQuad.contains(quad)) {
				// phi defines a new version
				versionStack.push(NewVersion());
				((Phi) quad).var = versionStack.peek();
			}
			else if (quad instanceof Alloca && var.defsQuad.contains(quad)) {
				// remove Alloca quad
				// if current basic block doesn't have any other def of this variable, insert a trivial dummy def.
				iter.remove();
				versionStack.push(NewVersion());
				Mov def = new Mov(versionStack.peek(), new Constant(DUMMY));
				// FIXME : ugly
				def.blk = blk;
				iter.add(def);
			}
		}
		
		// for phi node, investigate successors
		for (BasicBlock scs : blk.successor) {
			// check whether scs has a phi-quad w.r.t current var.
			Map<Reg, Phi> phis = infos.get(scs).phis;
			if (phis.containsKey(var)) {
				// if it has an entry from current blk.
				Phi phi = phis.get(var);
				if (!phi.options.containsKey(blk)) {
					// replace the use of homogeneous 'Var' with current version.
					phi.options.put(blk, versionStack.peek());
				}
			}
		}
		
		infos.get(blk).domTree.forEach(idom -> RenameVariable(var, idom));
		for (ListIterator<Quad> iter = quads.listIterator(); iter.hasNext(); ) {
			Quad quad = iter.next();
			if (var.defsQuad.contains(quad))
				versionStack.pop();
		}
	}

	/**
	 * Make sure that all allocated regs have been renamed.
	 * */
	private void CheckRename() {
		BasicBlock curB = cfgEntry;
		List<Quad> quads = curB.TraverseQuad();
		for (Quad quad : quads) {
			if (quad instanceof Store) {
				Reg bug = ((Store) quad).dst;
				assert bug.alloca == bug.renamed;
			}
			else if (quad instanceof Load) {
				Reg bug = ((Load) quad).val;
				assert bug.alloca == bug.renamed;
			}
			else if (quad instanceof Phi) {
				Reg bug = ((Phi) quad).var;
				assert bug.renamed && bug.alloca;
				for (IrValue bugsV : ((Phi) quad).options.values()) {
					Reg bugs = (Reg) bugsV;
					assert bugs.renamed && bugs.alloca;
				}
			}
			else if (quad instanceof Alloca) {
				assert false;
			}
		}
	}
	
	private void print(HashMap<BasicBlock, Info> infos, String mode) {
		switch (mode) {
			case "inferiorTo":
				System.err.println("inferiorTo");
				for (BasicBlock blk : infos.keySet()) {
					System.err.print(blk.getName() + ":");
					infos.get(blk).inferiorTo.forEach((x -> System.err.print(" " + x.getName())));
					System.err.println();
				}
				break;
				
			case "superiorTo":
				System.err.println("superiorTo");
				for (BasicBlock blk : infos.keySet()) {
					System.err.print(blk.getName() + ":");
					infos.get(blk).superiorTo.forEach((x -> System.err.print(" " + x.getName())));
					System.err.println();
				}
				break;
				
			case "iDom":
				System.err.println("iDom");
				for (BasicBlock blk : infos.keySet()) {
					System.err.print(blk.getName() + ":");
					BasicBlock iDom = infos.get(blk).iDom;
					if (iDom != null)
						System.err.print(" " + iDom.getName());
					System.err.println();
				}
				break;
			case "domTree":
				System.err.println("domTree");
				for (BasicBlock blk : infos.keySet()) {
					System.err.print(blk.getName() + ":");
					infos.get(blk).domTree.forEach((x -> System.err.print(" " + x.getName())));
					System.err.println();
				}
				break;
			case "domFrontier":
				System.err.println("domFrontier");
				for (BasicBlock blk : infos.keySet()) {
					System.err.print(blk.getName() + ":");
					infos.get(blk).domFrontier.forEach((x -> System.err.print(" " + x.getName())));
					System.err.println();
				}
				break;
			case "phiOpts":
				System.err.println("phiOpts");
				for (Reg var : vars) {
					for (BasicBlock blk : infos.keySet()) {
						if (infos.get(blk).phis.containsKey(var)) {
							Phi phi = infos.get(blk).phis.get(var);
							System.err.print(var.getText() + "--" + blk.getName() + ":");
							phi.options.forEach((x, y) -> System.err.print(" " + x.getName() + " " + y.getText() + ";"));
							System.err.println();
						}
					}
				}
				break;
		}
	}
}