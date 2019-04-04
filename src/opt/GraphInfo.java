package opt;

import ir.quad.Phi;
import ir.structure.BasicBlock;
import ir.structure.Reg;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GraphInfo {
	// node -> nodes that dominates it.
	public Set<BasicBlock> inferiorTo = new HashSet<>();
	// node -> nodes that are dominated by it.
	public Set<BasicBlock> superiorTo = new HashSet<>();
	
	public Boolean vis = true;
	public BasicBlock iDom = null;
	public Set<BasicBlock> domTree = new HashSet<>();
	public Set<BasicBlock> domFrontier = new HashSet<>();
	public Map<Reg, Phi> phis = new HashMap<>();
}
