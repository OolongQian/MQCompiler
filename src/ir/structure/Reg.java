package ir.structure;


import ir.quad.Quad;

import java.util.HashSet;
import java.util.Set;

import static ir.Utility.inlinePrefix;
import static ir.Utility.irRegRecorder;

public class
Reg extends IrValue {
	public String name;
	
	// used for SSA construction.
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
	
	@Override
	public IrValue Copy() {
		String copyName;
		// don't move global variables.
		if (name.startsWith("@"))
			copyName = name;
		else
			copyName = name + inlinePrefix;
		
		if (!irRegRecorder.containsKey(copyName))
			irRegRecorder.put(copyName, new Reg (copyName));
		return irRegRecorder.get(copyName);
	}
}
