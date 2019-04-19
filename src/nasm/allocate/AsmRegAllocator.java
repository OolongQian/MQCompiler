package nasm.allocate;

import nasm.AsmBB;
import nasm.AsmFunct;
import nasm.inst.*;
import nasm.live.LivenessAnalysis;
import nasm.reg.PhysicalReg;
import nasm.reg.Reg;
import nasm.reg.StackMem;

import java.io.FileNotFoundException;
import java.util.*;

import static config.Config.DEBUGPRINT_INTERFERE;
import static config.Config.DEBUGPRINT_INTERFERE_GRAPHVIZ;
import static config.Config.DEBUGPRINT_LIVENESS;
import static nasm.Utils.*;
import static nasm.reg.PhysicalReg.REGNUM;

/**
 * Note that register allocator serves the entire determination of which register goes to mem,
 * including the stack argument passing in stack frame.
 *
 * This temp allocator glues separate registers together, which may be inconsistent.
 * It spills virtual registers by creating StackMem (put it on stack), and mov mem mem is handled
 * by offsetBackFill.
 * */
// allocate register in function level.
public class AsmRegAllocator {
	
	private LivenessAnalysis liveAnalyzer;
	
	private AsmAllocateContext ctx;
	
	
	/**
	 * Allocate registers for nasm assembly, and record all preeped on stack variables.
	 */
	AsmFunct curf;
	
	public void AllocateRegister(AsmFunct asmFunct) {
		ctx = new AsmAllocateContext();
		liveAnalyzer = new LivenessAnalysis();
		liveAnalyzer.ConfigLiveOut(ctx.liveOuts);
		
		curf = asmFunct;
		ScanPreColoredAndInitial();
		liveAnalyzer.LivenessAnalyze(curf);
			if (DEBUGPRINT_LIVENESS)
				liveAnalyzer.PrintLiveness();
		BuildInterference();
			if (DEBUGPRINT_INTERFERE)
				ctx.PrintGraph(curf);
			try {
				if (DEBUGPRINT_INTERFERE_GRAPHVIZ)
					ctx.Graphviz(curf);
			} catch (FileNotFoundException e) {
				throw new RuntimeException();
			}
		MakeWorklists();
		
		do {
			if (!ctx.simplifyWorklist.isEmpty())
				Simplify();
			else if (!ctx.worklistMoves.isEmpty())
				Coalesce();
			else if (!ctx.freezeWorklist.isEmpty())
				Freeze();
			else if (!ctx.spillWorklist.isEmpty())
				SelectSpill();
			
		} while (!ctx.simplifyWorklist.isEmpty() ||
						 !ctx.worklistMoves.isEmpty() ||
						 !ctx.freezeWorklist.isEmpty() ||
						 !ctx.spillWorklist.isEmpty());
		
		AssignColors();
		
		if (!ctx.spilledNodes.isEmpty()) {
			RewriteProgram();
			AllocateRegister(asmFunct);
		}
		
		// attach color map in allocateContext to Reg in nasm instructions.
		AttachColor2Reg();
	}
	
	// scan pre-colored virtual register and record them into context.
	// collect all temporary registers, not pre-colored and not yet processed.
	private void ScanPreColoredAndInitial() {
		// tmp list for registers.
		for (AsmBB bb : curf.bbs)
			for (Inst inst : bb.insts) {
				for (Reg vreg : GetVregs(inst)) {
					if (vreg.isColored()) {
						ctx.precolored.put(vreg.hintName, vreg.color.phyReg);
						ctx.colors.put(vreg.hintName, vreg.color.phyReg);
					}
					else
						ctx.initial.add(vreg.hintName);
				}
			}
	}

	// traverse quad in reverse order. Every time a virtual register is defined, it interferes
	// with other current living virtual registers.
	// some subtle details are present in this pseudo code, but we don't care.
	private void BuildInterference() {
		for (AsmBB bb : curf.bbs) {
			Set<String> live = ctx.liveOuts.get(bb);
			
			// reverse traversal current bb.
			for (int i = bb.insts.size() - 1; i >= 0; --i) {
				// for mov instruction, it doesn't actually create interfere.
				Inst inst = bb.insts.get(i);
				
				Set<String> use = new HashSet<>();
				GetUses(inst).forEach(x -> use.add(x.hintName));
				Set<String> def = new HashSet<>();
				GetDefs(inst).forEach(x -> def.add(x.hintName));
				
				if (inst instanceof Mov) {
					// remove use(inst) if this is a virtual register.
					live.removeIf(use::contains);
					// add dst and src to movelist.
					if (inst.dst instanceof Reg)
						ctx.AddMovelist(((Reg) inst.dst).hintName, (Mov) inst);
					if (inst.src instanceof Reg)
						ctx.AddMovelist(((Reg) inst.src).hintName, (Mov) inst);
					// add current move to moves enabled for possible coalescing.
					if (inst.dst instanceof Reg && inst.src instanceof Reg)
						ctx.worklistMoves.add((Mov) inst);
				}
				// add defined virtual register into live, it's just for interfere consistency.
				live.addAll(def);
				
				// defined virtual register interferes with all living virtual register.
				for (String d : def)
					for (String l : live)
						AddInterEdge(d, l);
				
				live.removeIf(def::contains);
				live.addAll(use);
			}
		}
	}

	// dispatch waiting-for-processed virtual registers to spillWorklist, freezeWorklist, simplifyWorklist.
	private void MakeWorklists() {
		for (Iterator<String> iter = ctx.initial.iterator(); iter.hasNext(); ) {
			String vreg = iter.next();
			
			if (ctx.itfg.GetDegree(vreg) >= REGNUM)
				ctx.spillWorklist.add(vreg);
			else if (MoveRelated(vreg))
				ctx.freezeWorklist.add(vreg);
			else
				ctx.simplifyWorklist.add(vreg);
		}
	}
	
	private void Simplify () {
		Iterator<String> iter = ctx.simplifyWorklist.iterator();
		assert iter.hasNext();
		// pick a virtual register from simplify worklist.
		String vreg = iter.next();
		iter.remove();
		// push it to stack.
		ctx.selectStack.push(vreg);
		// remove this vregs impact on interference graph. Release neighbors for simplification or
		// grant them other benefits.
		// but the key is that interference graph itself has to be maintained, because the pushed
		// virtual registers will be popped for actual spill detection.
		// separate degree and actual graph layout structure.
		for (String adj : Adjacent(vreg))
			DecrementDegree(adj);
	}
	
	private void Coalesce() {
		Iterator<Mov> iter = ctx.worklistMoves.iterator();
		assert iter.hasNext();
		
		Mov mov = iter.next();
		iter.remove();
		
		String x = GetAlias(((Reg) mov.dst).hintName);
		String y = GetAlias(((Reg) mov.src).hintName);
		// make pre-colored node 'u'
		String u, v;
		if (ctx.IsPreColored(y)) {
			u = y; v = x;
		} else {
			u = x; v = y;
		}

		if (u.equals(v)) {
			ctx.coalescedMoves.add(mov);
			AddWorklist(u);
		}
		else if (ctx.IsPreColored(v) || ctx.itfg.HasAdjSet(u, v)){
			ctx.constainedMoves.add(mov);
			AddWorklist(u);
			AddWorklist(v);
		}
		// this is a complicated one.
		else {
			// first 'and'
			boolean cond_1;
			boolean forall_true;
			boolean exist_false = false;
			//    forall
			for (String t : Adjacent(v))
				if (!OK(t, u)) exist_false = true;
			forall_true = !exist_false;
			cond_1 = ctx.IsPreColored(u) && forall_true;
			// second 'and'
			Set<String> unionAdj = Adjacent(u); unionAdj.addAll(Adjacent(v));
			boolean cond_2 = !ctx.IsPreColored(u) && Conservative(unionAdj);
			
			boolean flag = cond_1 || cond_2;
			if (flag) {
				// coalesce them !
 				ctx.coalescedMoves.add(mov);
				Combine(u, v);
				AddWorklist(u);
			}
			else
				ctx.activeMoves.add(mov);
		}
	}
	
	// sub-function used by Coalesce.
	// combine two nodes in interference graph.
	private void Combine (String u, String v) {
		if (ctx.freezeWorklist.contains(v))
			ctx.freezeWorklist.remove(v);
		else
			ctx.spillWorklist.remove(v);
		
		ctx.coalescedNodes.add(v);
		assert !ctx.alias.containsKey(v);
		ctx.alias.put(v, u);
		
		ctx.GetMovelist(u).addAll(ctx.GetMovelist(v));
		
		Set<String> tmp = new HashSet<>(); tmp.add(v);
		EnableMoves(tmp);
		
		for (String t : Adjacent(v)) {
			AddInterEdge(t, u);
			DecrementDegree(t);
		}
		
		if (ctx.itfg.GetDegree(u) >= REGNUM && ctx.freezeWorklist.contains(u)) {
			ctx.freezeWorklist.remove(u);
			ctx.spillWorklist.add(u);
		}
	}
	
	private void Freeze() {
		Iterator<String> iter = ctx.freezeWorklist.iterator();
		assert iter.hasNext();
		
		String vreg = iter.next();
		iter.remove();
		
		ctx.simplifyWorklist.add(vreg);
		FreezeMoves(vreg);
	}
	
	private void FreezeMoves(String vreg) {
		for (Mov mov : NodeMoves(vreg)) {
			String u, v;
			u = vreg;
			String x = ((Reg) mov.dst).hintName;
			String y = ((Reg) mov.src).hintName;
			
			// let v be the other one.
			if (GetAlias(y).equals(u))
				v = GetAlias(x);
			else {
				assert u.equals(GetAlias(x));
				v = GetAlias(y);
			}
			
			ctx.activeMoves.remove(mov);
			ctx.frozenMoves.add(mov);
			
			if (ctx.freezeWorklist.contains(v) && NodeMoves(v).isEmpty()) {
				ctx.freezeWorklist.remove(v);
				ctx.simplifyWorklist.add(v);
			}
		}
	}
	
	private void SelectSpill() {
		// select node to be spilled using favorite heuristic.
		// NOTE : avoid choosing nodes that are the tiny live ranges resulting
		// NOTE : from the fetches of previously spilled registers.
		Iterator<String> iter = ctx.spillWorklist.iterator();
		int heuristicIndex = (int) (Math.random()*(ctx.spillWorklist.size()));
		for (int i = 0; i < heuristicIndex; ++i) {
			assert iter.hasNext();
			iter.next();
		}
		
		assert iter.hasNext();
		String spilled = iter.next();
		
		ctx.spillWorklist.remove(spilled);
		ctx.simplifyWorklist.add(spilled);
		
		FreezeMoves(spilled);
	}
	
	private void AssignColors() {
		while (!ctx.selectStack.empty()) {
			String n = ctx.selectStack.pop();
			Set<PhysicalReg.PhyRegType> okColors = new HashSet<>(PhysicalReg.okColors);
			// exclude adjacent colors in interference graph.
			for (String w : ctx.itfg.GetAdjList(n)) {
				if (ctx.coloredNodes.contains(GetAlias(w)) || ctx.precolored.containsKey(GetAlias(w)))
					okColors.remove(ctx.colors.get(w));
			}
			
			if (okColors.isEmpty())
				ctx.spilledNodes.add(n);
			else {
				ctx.coloredNodes.add(n);
				ctx.colors.put(n, okColors.iterator().next()); 
			}
		}
		
		for (String coale : ctx.coalescedNodes)
			ctx.colors.put(coale, ctx.colors.get(GetAlias(coale)));
	}
	
	private void RewriteProgram() {
		// allocate memory locations for each spilledNodes.
		// create a new temporary vi for each definition and each use.
		// in the program (instructions), insert a store after each definition
		// of a vi, a load before each use of a vi.
		// put all the vi into a set newTemps.
		
		// we rewrite one vreg at a time.
		for (String spl : ctx.spilledNodes) {
			for (AsmBB bb : curf.bbs) {
				for (int i = 0; i < bb.insts.size(); ++i) {
					Inst inst = bb.insts.get(i);
					
					List<Reg> uses = GetUses(inst);
					List<Reg> defs = GetDefs(inst);
					
					boolean addStore = false;
					if (defs.size() > 0) {
						assert defs.size() == 1;
						// need to add a store, if the defined virtual register is spilled.
						addStore = spl.equals(defs.get(0).hintName);
					}
					
					for (Reg use : uses) {
						// if use is spilled, create a virtual register,
						// load it in advance, and load it.
						if (spl.equals(use.hintName)) {
							String splTmp = ctx.GetNewTempforSpilled(curf, use);
							StackMem spilled = new StackMem(spl);
							bb.insts.add(i++, new Load(use, spilled, bb));
							ReplaceUses(inst, use.hintName, splTmp);
						}
					}
					
					if (addStore) {
						assert defs.size() == 1;
						Reg def = defs.get(0);
						
						// if current def's name has been changed by use... no extra virtual register is needed.
						// if current def's name is still spl, an extra virtual register is needed to hold the
						// nasm calculated value, and then store the vreg into stackMem.
						if (spl.equals(def.hintName))
							def.hintName = ctx.GetNewTempforSpilled(curf, def);
						bb.insts.add(++i, new Mov(new StackMem(spl), def, bb));
					}
				}
			}
		}
	}
	
	private void AttachColor2Reg() {
		for (AsmBB bb : curf.bbs) {
			for (Inst inst : bb.insts) {
				for (Reg reg : GetVregs(inst)) {
					if (!reg.isColored()) {
						assert ctx.colors.containsKey(reg.hintName); 
						reg.AllocReg(new PhysicalReg(ctx.colors.get(reg.hintName)));
					}
				}
			}
		}
	}
	
	/********************* utility *************************/
	private boolean MoveRelated(String vreg) {
		return !NodeMoves(vreg).isEmpty();
	}
	
	// available moves associated with current virtual registers.
	private Set<Mov> NodeMoves (String vreg) {
		Set<Mov> moves = new LinkedHashSet<>();

		for (Mov mov : ctx.GetMovelist(vreg))
			if (ctx.activeMoves.contains(mov) || ctx.worklistMoves.contains(mov))
				moves.add(mov);

		return moves;
	}
	
	private Set<String> Adjacent(String vreg) {
		Set<String> adjs = new HashSet<>();
		for (String adj : ctx.itfg.GetAdjList(vreg)) {
			if (!ctx.selectStack.contains(adj) && !ctx.coalescedNodes.contains(adj))
				adjs.add(adj);
		}
		return adjs; 
	}
	
	// NOTE : in tiger book, adjList and degree are only for non-pre-colored virtual registers.
	private void AddInterEdge(String u, String v) {
		if (!ctx.itfg.HasAdjSet(u, v) && !u.equals(v)) {
			ctx.itfg.AddAdjSet(u, v);
			ctx.itfg.AddAdjSet(v, u);
			
			if (!ctx.IsPreColored(u)) {
				ctx.itfg.AddAdjList(u, v);
				ctx.itfg.IncDegree(u);
			}
			if (!ctx.IsPreColored(v)) {
				ctx.itfg.AddAdjList(v, u);
				ctx.itfg.IncDegree(v);
			}
		}
	}
	
	// decrement node's degree, and do something when degree drops from K to K-1.
	private void DecrementDegree(String vreg) {
		int deg = ctx.itfg.GetDegree(vreg);
		ctx.itfg.DecDegreeUtil(vreg);
		
		if (deg == REGNUM) {
			// enable moves for current vreg and all its neighbors.
			Set<String> regsMovEnable = new HashSet<>(Adjacent(vreg));
			regsMovEnable.add(vreg);
			EnableMoves(regsMovEnable);
			
			if (MoveRelated(vreg))
				ctx.freezeWorklist.add(vreg);
			else
				ctx.simplifyWorklist.add(vreg);
		}
	}
	
	// FIXME : i don't very much understand it.
	private void EnableMoves (Set<String> vregs) {
		for (String vreg : vregs)
			for (Mov mov : NodeMoves(vreg))
				if (ctx.activeMoves.contains(mov)) {
					ctx.activeMoves.remove(mov);
					ctx.worklistMoves.add(mov);
				}
	}
	
	// attributes for vreg has changed. Adjust and add it to suitable worklist.
	// reevaluate and put back to suitable place in worklist.
	private void AddWorklist(String vreg) {
		if (!ctx.IsPreColored(vreg) &&
						!MoveRelated(vreg) &&
						ctx.itfg.GetDegree(vreg) < REGNUM) {
			ctx.freezeWorklist.remove(vreg);
			ctx.simplifyWorklist.add(vreg);
		}
	}
	
	private boolean OK (String t, String r) {
		return ctx.itfg.GetDegree(t) < REGNUM ||
						ctx.IsPreColored(t) ||
						ctx.itfg.HasAdjSet(t, r);
	}
	
	// use conservative coalesce strategy.
	private boolean Conservative(Set<String> vregs) {
		int k = 0;
		for (String vreg : vregs)
			if (ctx.itfg.GetDegree(vreg) >= REGNUM)
				++k;
		return k < REGNUM;
	}
	
	// simple union-find set
	private String GetAlias (String vreg) {
		if (ctx.coalescedNodes.contains(vreg))
			return GetAlias(ctx.alias.get(vreg));
		return vreg;
	}
}

