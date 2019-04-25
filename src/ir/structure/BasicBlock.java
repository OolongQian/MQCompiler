package ir.structure;

import ir.quad.Quad;

import java.util.LinkedList;
import java.util.List;

public class BasicBlock {
	public String name;
	public List<Quad> quads = new LinkedList<>();
	public IrFunct parentFunct;
	public Integer loopLevel = null;
	
	public BasicBlock prev, next;
	
	public BasicBlock(String name, IrFunct parentFunct, Integer loopLevel) {
		this.name = name;
		this.parentFunct = parentFunct;
		this.prev = this.next = null;
		this.loopLevel = loopLevel;
	}
}
