package nasm.inst;

import nasm.AsmBB;
import nasm.AsmPrinter;
import nasm.reg.Reg;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Call extends Inst {
	public String functName;
	// if a function returns, it defines pre-colored register rax.
	public Boolean ret = null;
	
	private static Map<String, Boolean> builtinRet = new HashMap<>();
	static {
		builtinRet.put("print", Boolean.FALSE);
		builtinRet.put("println", Boolean.FALSE);
		builtinRet.put("getString", Boolean.TRUE);
		builtinRet.put("getInt", Boolean.TRUE);
		builtinRet.put("toString", Boolean.TRUE);
		builtinRet.put("_stringLength", Boolean.TRUE);
		builtinRet.put("_stringSubstring", Boolean.TRUE);
		builtinRet.put("_stringParseInt", Boolean.TRUE);
		builtinRet.put("_stringOrd", Boolean.TRUE);
		builtinRet.put("_stringAdd", Boolean.TRUE);
		builtinRet.put("_stringEq", Boolean.TRUE);
		builtinRet.put("_stringNeq", Boolean.TRUE);
		builtinRet.put("_stringLt", Boolean.TRUE);
		builtinRet.put("_stringGt", Boolean.TRUE);
		builtinRet.put("_stringLe", Boolean.TRUE);
		builtinRet.put("_stringGe", Boolean.TRUE);
		builtinRet.put("_arraySize", Boolean.TRUE);
	}
	
	public Call(AsmBB blk, String functName, Boolean ret) {
		super(null, null, blk);
		this.functName = functName;
		
		if (ret == null) {
			assert builtinRet.containsKey(functName);
			this.ret = builtinRet.get(functName);
		} else
			this.ret = ret;
	}
	
	@Override
	public void AcceptPrint(AsmPrinter printer) {
		printer.Print(this);
	}
	
}
