package opt;

import ir.structure.BasicBlock;
import ir.structure.CFG;
import ir.structure.ListBB;

import java.util.*;


public class DominanceBuilder {
	private HashMap<BasicBlock, GraphInfo> gInfos = new HashMap<>();
	private CFG cfg;
	private ListBB list;
	private List<BasicBlock> entries = new LinkedList<>();
	
	public HashMap<BasicBlock, GraphInfo> getgInfos() {
		return gInfos;
	}
	
	public void BuildConfig(ListBB list, CFG cfg) {
		this.cfg = cfg;
		this.list = list;
		gInfos.clear();
		entries.clear();
		
		// entry node is the one without predecessors.
		for (BasicBlock cur = list.Head(); cur != null; cur = cur.next) {
			gInfos.put(cur, new GraphInfo());
			if (cfg.predesessors.get(cur).size() == 0)
				entries.add(cur);
		}
	}
	
	/**
	 * ************************* DOMINANT TREE **********************************
	 * Build dominant tree for a List_ of basic blocks.
	 */
	public void BuildDominance() {
		Set<BasicBlock> empty = new HashSet<>();
		
		BasicBlock curB = list.Head();
		while (curB != null) {
			// clear each BB.
			Set<BasicBlock> in = Predecessors(curB);
			Set<BasicBlock> out = Successors(curB);
			
			SetPredecessors(curB, empty);
			SetSuccessors(curB, empty);
			
			// reset marker.
			gInfos.values().forEach(x -> x.vis = false);
			
			// check connectivity.
			entries.forEach(this::DfsDT);
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
		if (!gInfos.containsKey(curB)) {
			int a = 1;
		}
		if (gInfos.get(curB).vis) return;
		gInfos.get(curB).vis = true;
		Successors(curB).forEach(this::DfsDT);
	}
	
	
	/**
	 * ************************* IMMEDIATE DOMINANCE ***********************************/
	public void BuildImmediateDominance() {
		gInfos.values().forEach(x -> x.iDom = null);
		gInfos.values().forEach(x -> x.domTree.clear());
		
		BasicBlock curB = list.Head();
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
	}
	
	
	/**
	 * ************************* DOMINANT FRONTIER ***********************************/
	public void DominanceFrontier() {
		gInfos.values().forEach(x -> x.domFrontier.clear());
		BasicBlock curB = list.Head();
		
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
	}
	
	private Set<BasicBlock> Predecessors(BasicBlock blk) {
		return cfg.predesessors.get(blk);
	}
	private Set<BasicBlock> Successors(BasicBlock blk) {
		return cfg.successors.get(blk);
	}
	private void SetPredecessors(BasicBlock blk, Set<BasicBlock> preds) {
		cfg.predesessors.put(blk, preds);
	}
	private void SetSuccessors(BasicBlock blk, Set<BasicBlock> sucses) {
		cfg.successors.put(blk, sucses);
	}
	
	
	public void PrintSupInf() {
		System.out.println("supinf for " + gInfos.keySet().iterator().next().parentFunct.name);
		for (BasicBlock key : gInfos.keySet()) {
			System.out.println(key.name);
			System.out.print(">> sup ");
			for (BasicBlock sup : gInfos.get(key).superiorTo)
				System.out.print(sup.name + " ");
			System.out.println();
			System.out.print(">> inf ");
			for (BasicBlock inf : gInfos.get(key).inferiorTo)
				System.out.print(inf.name + " ");
			System.out.println();
			System.out.println();
		}
	}
}
