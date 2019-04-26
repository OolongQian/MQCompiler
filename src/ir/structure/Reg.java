package ir.structure;


import ir.quad.Quad;

import java.util.HashSet;
import java.util.Set;

public class
Reg extends IrValue {
	public String name;
	public Set<BasicBlock> usesBB = new HashSet<>();
	public Set<BasicBlock> defsBB = new HashSet<>();
	public Set<Quad> usesQuad = new HashSet<>();
	public Set<Quad> defsQuad = new HashSet<>();
	
	public Reg(String name) {
		this.name = name;
	}
	
	@Override
	public String getText() {
		return name;
	}
}
