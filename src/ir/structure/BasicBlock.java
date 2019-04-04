package ir.structure;

import ir.quad.Quad;

import java.util.LinkedList;
import java.util.List;

public class BasicBlock {
	public String name;
	public List<Quad> quads = new LinkedList<>();
	public boolean complete = false;
	public Function parentFunct;
	
	public BasicBlock prev, next;
	
	public BasicBlock(String name, Function parentFunct) {
		this.name = name;
		this.parentFunct = parentFunct;
		this.prev = this.next = null;
	}
}
