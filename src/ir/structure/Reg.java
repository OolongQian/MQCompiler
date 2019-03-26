package ir.structure;

import ir.quad.Quad;

import java.util.HashSet;
import java.util.Set;

public class Reg extends IrValue {
	public String name;
	public Reg reachingDef;
	public Set<BasicBlock> usesBB = new HashSet<>();
	public Set<BasicBlock> defsBB = new HashSet<>();
	public Set<Quad> usesQuad = new HashSet<>();
	public Set<Quad> defsQuad = new HashSet<>();
	
	// for debug.
	// in SSA construction.
	public boolean alloca = false;
	public boolean renamed = false;
	
	public Reg(String name) {
		this.name = name;
	}
	
	public boolean IsNull() {
		return name.equals("@null");
	}
	
	@Override
	public String getText() {
		return name;
	}
}
