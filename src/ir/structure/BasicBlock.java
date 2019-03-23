package ir.structure;

import ir.Linkable;
import ir.quad.Phi;
import ir.quad.Quad;

import java.util.*;

public class BasicBlock extends Linkable {
	private String name;
	private List<Quad> quads = new LinkedList<>();
	public Set<BasicBlock> predecessor = new HashSet<>();
	public Set<BasicBlock> successor = new HashSet<>();
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
	public void PushfrontPhi(Phi quad) {
		quads.add(0, quad);
	}
	
	public List<Quad> TraverseQuad() {
		return quads;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
