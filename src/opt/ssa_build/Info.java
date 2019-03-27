package opt.ssa_build;

import ir.quad.Phi;
import ir.structure.BasicBlock;
import ir.structure.Reg;

import java.util.*;

public class Info {
	// vars are data structures in functional level, instead of basic block.
	// variable info recorder for SSA construction.
	public static Set<Reg> vars = new HashSet<>();
	public static Stack<Reg> versionStack = new Stack<>();
	public static int versionCnt = 0;
	private static String varName;
	// node -> nodes that dominates it.
	public Set<BasicBlock> inferiorTo = new HashSet<>();
	// node -> nodes that are dominated by it.
	public Set<BasicBlock> superiorTo = new HashSet<>();
	public Boolean vis = true;
	public BasicBlock iDom = null;
	public Set<BasicBlock> domTree = new HashSet<>();
	public Set<BasicBlock> domFrontier = new HashSet<>();
	public Map<Reg, Phi> phis = new HashMap<>();
	
	public static void ConfigNamingVar(Reg var) {
		assert var.alloca;
		versionStack.clear();
		varName = var.name;
		versionCnt = 0; // SSA version begins from 0. Add a temp version with index -1.
	}
	
	// FIXME : remove ! later.
	public static Reg NewVersion() {
		Reg newVer = new Reg('!' + varName + '[' + versionCnt++ + ']');
		newVer.alloca = true;
		newVer.renamed = true;
		return newVer;
	}
}