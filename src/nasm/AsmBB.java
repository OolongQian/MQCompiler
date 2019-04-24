package nasm;

import nasm.inst.Inst;

import java.util.LinkedList;
import java.util.List;

/** Inst Basic block is simpler than ir's, because fewer optimization is conducted here. */
public class AsmBB {
	// _AsmBBName_AsmFunctionName
	public String hintName;
	public AsmFunct parentFunct;
	public List<Inst> insts = new LinkedList<>();
	public Integer loopLevel = null;
	
	public AsmBB (String hintName, AsmFunct parent, Integer loopLevel) {
		this.hintName = hintName;
		this.parentFunct = parent;
		this.loopLevel = loopLevel;
	}
}
