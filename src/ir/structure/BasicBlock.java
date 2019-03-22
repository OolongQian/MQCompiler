package ir.structure;

import ir.Linkable;
import ir.quad.Quad;

import java.util.LinkedList;
import java.util.List;

public class BasicBlock extends Linkable {
	private String name;
	private List<Quad> quads = new LinkedList<>();
	private List<BasicBlock> predecessor = new LinkedList<>();
	private List<BasicBlock> successor = new LinkedList<>();
	public boolean complete = false;
	public Function parentFunct;
	
	public BasicBlock(String name, Function parent) {
		this.name = name;
		this.parentFunct = parent;
	}
	
	/**
	 * CFG construction.
	 * */
	public void JumpTo(BasicBlock to) {
		successor.add(to);
		to.predecessor.add(this);
	}
	
	public String getName() {
		return name;
	}
	
	public void Complete() {
//		assert !complete;
		complete = true;
	}
	
	public void Add(Quad quad) {
		assert !complete;
		quads.add(quad);
	}
	public void Add(int pos, Quad quad) {
		assert !complete;
		quads.add(pos, quad);
	}
	
	public List<Quad> TraverseQuad() {
		return quads;
	}
}
