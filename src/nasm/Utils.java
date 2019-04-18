package nasm;

import ir.structure.BasicBlock;
import nasm.inst.Msg;

import java.util.HashMap;
import java.util.Map;

public class Utils {
	public static void DelMsg (AsmFunct asmFunct) {
		for (AsmBB bb : asmFunct.bbs) {
			bb.insts.removeIf(x -> x instanceof Msg);
		}
	}
	
	public static void Say (AsmBB bb, int idx, String something) {
		bb.insts.add(idx, new Msg (bb, something));
	}
	
	public static void Say (AsmBB bb, String something) {
		bb.insts.add(new Msg (bb, something));
	}
	
	// turn *0 into __0
	public static String StringLiteralRenamer (String strId) {
		return "_S_" + strId.substring(1);
	}
	
	// rename basic block's name to be AsmBB, attach parentFunction to it.
	// and again, rename Basic block name in jump.
	public static String BasicBlockRenamer (BasicBlock bb) {
		return String.format("_B_%s_%s", bb.name.substring(1), bb.parentFunct.name);
	}
	
	// change @a to _G_a.
	public static String GlobalRenamer (String gName) {
		return String.format("_G_%s", gName.substring(1));
	}
	
	private static Map<String, String> builtin2Extern = new HashMap<>();
	static {
		builtin2Extern.put("~print", "print");
		builtin2Extern.put("~println", "println");
		builtin2Extern.put("~getString", "getString");
		builtin2Extern.put("~getInt", "getInt");
		builtin2Extern.put("~toString", "toString");
		builtin2Extern.put("~-string#length", "_stringLength");
		builtin2Extern.put("~-string#substring", "_stringSubstring");
		builtin2Extern.put("~-string#parseInt", "_stringParseInt");
		builtin2Extern.put("~-string#ord", "_stringOrd");
		builtin2Extern.put("~-string#add", "_stringAdd");
		builtin2Extern.put("~--array#size", "_arraySize");
		builtin2Extern.put("~-string#lt", "_stringEq");
		builtin2Extern.put("~-string#gt", "_stringNeq");
		builtin2Extern.put("~-string#le", "_stringLt");
		builtin2Extern.put("~-string#ge", "_stringGt");
		builtin2Extern.put("~-string#eq", "_stringLe");
		builtin2Extern.put("~-string#ne", "_stringGe");
	}
	public static String BuiltinRenamer (String fName) {
		assert builtin2Extern.containsKey(fName);
		return builtin2Extern.get(fName);
	}
}
