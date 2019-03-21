package ir.Interpreter.parse;

import java.util.*;

public class Funct {
	/**
	 * Function name
	 * */
	public String name;
	
	/**
	 * List of literal instructions
	 * */
	public List<Inst> insts = new ArrayList<>();
	
	/**
	 * let us locate all the basic blocks inside a function
	 * */
	public Map<String, Integer> label2Index = new HashMap<>();
	
	/**
	 * Formal parameters' literal names.
	 * <Function> foo %0 %1
	 * */
	public List<String> formalRegs = new LinkedList<>();
	
	public Funct(String name) {
		this.name = name;
	}
	
	public int GetNextIndex() {
		return insts.size();
	}
	
	public void AddInst(Inst inst) {
		insts.add(inst);
	}
	
	public void AddFormalArg(String farg) {
		formalRegs.add(farg);
	}
}
