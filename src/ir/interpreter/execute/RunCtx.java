package ir.interpreter.execute;

import ir.interpreter.parse.Funct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RunCtx {
	/**
	 * Each run-context is associated with a text of code.
	 * */
	public Funct func;
	
	/**
	 * Current instruction No.
	 * */
	public int curInst;
	
	/**
	 * dead marker
	 * */
	public boolean terminate = false;
	
	/**
	 * return value.
	 * */
	public Reg ret = null;
	
	/**
	 * record all live register values.
	 * */
	public Map<String, Reg> defuse = new HashMap<>();
	
	/**
	 * Used when return.
	 * */
	public RunCtx caller;
	
	/**
	 * Trace current BasicBlock
	 * */
	public String curBlk;
	
	/**
	 * Choose phi option
	 * */
	public String fromBlk;

	public RunCtx(Funct func) {
		this.func = func;
		this.caller = null;
	}
	
	/**
	 * int foo %0, %1
	 * call foo(%1, %2)
	 * Associate actual calling args with formal args.
	 * Bind them in defuse.
	 * */
	public void PassArgs(List<Reg> invokeArgs) {
		assert func.formalRegs.size() == invokeArgs.size();
		for (int i = 0; i < invokeArgs.size(); ++i) {
			String formalName = func.formalRegs.get(i);
			Reg argVal = invokeArgs.get(i);
			assert !defuse.containsKey(formalName);
			defuse.put(formalName, argVal);
		}
	}
	
	// current RunCtx is calling another function as Running context.
	public void Invoke(RunCtx callee) {
		callee.caller = this;
	}
	
	public void JumpToBB(String label) {
		fromBlk = curBlk;
		curBlk = label;
	}
	
	public String GetJumpFrom() {
		assert fromBlk != null;
		return fromBlk;
	}
	
	public void SetRetVal(Reg val) {
		this.ret = val;
	}
	
	public Reg GetRetVal() {
		return ret;
	}
	
	// terminate running, and return the previous Running context.
	public RunCtx Return() {
		return caller;
	}
}
