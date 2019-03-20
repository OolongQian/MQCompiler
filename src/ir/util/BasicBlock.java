package ir.util;

import ir.quad.Quad;
import ir.reg.IrValue;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BasicBlock {
  private String name;
  public List<Quad> quads = new LinkedList<>();
  private boolean complete = false;
  // enter with value.
  public Map<BasicBlock, IrValue> phiOptions = new HashMap<>();
  public List<BasicBlock> enter = new LinkedList<>();
  public List<BasicBlock> exit = new LinkedList<>();
  

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

  public void EnterWithVal(BasicBlock from, IrValue val) {
		assert !phiOptions.containsKey(from);
		phiOptions.put(from, val);
		enter.add(from);
  }
  
  public void Enter(BasicBlock from) {
		enter.add(from);
  }
  
  public void Exit(BasicBlock to) {
		exit.add(to);
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
