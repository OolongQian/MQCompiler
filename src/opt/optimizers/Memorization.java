package opt.optimizers;

import ir.IrProg;
import ir.quad.*;
import ir.structure.*;

import java.util.*;

import static ir.Utility.MakeGreg;
import static ir.Utility.MakeInt;

public class Memorization {
	
	private int tableSize = 256;
	private Map<String, Reg> funct2tablePtr = new HashMap<>();
	
	public void Memorize(IrProg ir) {
		FindImmutableFunct(ir);
		for (IrFunct funct : ir.functs.values()) {
			if(immutableFuncts.contains(funct.name)) continue;
			List<Call> recursions = QualifyAndFetch(funct);
			if (!recursions.isEmpty()) {
				MakeGlobalMemorize(ir, funct);
				for (Call recursion : recursions) {
					RewriteCall(recursion);
				}
			}
		}
	}
	
	private void MakeGlobalMemorize(IrProg ir, IrFunct funct) {
		Reg memorizePtr = MakeGreg("memPtr_" + funct.name);
		funct2tablePtr.put(funct.name, memorizePtr);
		
		ir.globals.put(memorizePtr.name, memorizePtr);
		IrFunct init = ir.functs.get("_init_");
		
		BasicBlock bb = init.bbs.list.Head();
		int appendPos = init.bbs.list.Size() - 1;
		Reg tmp = init.GetTmpReg();
		
		Malloc malloc = new Malloc(tmp, MakeInt(tableSize * 8));
		Store store = new Store(memorizePtr, tmp);
		malloc.blk = store.blk = bb;
		
		bb.quads.add(appendPos++, malloc);
		bb.quads.add(appendPos++, store);
	}
	
	private void RewriteCall(Call call) {
		BasicBlock beforeCall = call.blk;
		int loop_lv = beforeCall.loopLevel;
		IrFunct funct = call.blk.parentFunct;
		
		assert call.funcName.equals(funct.name);
		
		assert call.args.size() == 1;
		IrValue arg = call.args.get(0);
		assert !call.ret.name.equals("@null");
		Reg retreg = call.ret;
		
		BasicBlock checkTable = new BasicBlock(funct.GetBBName("check_table"), funct, loop_lv);
		BasicBlock getAns = new BasicBlock(funct.GetBBName("get_ans"), funct, loop_lv);
		BasicBlock setAns = new BasicBlock(funct.GetBBName("set_ans"), funct, loop_lv);
		BasicBlock normalCall = new BasicBlock(funct.GetBBName("normal_call"), funct, loop_lv);
		BasicBlock afterCall = new BasicBlock(funct.GetBBName("after_call"), funct, loop_lv);
		
		funct.bbs.list.InsertAfter(beforeCall, checkTable);
		funct.bbs.list.InsertAfter(checkTable, getAns);
		funct.bbs.list.InsertAfter(getAns, setAns);
		funct.bbs.list.InsertAfter(setAns, normalCall);
		funct.bbs.list.InsertAfter(normalCall, afterCall);
		
		Reg retLocal = funct.GetReserveReg("retValAddr");
		Alloca alloca = new Alloca(retLocal); alloca.blk = funct.bbs.list.Head();
		funct.bbs.list.Head().quads.add(0, alloca);
		
		// split before call to afterCall, also, delete call from before call.
		SplitBlock(beforeCall, afterCall, call);
		
		// do branch in checkTable.
		CheckRange(beforeCall, arg, checkTable, normalCall);
		
		// check whether the ans is in table.
//		Reg content = CheckTableValid(checkTable, arg, getAns, setAns);
		//////////////////////////////////////////////////////
		Reg tablePtr = funct.GetTmpReg();
		Reg offset = funct.GetTmpReg();
		Reg ansAddr = funct.GetTmpReg();
		Reg tableContent = funct.GetTmpReg();
		
		checkTable.quads.add(new Load(checkTable, tablePtr, funct2tablePtr.get(funct.name)));
		checkTable.quads.add(new Binary(checkTable, offset, Binary.Op.SHL, arg, MakeInt(3)));
		checkTable.quads.add(new Binary(checkTable, ansAddr, Binary.Op.ADD, tablePtr, offset));
		checkTable.quads.add(new Load(checkTable, tableContent, ansAddr));
		checkTable.quads.add(new Branch(checkTable, tableContent, getAns, setAns));
		
		///////////////////////////////////////////////////////
		// get ans in getAns.
		getAns.quads.add(new Store(getAns, retLocal, tableContent));
		getAns.quads.add(new Jump(getAns, afterCall));
		
		////////////////////////////////////////////////////////
		// setAns call the function, and store the result in table, and jump to after.
		Reg setansRet = funct.GetTmpReg();
		List<IrValue> args = new LinkedList<>(); args.add(arg);
		setAns.quads.add(new Call(setAns, funct.name, setansRet, args));
		setAns.quads.add(new Store(setAns, ansAddr, setansRet));
		setAns.quads.add(new Store(setAns, retLocal, setansRet));
		setAns.quads.add(new Jump(setAns, afterCall));
		
		//////////////////////////////////////////////////////////
		Reg normalRet = funct.GetTmpReg();
		List<IrValue> nArgs = new LinkedList<>(); nArgs.add(arg);
		normalCall.quads.add(new Call(normalCall, funct.name, normalRet, nArgs));
		normalCall.quads.add(new Store(normalCall, retLocal, normalRet));
		normalCall.quads.add(new Jump(normalCall, afterCall));
		
		///////////////////////////////////////////////////////////
		afterCall.quads.add(0, new Load (afterCall, call.ret, retLocal));
	}
	
	private void CheckRange(BasicBlock beforeCall, IrValue arg, BasicBlock checkTable, BasicBlock normalCall) {
		IrFunct funct = beforeCall.parentFunct;
		
		Reg lt = funct.GetTmpReg();
		Reg gt = funct.GetTmpReg();
		Reg and = funct.GetTmpReg();
		
		Binary ltBin = new Binary(lt, Binary.Op.LT, arg, MakeInt(tableSize));
		Binary gtBin = new Binary(gt, Binary.Op.GE, arg, MakeInt(0));
		Binary andBin = new Binary(and, Binary.Op.AND, lt, gt);
		Branch br = new Branch(and, checkTable, normalCall);
		
		ltBin.blk = gtBin.blk = andBin.blk = br.blk = beforeCall;
		beforeCall.quads.add(ltBin);
		beforeCall.quads.add(gtBin);
		beforeCall.quads.add(andBin);
		beforeCall.quads.add(br);
	}
	
	private void SplitBlock(BasicBlock before, BasicBlock after, Quad quad) {
		assert after.quads.size() == 0;
		
		int quadIdx = before.quads.indexOf(quad);
		before.quads.remove(quad);
		
		while (quadIdx < before.quads.size()) {
			Quad move = before.quads.get(quadIdx);
			move.blk = after;
			after.quads.add(move);
			before.quads.remove(quadIdx);
		}
	}
	
	private List<Call> QualifyAndFetch(IrFunct funct) {
		List<Call> recursions = new LinkedList<>();
		
		if (funct.regArgs.size() != 1 || !funct.retInt) {
			return recursions;
		}
		
		for (BasicBlock cur = funct.bbs.list.Head(); cur != null; cur = cur.next) {
			for (int i = 0; i < cur.quads.size(); ++i) {
				Quad quad = cur.quads.get(i);
				if (quad instanceof Call) {
					Call call = (Call) quad;
					assert !immutableFuncts.contains((call).funcName);
					if ((call).funcName.equals(funct.name)) {
						recursions.add(call);
					}
				}
			}
		}
		
		return recursions;
	}
	
	private Set<String> immutableFuncts = new HashSet<>();
	private Map<String, Set<String>> callMap = new HashMap<>();
	private void FindImmutableFunct(IrProg ir) {
		// immutable funct means, they relate to programs correctness.
		// if a function calls immutable function, itself becomes immutable.
		InitImmutable();
		ir.functs.values().forEach(x -> callMap.put(x.name, new HashSet<>()));
		for (IrFunct funct : ir.functs.values()) {
			for (BasicBlock cur = funct.bbs.list.Head(); cur != null; cur = cur.next) {
				for (int i = 0; i < cur.quads.size(); ++i) {
					Quad quad = cur.quads.get(i);
					if (quad instanceof Store && ((Store) quad).dst.name.startsWith("@"))
						immutableFuncts.add(funct.name);
					else if (quad instanceof Call) {
						assert callMap.containsKey(funct.name);
						callMap.get(funct.name).add(((Call) quad).funcName);
					}
				}
			}
		}
		
		boolean change;
		do {
			change = false;
			for (IrFunct funct : ir.functs.values()) {
				for (String callee : callMap.get(funct.name)) {
					if (immutableFuncts.contains(callee) &&
									!immutableFuncts.contains(funct.name)) {
						immutableFuncts.add(funct.name);
						change = true;
					}
				}
			}
		} while (change);
	}
	
	// malloc should also be immutable, but it is formulated as
	// a quad class type rather than a function call.
	private void InitImmutable() {
		immutableFuncts.add("_init_");
		immutableFuncts.add("main");
		immutableFuncts.add("~memset");
		immutableFuncts.add("~print");
		immutableFuncts.add("~println");
		immutableFuncts.add("~getString");
		immutableFuncts.add("~getInt");
	}
}
