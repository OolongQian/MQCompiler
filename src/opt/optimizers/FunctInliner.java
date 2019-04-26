package opt.optimizers;

import ir.IrProg;
import ir.quad.*;
import ir.structure.*;

import java.util.*;

import static config.Config.INLINE_LEVEL;
import static ir.Utility.inlinePrefix;
import static ir.Utility.irRegRecorder;
import static java.lang.Math.max;

public class FunctInliner {
	
	// functs original copy used for inline. immutable because the copy isn't completely deep.
	private Map<String, IrFunct> originFuncts = new HashMap<>();
	private Map<String, InlineInfo> inlineInfo = new HashMap<>();
	private Queue<Call> inlineCall = new LinkedList<>();
	// how many inline has occurred, used for local variable renaming.
	private int inlineNo = 0;
	
	public void FunctInline (IrProg ir) {
		inlinePrefix = "";
		
		RecordFunctOriginCopy(ir);
		ScanOriginFunctInfo ();
		
		for (inlineNo = 0; inlineNo < INLINE_LEVEL; ++inlineNo) {
			inlineCall.clear();
			ScanInlineCall (ir);
			
//			Printer printer = new Printer(null);
//			ir.functs.values().forEach(printer::print);
			
			while (!inlineCall.isEmpty()) {
				// call is in ir.
				Call call = inlineCall.remove();
				BasicBlock cur = call.blk;
				IrFunct irParent = call.blk.parentFunct;
				
				// split current basic block into two parts.
				IrFunct copy2Inline = GetFunctCopy(call.funcName);
				BasicBlock exit = SplitBByCall(call);
				
				// handle return value, if has return value. Alloca a memory space to store and load them.
				Reg retval = null;
				if (!call.ret.name.equals("@null")) {
					retval = new Reg("ret_" +  irParent.name + inlinePrefix);
					Alloca allocaRet = new Alloca(retval);
					allocaRet.blk = irParent.bbs.list.Head();
					irParent.bbs.list.Head().quads.add(0, allocaRet);
				}
				
				assert cur.quads.indexOf(call) == cur.quads.size() - 1;

				// remove call.
				cur.quads.remove(call);

				// pass parameters.
				for (int i = 0; i < call.args.size(); ++i) {
					Mov argPass = new Mov (copy2Inline.regArgs.get(i), call.args.get(i));
					argPass.blk = cur;
					cur.quads.add(argPass);
				}

				// add jump to copy2inline's entry.
				Jump jmp2inline = new Jump(copy2Inline.bbs.list.Head());
				jmp2inline.blk = cur;
				cur.quads.add(jmp2inline);
				
				// scan function to be inlined, replace return by a jump to exit.
				for (BasicBlock BBinl = copy2Inline.bbs.list.Head(); BBinl != null; BBinl = BBinl.next) {
					for (int i = 0; i < BBinl.quads.size(); ++i) {
						Quad quad = BBinl.quads.get(i);
						if (quad instanceof Ret) {
							// remove ret.
							Ret ret = (Ret) quad;
							BBinl.quads.remove(i);
							// if it need to return a value, add an move statement.
							if (!call.ret.name.equals("@null")) {
								assert retval != null;
								// add a store to it.
								Store storeRet = new Store (retval, ret.val);
								storeRet.blk = BBinl;
								BBinl.quads.add(i++, storeRet);
							}
							Jump jmp2exit = new Jump (exit);
							jmp2exit.blk = BBinl;
							BBinl.quads.add(i++, jmp2exit);
						}
					}
				}
				
				// exit should load return value.
				if (!call.ret.name.equals("@null")) {
					Load loadRet = new Load (call.ret, retval);
					loadRet.blk = exit;
					exit.quads.add(0, loadRet);
				}
				
				// place inlined function to irParent's basic block list.
				BasicBlock cursor = cur;
				BasicBlock nextinInline = null;
				
				copy2Inline.LinkedListCheck();
				irParent.LinkedListCheck();;
				for (BasicBlock BBinl = copy2Inline.bbs.list.Head(); BBinl != null; BBinl = nextinInline) {
					BBinl.parentFunct = irParent;
					nextinInline = BBinl.next;
					irParent.bbs.list.InsertAfter(cursor, BBinl);
					cursor = BBinl;
				}
				
				irParent.LinkedListCheck();;
			}
		}
	}
	
	private IrFunct GetFunctCopy (String functName) {
		// prepare the function to be inlined.
		assert inlineInfo.get(functName).HeuristicInline();
		assert originFuncts.containsKey(functName);
		// copy the original function and rename it.
		inlinePrefix = String.format("%sL", Integer.toString(inlineNo++));
		return FunctCopier(originFuncts.get(functName));
	}
	
	private void RecordFunctOriginCopy(IrProg ir) {
		ir.functs.values().forEach(x -> originFuncts.put(x.name, FunctCopier(x)));
	}
	
	private void ScanOriginFunctInfo () {
		for (IrFunct func : originFuncts.values()) {
			InlineInfo info = new InlineInfo();
			BasicBlock cur = func.bbs.list.Head();
			while (cur != null) {
				info.instNo += cur.quads.size();
				info.maxLoop = max (info.maxLoop, cur.loopLevel);
				for (Quad quad : cur.quads) {
					if (quad instanceof Call) {
						if (!((Call) quad).funcName.startsWith("~"))
							++info.callNo;
					}
				}
				cur = cur.next;
			}
			inlineInfo.put(func.name, info);
		}
	}
	
	private void ScanInlineCall (IrProg ir) {
		for (IrFunct func : ir.functs.values()) {
			BasicBlock cur = func.bbs.list.Head();
			while (cur != null) {
				for (Quad quad : cur.quads) {
					if (quad instanceof Call &&
									originFuncts.containsKey(((Call) quad).funcName) &&
									inlineInfo.get(((Call) quad).funcName).HeuristicInline())
							inlineCall.add((Call) quad);
				}
				cur = cur.next;
			}
		}
	}
	
	private Map<String, BasicBlock> BBMap = new HashMap<>();
	// add basic block prefix here.
	private IrFunct FunctCopier (IrFunct func) {
		BBMap.clear();
		irRegRecorder.clear();
		
		IrFunct copy = new IrFunct(func.name);
		copy.bbs.list.Clear();
		// curBB is just a tmp cursor.
		copy.curBB = null;
		copy.formalArgs.addAll(func.formalArgs);
		func.regArgs.forEach(x -> copy.regArgs.add((Reg) x.Copy()));
		// this is a util field used in ir building.
		copy.this_ = null;
		
		for (BasicBlock cur = func.bbs.list.Head(); cur != null; cur = cur.next) {
			assert (cur.next == null) == (cur == func.bbs.list.Tail());
			String copyBlockName = cur.name + inlinePrefix;
			BasicBlock BBcopy = new BasicBlock(copyBlockName, copy, cur.loopLevel);
			BBMap.put(copyBlockName, BBcopy);
			copy.bbs.list.PushBack(BBcopy);
		}
		
		for (BasicBlock cur = func.bbs.list.Head(); cur != null; cur = cur.next) {
			String copyBlockName = cur.name + inlinePrefix;
			assert BBMap.containsKey(copyBlockName);
			BasicBlock BBcopy = BBMap.get(copyBlockName);
			cur.quads.forEach(x -> BBcopy.quads.add(x.Copy(BBMap)));
		}
		// cfg is of no use in function inline.
		copy.bbs.cfg = null;
//		copy.bbs.cfg = CFGCopier(func.bbs.cfg, BBMap);

		return copy;
	}
	
	// suddenly find that CFG is of no use in function inline, since CFG always has to be rebuilt.
	private CFG CFGCopier (CFG cfg, Map<String, BasicBlock> BBMap) {
		CFG copy = new CFG();
		
		for (BasicBlock key : cfg.predesessors.keySet()) {
			String copykey = key.name + inlinePrefix;
			assert BBMap.containsKey(copykey);
			copy.predesessors.put(BBMap.get(copykey), new HashSet<>());
			
			for (BasicBlock predes : cfg.predesessors.get(key)) {
				String copypred =  predes.name + inlinePrefix;
				assert BBMap.containsKey(copypred);
				copy.predesessors.get(BBMap.get(copykey)).add(BBMap.get(copypred));
			}
		}
		
		for (BasicBlock key : cfg.successors.keySet()) {
			String copykey =  key.name + inlinePrefix;
			assert BBMap.containsKey(copykey);
			copy.successors.put(BBMap.get(copykey), new HashSet<>());

			for (BasicBlock scs : cfg.successors.get(key)) {
				String copyscs =  scs.name + inlinePrefix;
				assert BBMap.containsKey(copyscs);
				copy.successors.get(BBMap.get(copykey)).add(BBMap.get(copyscs));
			}
		}
		
		return copy;
	}
	
	
	// split basic block by call. call itself ends up in the previous basic block while
	// the later one, newly created part, is returned.
	// these two basic blocks are irrelevant in CFG
	// CFG is dead !!!
	private BasicBlock SplitBByCall (Call call) {
		BasicBlock cur = call.blk;
		IrFunct funct = cur.parentFunct;
		BasicBlock exit = new BasicBlock(cur.name + "_exit", funct, cur.loopLevel);
		// list insertion is all about linked list.
		funct.bbs.list.InsertAfter(cur, exit);
		// give up maintain cfg. build from scratch later on.
		int index = cur.quads.indexOf(call) + 1;
		while (index < cur.quads.size()) {
			exit.quads.add(cur.quads.get(index));
			cur.quads.remove(index);
		}
		exit.quads.forEach(x -> x.blk = exit);
		return exit;
	}
}


class InlineInfo {
	Integer instNo = 0;
	Integer maxLoop = 0;
	Integer callNo = 0;
	
	boolean HeuristicInline () {
		return instNo < 100 && callNo < 3;
	}
}