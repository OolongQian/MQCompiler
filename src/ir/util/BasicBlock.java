package ir.util;

import ir.quad.Quad;

import java.util.LinkedList;
import java.util.List;

public class BasicBlock {
  private String name;
  public List<Quad> quads = new LinkedList<>();
  private boolean complete = false;

  private BasicBlock ifTrue;
  private BasicBlock ifFalse;
	
	public String getName() {
		return name;
	}
	
	public BasicBlock(String name) {
    this.name = name;
  }

  public boolean IsCompleted() {
    return complete;
  }

  public void Complete() {
    complete = true;
  }

  public BasicBlock getIfTrue() {
    return ifTrue;
  }

  public void setIfTrue(BasicBlock ifTrue) {
    this.ifTrue = ifTrue;
  }

  public BasicBlock getIfFalse() {
    return ifFalse;
  }

  public void setIfFalse(BasicBlock ifFalse) {
    this.ifFalse = ifFalse;
  }
}
