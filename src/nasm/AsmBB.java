package nasm;

import nasm.asm.Asm;

import java.util.LinkedList;
import java.util.List;

/**
 * Asm Basic block is simpler than ir's, because fewer optimization is conducted here. */
public class AsmBB {
	// _AsmBBName_AsmFunctionName
	public String hintName;
	public AsmFunct parentFunct;
	public List<Asm> asms = new LinkedList<>();
	
	public AsmBB (String hintName, AsmFunct parent) {
		this.hintName = hintName;
		this.parentFunct = parent;
	}
	
}
