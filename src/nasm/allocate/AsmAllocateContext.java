package nasm.allocate;

import nasm.AsmBB;
import nasm.AsmFunct;
import nasm.inst.Mov;
import nasm.reg.PhysicalReg;
import nasm.reg.Reg;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;

import static nasm.Utils.GraphvizRegRenamer;

public class AsmAllocateContext {
	
	public Map<String, PhysicalReg.PhyRegType> precolored = new HashMap<>();
	public Map<String, PhysicalReg.PhyRegType> colors = new HashMap<>();
	
	
	// temporary registers, not precolored and not yet processed.
	public Set<String> initial = new LinkedHashSet<>();
	
	// liveness analysis for basic blocks.
	public Map<AsmBB, Set<String>> liveOuts = new HashMap<>();
	
	/**************** worklists *****************/
	public List<String> spillWorklist = new ArrayList<>();
	public Set<String> freezeWorklist = new LinkedHashSet<>();
	public Set<String> simplifyWorklist = new LinkedHashSet<>();

	/*************** move classification sets ****************/
	// moves have been coalesced.
	public Set<Mov> coalescedMoves = new LinkedHashSet<>();
	// moves that are interfered.
	public Set<Mov> constainedMoves = new LinkedHashSet<>();
	// moves frozen. No longer used for coalesce.
	public Set<Mov> frozenMoves = new LinkedHashSet<>();
	// moves waiting for coalescing.
	public Set<Mov> worklistMoves = new LinkedHashSet<>();
	// moves not yet ready for coalesce.
	public Set<Mov> activeMoves = new LinkedHashSet<>();
	
	/**************** vregs (node) states **********************/
	public Set<String> spilledNodes = new LinkedHashSet<>();
	public Set<String> coalescedNodes = new LinkedHashSet<>();
	public Set<String> coloredNodes = new LinkedHashSet<>(); 
	// stack containing temporaries removed from graph. (due to simplification or something else).
	public Stack<String> selectStack = new Stack<>();
	
	/**************** Heuristic ****************************/
	public Map<String, Double> heuristicUse = new HashMap<>();
	public Map<String, Double> heuristicDef = new HashMap<>();
	public Map<String, Double> heuristic = new HashMap<>();
	/*********************** Other data structures *************************/
	// a map from a node to the list of moves it is associated with.
	private Map<String, Set<Mov>> movelists = new HashMap<>();
	
	public void AddMovelist(String vReg, Mov move) {
		if (!movelists.containsKey(vReg))
			movelists.put(vReg, new LinkedHashSet<>());
		movelists.get(vReg).add(move);
	}
	
	public Set<Mov> GetMovelist(String vreg) {
		if (!movelists.containsKey(vreg))
			movelists.put(vreg, new HashSet<>());
		return movelists.get(vreg);
	}
	
	// when a move (u, v) is coalesced, and v put in coalescedNodes,
	// then alias(v) = u.
	public Map<String, String> alias = new HashMap<>();
	
	public InterfereGraph itfg = new InterfereGraph();
	
	/************ utility **************/
	public boolean IsPreColored(String reg) {
		return precolored.containsKey(reg);
	}
	
	/************** temp virtual register generator *****************/
	private Map<String, Integer> tmpCnt = new HashMap<>();
	// new temps are named in independent namespace of asmFunctions.
	// allocation context is reset for each AsmFunct, no need manual reset.
	public String GetNewTempforSpilled(AsmFunct asmFunct, Reg spilledVreg) {
		if (!tmpCnt.containsKey(spilledVreg.hintName))
			tmpCnt.put(spilledVreg.hintName, 0);
		
		int cnt = tmpCnt.get(spilledVreg.hintName);
		tmpCnt.put(spilledVreg.hintName, cnt + 1);
		
		return String.format("spl_%d_%s_%s", cnt, spilledVreg.hintName, asmFunct.name);
	}
	
	/***************** Visualization *******************/
	public void PrintGraph(AsmFunct curFunct) {
		System.out.println(String.format("%s interference : ", curFunct.name));
		itfg.PrintGraph();
		System.out.println();
	}
	
	public void Graphviz(AsmFunct curFunct, boolean beforeAlloca) throws FileNotFoundException {
		String filepath = (beforeAlloca) ? String.format("./bef_%s.dot", curFunct.name) :
																		String.format("./aft_%s.dot", curFunct.name);
		itfg.Graphviz(filepath, curFunct.name);
	}
}

class InterfereGraph {
	public Set<Pair> adjSet = new HashSet<>();
	private Map<String, Set<String>> adjList = new HashMap<>();
	private Map<String, Integer> degree = new HashMap<>();
	
	void PrintGraph() {
		for (String s : adjList.keySet()) {
			System.out.print(String.format("%s -- ", s));
			adjList.get(s).forEach(x -> System.out.print(String.format("%s ", x)));
			System.out.println();
		}
	}
	
	void Graphviz(String graphvizPath, String graphName) throws FileNotFoundException {
		PrintStream fout = new PrintStream(new FileOutputStream(graphvizPath));
		fout.println(String.format("digraph %s {", graphName));
		for (String src : adjList.keySet())
			for (String dst : adjList.get(src))
				fout.println(String.format("%s -> %s ;", GraphvizRegRenamer(src), GraphvizRegRenamer(dst)));
		fout.println("}");
	}
	
	boolean HasAdjSet(String u, String v) {
		return adjSet.contains(new Pair(u, v));
	}
	
	void AddAdjSet(String u, String v) {
		adjSet.add(new Pair(u, v));
	}
	
	void AddAdjList(String u, String v) {
		if (!adjList.containsKey(u))
			adjList.put(u, new LinkedHashSet<>());
		adjList.get(u).add(v);
	}
	
	Set<String> GetAdjList(String u) {
		if (!adjList.containsKey(u))
			adjList.put(u, new HashSet<>());
		return adjList.get(u);
	}
	
	void IncDegree(String u) {
		if (!degree.containsKey(u))
			degree.put(u, 0);
		
		degree.put(u, degree.get(u) + 1);
	}
	
	int GetDegree(String u) {
		if (!degree.containsKey(u))
			degree.put(u, 0);
		
		return degree.get(u);
	}
	
	// distinguish between decDegree, which checks when degree drops from K to K-1.
	void DecDegreeUtil(String u) {
		assert degree.containsKey(u);
		degree.put(u, degree.get(u) - 1);
	}
}


class Pair {
	String first;
	String second;
	
	public Pair(String first, String second) {
		this.first = first;
		this.second = second;
	}
	
	@Override
	public boolean equals(Object obj) {
		assert obj.getClass() == Pair.class;
		return first.equals(((Pair)obj).first) && second.equals(((Pair)obj).second);
	}
	
	// directed edges.
	@Override
	public int hashCode() {
		return ((int)(Math.pow(first.hashCode(), 31)) * second.hashCode()) + first.hashCode() * 31 + second.hashCode();
	}
	
}