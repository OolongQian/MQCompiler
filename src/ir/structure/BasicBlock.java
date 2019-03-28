package ir.structure;

import ir.Linkable;
import ir.quad.Alloca;
import ir.quad.Jump;
import ir.quad.Phi;
import ir.quad.Quad;

import java.util.*;

public class BasicBlock extends Linkable {
	private String name;
	public List<Quad> quads = new LinkedList<>();
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
	
	public void AllocaFront(Alloca quad) {
		assert parentFunct.bbs.GetHead() == this;
		quads.add(0, quad);
	}
	
	// isn't restricted by complete.
	public void PushfrontPhi(Phi quad) {
		quads.add(0, quad);
	}
	
	// isn't restricted by complete.
	public void AddDefaultJump(Quad quad) {
		quads.add(quad);
	}
	
	public int GetQuadSize() {
		return quads.size();
	}
	
	public List<Quad> TraverseQuad() {
		return quads;
	}
	
	public Quad GetLastQuad() {
		return quads.get(quads.size() - 1);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
