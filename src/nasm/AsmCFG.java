package nasm;

import java.util.HashMap;
import java.util.Set;

public class AsmCFG {
	public HashMap<AsmBB, Set<AsmBB>> predesessors = new HashMap<>();
	public HashMap<AsmBB, Set<AsmBB>> successors = new HashMap<>();
}
