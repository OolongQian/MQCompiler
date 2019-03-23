package opt.ssa;

import ir.quad.Phi;
import ir.structure.BasicBlock;
import ir.structure.Reg;

import java.util.*;

public class Info {
	// node -> nodes that dominates it.
	public Set<BasicBlock> inferiorTo = new HashSet<>();
	// node -> nodes that are dominated by it.
	public Set<BasicBlock> superiorTo = new HashSet<>();
	public Boolean vis = true;
	public BasicBlock iDom = null;
	public Set<BasicBlock> domTree = new HashSet<>();
	public Set<BasicBlock> domFrontier = new HashSet<>();
	public Map<Reg, Phi> phis = new HashMap<>();
	public static Set<Reg> vars = new HashSet<>();
	public static Stack<Reg> versionStack = new Stack<>();
	private static int versionCnt = 0;
	private static String varName;
	
	public static void ConfigNamingVar(Reg var) {
		assert var.alloca;
		varName = var.name;
		versionCnt = 0;
	}
	public static Reg NewVersion() {
		Reg newVer = new Reg(varName + '[' + versionCnt++ + ']');
		newVer.alloca = true;
		newVer.renamed = true;
		return newVer;
	}
}