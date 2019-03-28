package opt.optimizers;

import ir.quad.Call;
import ir.quad.Quad;
import ir.structure.BasicBlock;
import ir.structure.Function;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/************************** CFG changes ******************************/
public class FunctionInliner {
	/**
	 * Make a copy of all function and record its info.
	 * Because function inline will change the original Function's structure.
	 * We don't want the inline effect accumulates exponentially.
	 * */
	private Map<String, FunctInfo> inlineInfos = new HashMap<>();
	private Map<String, Function> functCopys = new HashMap<>();
	
	public void CollectFunctInfo(Map<String, Function> functs) {
		for (String name : functs.keySet()) {
			try {
				Function copy = functs.get(name).DeepClone();
				functCopys.put(name, copy);
				inlineInfos.put(name, ParseFunctInfo(copy));
			}
			catch (IOException e) {
				;
			}
			catch (ClassNotFoundException e) {
				;
			}
			
		}
	}
	private FunctInfo ParseFunctInfo(Function funct) {
		FunctInfo info = new FunctInfo();
		BasicBlock cur = funct.bbs.GetHead();
		
		if (funct.this_ != null) {
			info.method = true;
		}
		
		while (cur != null) {
			List<Quad> quads = cur.quads;
			for (Quad quad : quads) {
				++info.quadNo;
				if (quad instanceof Call) {
					++info.callNo;
					if (((Call) quad).funcName.equals(funct.name)) {
						info.recursive = true;
					}
				}
			}
			cur = (BasicBlock) cur.next;
		}
		
		return info;
	}
	
	/**
	 * traverse quads in an ir function, inline all satisfied function call.
	 * Record all calls to be processed to prevent concurrent modification problem.
	 * */
	private List<Call> calls = new LinkedList<>();
	
	public void InlineFunctCalls(Function funct) {
		BasicBlock cur = funct.bbs.GetHead();
		while (cur != null) {
			for (Quad quad : cur.quads) {
				if (quad instanceof Call)
					calls.add((Call) quad);
			}
			cur = (BasicBlock) cur.next;
		}
		
		for (Call call : calls) {
			if (ChooseInline(call.funcName)) {
				InlineFunctCall(call);
			}
		}
	}
	
	private void InlineFunctCall(Call call) {
	
	}
	/**
	 * Customized inline choice based on functInfo.
	 * */
	private boolean ChooseInline(String name) {
		FunctInfo info = inlineInfos.get(name);
		return true;
	}
	
	/**
	 * registers and basic blocks in inlined function needs to be renamed.
	 * If inlining multiple times, each pass needs an extra namespace.
	 * If a same function is inlined multiple times in different positions, we also needs different names, because we are in ssa form.
	 * Use a unique id for inline renaming suffix. inlineNo is updated in each inline.
	 * Function inline rename is implemented by suffix ^(inlineNo).
	 * */
	private int inlineNo = 0;
	private String Rename(String oldName) {
		return oldName + '^' + Integer.toString(inlineNo);
	}
}

class FunctInfo {
	public int quadNo = 0;
	public int callNo = 0;
	public boolean recursive = false;
	public boolean method = false;
}