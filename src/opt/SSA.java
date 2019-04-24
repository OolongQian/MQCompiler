package opt;

import ir.IrProg;
import ir.quad.*;
import ir.structure.*;
import opt.optimizers.*;

import java.util.*;

import static config.Config.NULL;
import static config.Config.SSALOG;

// SSA transformation is done for one function at a time, let's make it compact.
// transformation consists of a series of passes, we have to config the IrFunct.
public class SSA {
	
	public void BuildSSA(IrProg ir) {
		for (IrFunct funct : ir.functs.values()) {
			BuildConfig(funct);
			BuildDominance();
			BuildImmediateDominance();
			DominanceFrontier();
			UseDefCollection();
			PhiPlacement();
			Renaming();
		}
	}

	public void OptimSSA(IrProg ir) {
		ir.functs.values().forEach(Defuse::CollectFunctDefuse);
		
		for (int i = 0; i < 5; ++i) {
			DeadEliminator dead = new DeadEliminator();
			dead.DeadCodeEliminate();

			ConstPropagator conster = new ConstPropagator();
			conster.ConstPropagate();

			CommonExprDeleter expr = new CommonExprDeleter();
			expr.WipeCommonExpr();
		}
		
		CopyPropagator copy = new CopyPropagator();
		copy.PropagateCopy();
	}
	
	/************************ config.Config a CFG to optimize ************************/
	private IrFunct cFun;
	private HashMap<BasicBlock, GraphInfo> gInfos = new HashMap<>();
	

	
	public void BuildConfig(IrFunct funct) {
		this.cFun = funct;
		
		gInfos.clear();
		vars.clear();
		
		BasicBlock curB = cFun.bbs.list.Head();
		while (curB != null) {
			gInfos.put(curB, new GraphInfo());
			curB = curB.next;
		}
	}
	
	/**
	 * ************************* DOMINANT TREE **********************************
	 * Build dominant tree for a List_ of basic blocks.
	 */
	public void BuildDominance() {
		Set<BasicBlock> empty = new HashSet<>();
		
		BasicBlock entry = cFun.bbs.list.Head();
		BasicBlock curB = entry;
		while (curB != null) {
			// clear each BB.
			Set<BasicBlock> in = Predecessors(curB);
			Set<BasicBlock> out = Successors(curB);
			
			SetPredecessors(curB, empty);
			SetSuccessors(curB, empty);
			
			// reset marker.
			gInfos.values().forEach(x -> x.vis = false);
			
			// check connectivity.
			DfsDT(entry);
			// those cannot be reached BBs are dominated by curB.
			for (BasicBlock blk : gInfos.keySet()) {
				// make DT strictly dominant.
				if (blk == curB) continue;
				// if cannot visit blk except curB, curB dominates blk.
				if (!gInfos.get(blk).vis) {
					// curB's inferior is blk.
					gInfos.get(curB).superiorTo.add(blk);
					// blk is superior to curB.
					gInfos.get(blk).inferiorTo.add(curB);
				}
			}
			
			SetPredecessors(curB, in);
			SetSuccessors(curB, out);
			curB = curB.next;
		}
	}
	
	private void DfsDT(BasicBlock curB) {
		if (gInfos.get(curB).vis) return;
		gInfos.get(curB).vis = true;
		Successors(curB).forEach(this::DfsDT);
	}
	
	
	/**
	 * ************************* IMMEDIATE DOMINANCE ***********************************/
	public void BuildImmediateDominance() {
		gInfos.values().forEach(x -> x.iDom = null);
		gInfos.values().forEach(x -> x.domTree.clear());
		
		BasicBlock curB = cFun.bbs.list.Head();
		while (curB != null) {
			// pick one idom out of a list of dominance.
			Set<BasicBlock> dominance = gInfos.get(curB).inferiorTo;
			BasicBlock iDomTmp = null;
			for (BasicBlock dom : dominance) {
				if (iDomTmp == null) {
					iDomTmp = dom;
					continue;
				}
				// idom should be inferior to any other dom.
				// dominance is a total ordering.
				if (dom == iDomTmp) continue;
				if (gInfos.get(iDomTmp).superiorTo.contains(dom)) {
					// if current idomTmp dominates dom, where dom dominates curB -> iDomTmp is not iDom.
					// we can safely find iDom given dominance is a total ordering.
					iDomTmp = dom;
				}
			}
			// set iDom. A BB may have no iDom.
			BasicBlock iDom = iDomTmp;
			if (iDom != null) {
				gInfos.get(curB).iDom = iDom;
				// construct dominance tree along the way.
				gInfos.get(iDom).domTree.add(curB);
			}
			// update current BB cursor.
			curB = curB.next;
		}
		
		if (SSALOG) {
			System.err.println(cFun.name);
			print(gInfos, "iDom");
			System.err.println();
			print(gInfos, "domTree");
			System.err.println();
		}
	}
	
	/**
	 * ************************* DOMINANT FRONTIER ***********************************/
	public void DominanceFrontier() {
		gInfos.values().forEach(x -> x.domFrontier.clear());
		BasicBlock curB = cFun.bbs.list.Head();
		
		while (curB != null) {
			for (BasicBlock fath : Predecessors(curB)) {
				BasicBlock t = fath;
				while (t != gInfos.get(curB).iDom) {
					gInfos.get(t).domFrontier.add(curB);
					t = gInfos.get(t).iDom;
				}
			}
			curB = curB.next;
		}
		if (SSALOG) {
			System.err.println(cFun.name);
			print(gInfos, "domFrontier");
			System.err.println();
		}
	}
	
	/**
	 * ************************* Var Use-Def collection ************
	 * Attach use-def information on Reg.
	 * Attach phi-function information on BasicBlock.
	 * */
	
	// vars are data structures in functional level, instead of basic block.
	// variable info recorder for SSA construction.
	public Set<Reg> vars = new HashSet<>();
	public Stack<Reg> versionStack = new Stack<>();
	public int versionCnt = 0;
	private String varName;
	
	public void ConfigNamingVar(Reg var) {
		versionStack.clear();
		varName = var.name;
		versionCnt = 0; // SSA version begins from 0. Add a temp version with index -1.
	}
	
	// FIXME : remove ! later.
	public Reg NewVersion() {
		Reg newVer = new Reg('!' + varName + '[' + versionCnt++ + ']');
		return newVer;
	}
	
	public void UseDefCollection() {
		BasicBlock curB = cFun.bbs.list.Head();
		while (curB != null) {
			for (Quad quad : curB.quads) {
				if (quad instanceof Alloca) {
					Reg var = ((Alloca) quad).var;
					vars.add(var);
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
			curB = curB.next;
		}
	}
	
	
	/**
	 * ************************* Phi placement *************/
	public void PhiPlacement() {
		Queue<BasicBlock> workList = new ArrayDeque<>();
		
		// insert phi function for each var.
		for (Reg var : vars) {
			// a mark for whether processed.
			gInfos.values().forEach(x -> x.vis = false);
			
			for (BasicBlock defB : var.defsBB) {
				// if a var is defined in the BB, we must process it.
				gInfos.get(defB).vis = true;
				workList.add(defB);
			}
			
			while (!workList.isEmpty()) {
				BasicBlock defB = workList.poll();
				// traverse current BB's dominant frontier, where its phi function should reside.
				// FIXME : what about phi-function pruning ?
				for (BasicBlock domFront : gInfos.get(defB).domFrontier) {
					// check phi-function in DF
					Map<Reg, Phi> phis = gInfos.get(domFront).phis;
					// if phi hasn't been added in dominance frontier.
					// add phi, and add defs.
					if (!phis.containsKey(var)) {
						// add definitions.
						Phi phi = new Phi(var);
						phis.put(var, phi);
						var.defsBB.add(domFront);
						var.defsQuad.add(phi);
						
						if (!gInfos.get(domFront).vis) {
							gInfos.get(domFront).vis = true;
							workList.add(domFront);
						}
					}
				}
			}
		}
		
		// insert phi-quad into the head of each basic block
		BasicBlock curB = cFun.bbs.list.Head();
		while (curB != null) {
			Map<Reg, Phi> phis = gInfos.get(curB).phis;
			for (Phi phi : phis.values()) {
				// FIXME : ugly
				phi.blk = curB;
				curB.quads.add(0, phi);
				// each phi-node is a def of var.
//				phi.var.defsQuad.add(phi);
//				phi.var.defsBB.add(curB);
			}
			curB = curB.next;
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
			RenameVariable(var, cFun.bbs.list.Head());
		}
		
		if (SSALOG) {
			System.err.println(cFun.name);
			print(gInfos, "phiOpts");
			System.err.println();
		}
	}
	
	
	/** Record SSA info during variable renaming. */
	private void RenameVariable(Reg var, BasicBlock blk) {
		List<Quad> quads = blk.quads;
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
				assert versionStack.size() != 1;
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
				// FIXME : don't add dummy entry def.
				versionStack.push(NewVersion());
				Mov def = new Mov(versionStack.peek(), new Constant(NULL));
				// FIXME : ugly
				def.blk = blk;
				iter.add(def);
			}
		}
		
		// for phi node, investigate successors
		for (BasicBlock scs : Successors(blk)) {
			// check whether scs has a phi-quad w.r.t current var.
			Map<Reg, Phi> phis = gInfos.get(scs).phis;
			if (phis.containsKey(var)) {
				// if it has an entry from current blk.
				Phi phi = phis.get(var);
				if (!phi.options.containsKey(blk)) {
					// replace the use of homogeneous 'Var' with current version.
					phi.options.put(blk, versionStack.peek());
				}
			}
		}
		
		gInfos.get(blk).domTree.forEach(idom -> RenameVariable(var, idom));
		for (ListIterator<Quad> iter = quads.listIterator(); iter.hasNext(); ) {
			Quad quad = iter.next();
			if (var.defsQuad.contains(quad))
				versionStack.pop();
		}
	}
	
	
	/*********************** SSA destruction ********************************/
	public void DestructSSA(IrProg ir) {
		for (IrFunct funct : ir.functs.values()) {
			DestructConfig(funct);
			SplitAndCopy();
		}
	}
	
	private void DestructConfig(IrFunct funct) {
		cFun = funct;
	}
	
	private void SplitAndCopy() {
		BasicBlock entry = cFun.bbs.list.Head();
		BasicBlock cur = entry;
		
		Queue<BasicBlock> workList = new ArrayDeque<>();
		while (cur != null) {
			workList.add(cur);
			cur = cur.next;
		}
		
		while (!workList.isEmpty()) {
			cur = workList.remove();
			// check if current BB has phi function.
			boolean hasPhi = false;
			for (Quad quad : cur.quads)
				if (quad instanceof Phi) {
					hasPhi = true;
					break;
				}
			if (!hasPhi) continue;
			
			// store phi-sources and parallel copy contents' association.
			Map<BasicBlock, ParallelCopy> splitsCopies = new HashMap<>();
			
			// create split for each predecessors.
			Set<BasicBlock> preds = Predecessors(cur);
			Queue<BasicBlock> predsList = new ArrayDeque<>(preds);
			
			while (!predsList.isEmpty()) {
				BasicBlock pred = predsList.remove();
				// if pred has multiple outgoing edges.
				Set<BasicBlock> suces = Successors(pred);
				assert suces.contains(cur);
				
				if (suces.size() > 1) {
					BasicBlock split = CreateSplit(pred, cur, splitsCopies);
					// maintain list structure.
					cFun.bbs.list.InsertAfter(pred, split);
					// maintain cfg.
					preds.remove(pred);
					preds.add(split);
					suces.remove(cur);
					suces.add(split);
				}
				// if there's no multiple predecessor, just append parallel copy at the tail of the single pred,
				// instead of split insertion.
				else {
					ParallelCopy copy = new ParallelCopy();
					copy.blk = pred;
					// the last quad of predecessor is jump, branch, ret. insert before it.
					Quad last = pred.quads.get(pred.quads.size() - 1);
					if (last instanceof Jump || last instanceof Branch || last instanceof Ret) {
						pred.quads.add(pred.quads.size() - 1, copy);
					} else {
						pred.quads.add(copy);
					}
					splitsCopies.put(pred, copy);
				}
			}
			
			// start evaluating phi nodes.
			// load phi dst to get phi result.
			Queue<Phi> phisList = new ArrayDeque<>();
			for (Quad quad : cur.quads) {
				if (quad instanceof Phi)
					phisList.add((Phi) quad);
			}
			while (!phisList.isEmpty()) {
				// alloca a mem address for the phi to load.
				Phi phi = phisList.remove();
				// note : we turn phi to mov, and insert mov at suitable places.
//				Reg phiDst = ReplacePhiByLoad(phi);
				// remove phi node.
				// and don't have to load here, because phi destination has been copied in predecessors.
				phi.blk.quads.remove(phi);
				// insert moves in predecessors in the split parallel copies.
				Reg phiDst = phi.var;
				InsertMoves (splitsCopies, phiDst, phi);
//				InsertCopies(splitsCopies, phiDst, phi);
			}
		}
		SequentializeParallelCopies();
	}
	
	// a simple approach.
	private void SequentializeParallelCopies() {
		BasicBlock cur = cFun.bbs.list.Head();
		Queue<ParallelCopy> copies = new ArrayDeque<>();
		
		while (cur != null) {
			for (Quad quad : cur.quads)
				if (quad instanceof ParallelCopy)
					copies.add((ParallelCopy) quad);
			cur = cur.next;
		}
		
		// transform previous simple approach by parallel copy sequentialization.
		// note : here we sequentialize the pcopy.
		while (!copies.isEmpty()) {
			ParallelCopy pcopy = copies.remove();
			cur = pcopy.blk;
			
			List<Mov> seq = new LinkedList<>();
			
			while (!AllTrivialCopy(pcopy)) {
				Mov copy = null;
				for (Mov move : pcopy.copies)
					if (!TrivialCopy(move) && !ExistNonTrivialDstuse(pcopy, move)) {
						copy = move;
						break;
					}
				if (copy != null) {
					// exist, append to seq.
					seq.add(copy);
					pcopy.copies.remove(copy);
				} else {
					// pcopy is only made-up of cycles; break one of them.
					// not a <- a, we need a <- b
					Mov nonself = null;
					for (Mov move : pcopy.copies)
						if (!TrivialCopy(move)) {
							nonself = move;
							break;
						}
					
					assert nonself != null;
					assert nonself.src instanceof Reg;
					Reg brkCycle = new Reg(String.format("clc_%s_%s", ((Reg) nonself.src).name, cFun.name));
					// create a' <- a.
					Mov loopBrker = new Mov(brkCycle, nonself.src);
					loopBrker.blk = cur;
					seq.add(loopBrker);
					// replace b <- a into b <- a' in pcopy.
					nonself.src = brkCycle;
				}
			}
			
			// ok, now add sequentialized copies.
			int pcopyIndex = cur.quads.indexOf(pcopy);
			for (Mov copy : seq)
				cur.quads.add(pcopyIndex++, copy);
			cur.quads.remove(pcopy);
		}
		/*
		while (!copies.isEmpty()) {
			ParallelCopy pcopy = copies.remove();
			cur = pcopy.blk;
			int pcopyIndex = cur.quads.indexOf(pcopy);
			// FIXME : stores in pcopy has been updated to moves.
//			for (Store copy : (pcopy).copies)
//				cur.quads.add(pcopyIndex+1, copy);
			cur.quads.remove(pcopy);
		}
		*/
	}
	
	// logic methods used by copy sequentialization.
	private boolean ExistNonTrivialDstuse (ParallelCopy pcopy, Mov move) {
		assert !TrivialCopy(move);
		// this copy is b <- a. Check there doesn't exist c <- b.
		for (Mov mov : pcopy.copies) {
			if (mov.src instanceof Reg &&
							((Reg) mov.src).name.equals(move.dst.name) && // use b
							!TrivialCopy(mov))  // not trivial
				return true;
		}
		return false;
	}
	
	private boolean AllTrivialCopy (ParallelCopy pcopy) {
		for (Mov copy : pcopy.copies)
			if (!TrivialCopy(copy))
				return false;
		return true;
	}
	
	private boolean TrivialCopy (Mov copy) {
		return copy.src instanceof Reg && copy.dst.name.equals(((Reg) copy.src).name);
	}
	
	// create basic block and change quads in corresponding BB.
	private BasicBlock CreateSplit(BasicBlock from, BasicBlock to, Map<BasicBlock, ParallelCopy> copies) {
		assert from.loopLevel != null;
		BasicBlock split = new BasicBlock(cFun.GetBBName("split"), cFun, from.loopLevel);
		// create copy.
		ParallelCopy copy = new ParallelCopy();
		copy.blk = split;
		split.quads.add(copy);
		copies.put(from, copy);
		
		// maintain CFG control flow.
		// add default jump to to.
		Jump jp = new Jump(to);
		jp.blk = split;
		split.quads.add(jp);
		
		// insert split, change control target in quads, change cfg.
		Quad last = from.quads.get(from.quads.size() - 1);
		assert last instanceof Jump || last instanceof Branch;
		if (last instanceof Jump)
			ReplaceJump((Jump) last, to, split);
		else
			ReplaceBranch((Branch) last, to, split);
		return split;
	}
	
	// add copies in predecessors or splits
	private void InsertCopies(Map<BasicBlock, ParallelCopy> copies, Reg phiDst, Phi phi) {
		for (BasicBlock from : phi.options.keySet()) {
			// adopt a simple approach here, we do store and load.
			// they are temp pseudo quads in parallel copy, assign no parent block.
			Store phiAssign = new Store(phiDst, phi.options.get(from));
			phiAssign.blk = from;
//						Mov move = new Mov(phi.var, phi.options.get(from));
//						move.blk = null;
			assert copies.containsKey(from);
			// NOTE : parallel copy's pcopy data structure has been changed to Set<Mov> instead of Set<Store>.
//			copies.get(from).copies.add(phiAssign);
		}
	}
	
	private void InsertMoves (Map<BasicBlock, ParallelCopy> copies, Reg phiDst, Phi phi) {
		for (BasicBlock from : phi.options.keySet()) {
			// create 'move' for phi node.
			Mov phiMove = new Mov(phiDst, phi.options.get(from));
			phiMove.blk = from;
			// add these moves to predecessors' pcopy via 'copies' map.
			// pcopies will be sequentialized later on.
			assert copies.containsKey(from);
			copies.get(from).copies.add(phiMove);
		}
	}
	// alloca a mem addr for phi to load
	private Reg ReplacePhiByLoad(Phi phi) {
		BasicBlock entry = cFun.bbs.list.Head();
		BasicBlock cur = phi.blk;

		// create a new phiDst, and insert an alloca.
		Reg phiDst = cFun.GetReserveReg(phi.var.name + "phi_dst");
		Alloca phiAlloca = new Alloca(phiDst);
		phiAlloca.blk = entry;
		entry.quads.add(0, phiAlloca);
		
		// replace phi node by a load.
		cur.quads.remove(phi);
		Load phiAns = new Load(phi.var, phiDst);
		phiAns.blk = cur;
		cur.quads.add(0, phiAns);
		
		return phiDst;
	}
	// simple lexical change
	private void ReplaceJump(Jump jp, BasicBlock old, BasicBlock new_) {
		assert jp.target == old;
		jp.target = new_;
	}
	private void ReplaceBranch(Branch br, BasicBlock old, BasicBlock new_) {
		assert br.ifTrue == old || br.ifFalse == old;
		if (br.ifTrue == old)
			br.ifTrue = new_;
		if (br.ifFalse == old)
			br.ifFalse = new_;
	}
	
	
	
	/************************* Utilities for simplicity *************************/
	public Set<BasicBlock> Predecessors(BasicBlock blk) {
		return cFun.bbs.cfg.predesessors.get(blk);
	}
	public Set<BasicBlock> Successors(BasicBlock blk) {
		return cFun.bbs.cfg.successors.get(blk);
	}
	public void SetPredecessors(BasicBlock blk, Set<BasicBlock> preds) {
		cFun.bbs.cfg.predesessors.put(blk, preds);
	}
	public void SetSuccessors(BasicBlock blk, Set<BasicBlock> sucses) {
		cFun.bbs.cfg.successors.put(blk, sucses);
	}
	
	private void print(HashMap<BasicBlock, GraphInfo> infos, String mode) {
		switch (mode) {
			case "inferiorTo":
				System.err.println("inferiorTo");
				for (BasicBlock blk : infos.keySet()) {
					System.err.print(blk.name + ":");
					infos.get(blk).inferiorTo.forEach((x -> System.err.print(" " + x.name)));
					System.err.println();
				}
				break;
			
			case "superiorTo":
				System.err.println("superiorTo");
				for (BasicBlock blk : infos.keySet()) {
					System.err.print(blk.name + ":");
					infos.get(blk).superiorTo.forEach((x -> System.err.print(" " + x.name)));
					System.err.println();
				}
				break;
			
			case "iDom":
				System.err.println("iDom");
				for (BasicBlock blk : infos.keySet()) {
					System.err.print(blk.name + ":");
					BasicBlock iDom = infos.get(blk).iDom;
					if (iDom != null)
						System.err.print(" " + iDom.name);
					System.err.println();
				}
				break;
			case "domTree":
				System.err.println("domTree");
				for (BasicBlock blk : infos.keySet()) {
					System.err.print(blk.name + ":");
					infos.get(blk).domTree.forEach((x -> System.err.print(" " + x.name)));
					System.err.println();
				}
				break;
			case "domFrontier":
				System.err.println("domFrontier");
				for (BasicBlock blk : infos.keySet()) {
					System.err.print(blk.name + ":");
					infos.get(blk).domFrontier.forEach((x -> System.err.print(" " + x.name)));
					System.err.println();
				}
				break;
			case "phiOpts":
				System.err.println("phiOpts");
//				for (Reg var : vars) {
//					for (BasicBlock blk : infos.keySet()) {
//						if (infos.get(blk).phis.containsKey(var)) {
//							Phi phi = infos.get(blk).phis.get(var);
//							System.err.print(var.getText() + "--" + blk.getName() + ":");
//							phi.options.forEach((x, y) -> System.err.print(" " + x.name + " " + y.getText() + ";"));
//							System.err.println();
//						}
//					}
//				}
				break;
		}
	}
}
