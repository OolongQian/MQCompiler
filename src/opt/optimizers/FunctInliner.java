package opt.optimizers;

import ir.IrProg;
import ir.quad.*;
import ir.structure.*;

import java.util.*;

import static config.Config.INLINE_LEVEL;
import static ir.Utility.inlineSuffix;
import static java.lang.Math.max;

public class FunctInliner {
	
	// functs original copy used for inline. immutable because the copy isn't completely deep.
	private Map<String, IrFunct> originFuncts = new HashMap<>();
	private Map<String, InlineInfo> inlineInfo = new HashMap<>();
	private Queue<Call> inlineCall = new LinkedList<>();
	
	
	private int inlineNo = 0;
	public void FunctInline(IrProg ir) {
		RecordFunctOriginCopy(ir);
		ScanOriginFunctInfo ();
		
		for (inlineNo = 0; inlineNo < INLINE_LEVEL; ++inlineNo) {
			inlineCall.clear();
			// scan ir to find possible inline position.
			ScanInlineCall(ir);
			
			// code movement, make sure each basic block ends with a jump/branch/ret
			while (!inlineCall.isEmpty()) {
				// cur is in ir.
				Call irCall = inlineCall.remove();
				IrFunct irParent = irCall.blk.parentFunct;
				BasicBlock irCur = irCall.blk;
				// split current bb
				BasicBlock irExit = SplitBByCall(irCall);
				
				// get funct copy. increment inlineNo each time we do an inline.
				inlineSuffix = "L" + Integer.toString(inlineNo++);
				IrFunct copyFunct = FunctCopier(originFuncts.get(irCall.funcName));
				
				Reg retval = null;
				if (!irCall.ret.name.equals("@null"))
					retval = AllocaLoadRet(irCall, copyFunct, irExit);
				
				// remove call.
				assert irCur.quads.indexOf(irCall) == irCur.quads.size() - 1;
				irCur.quads.remove(irCall);
				
				// pass parameters ?
				// we can substitute actually, but do move pass first.
				for (int i = 0; i < irCall.args.size(); ++i) {
					Mov argPass = new Mov (copyFunct.regArgs.get(i), irCall.args.get(i));
					argPass.blk = irCur;
					irCur.quads.add(argPass);
				}
				
				// add jump to inform cfg builder. .
				// add jump to copy2inline's entry.
				Jump jmp2inline = new Jump(copyFunct.bbs.list.Head());
				jmp2inline.blk = irCur;
				irCur.quads.add(jmp2inline);
				
				Ret2Exit(retval, copyFunct, irExit);
				
				// inline function placement
				PlaceInlineFunction(irCur, irParent, copyFunct);
			}
		}
	}
	
	private void RecordFunctOriginCopy (IrProg ir) {
		// get original.
		inlineSuffix = "";
		ir.functs.values().forEach(x -> originFuncts.put(x.name, FunctCopier(x)));
	}
	
	// copy with name suffix on regs (except global), and basic blocks.
	// keep global and string the same.
	// keep imm distinct.
	private Map<BasicBlock, BasicBlock> BBMap = new HashMap<>();
	// association from original reg to new reg. local and tmp.
	private Map<Reg, Reg> regRecorder = new HashMap<>();
	
	private IrFunct FunctCopier (IrFunct func) {
		BBMap.clear();
		regRecorder.clear();
		
		IrFunct copyFunct = new IrFunct(func.name);
		
		// add basic block.
		// clear defaultly added entry block.
		copyFunct.bbs.list.Clear();
		// formal regs seems has little use.
		copyFunct.formalArgs.addAll(func.formalArgs);
		func.regArgs.forEach(x -> copyFunct.regArgs.add((Reg) Cpval(x)));
		// a util field used in ir building
		copyFunct.this_ = null;
		
		// create all basic blocks and save for later reference.
		for (BasicBlock cur = func.bbs.list.Head(); cur != null; cur = cur.next) {
			assert (cur.next == null) == (cur == func.bbs.list.Tail());
			String copyBlockName = cur.name + inlineSuffix;
			BasicBlock BBcopy = new BasicBlock(copyBlockName, copyFunct, new Integer(cur.loopLevel));
			BBMap.put(cur, BBcopy);
			copyFunct.bbs.list.PushBack(BBcopy);
		}
		
		for (BasicBlock cur = func.bbs.list.Head(); cur != null; cur = cur.next) {
			assert BBMap.containsKey(cur);
			BasicBlock BBcopy = BBMap.get(cur);
			for (Quad quad : cur.quads)
				BBcopy.quads.add(CopyInst(quad, BBcopy));
		}
		
		// cfg is of no use in function inline.
		// leave it defaultly empty.
		return copyFunct;
	}
	
	private IrValue Cpval (IrValue val) {
		if (val instanceof Constant)
			return new Constant(((Constant) val).GetConstant());
		if (val instanceof StringLiteral)
			return val;
		if (val instanceof Reg) {
			if (((Reg) val).name.startsWith("@"))
				return val;
			else {
				// keep virtual register consistent
				if (!regRecorder.containsKey(val))
					regRecorder.put((Reg) val, new Reg(((Reg) val).name + inlineSuffix));
				return regRecorder.get(val);
			}
		}
		assert false;
		return null;
	}
	
	private Quad CopyInst (Quad quad, BasicBlock blk) {
		Quad copyInst = null;
		if (quad instanceof Alloca){
			copyInst = new Alloca((Reg) Cpval(((Alloca) quad).var));
		}
		else if (quad instanceof Binary) {
			copyInst = new Binary ((Reg) Cpval(((Binary) quad).ans),
							((Binary) quad).op,
							Cpval(((Binary) quad).src1),
							Cpval(((Binary) quad).src2)); 
		}
		else if (quad instanceof Branch) {
			assert BBMap.containsKey(((Branch) quad).ifTrue);
			assert BBMap.containsKey(((Branch) quad).ifFalse);
			copyInst = new Branch(Cpval(((Branch) quad).cond),
							BBMap.get(((Branch) quad).ifTrue),
							BBMap.get(((Branch) quad).ifFalse));
		}
		else if (quad instanceof Call) {
			List<IrValue> cpargs = new LinkedList<>();
			((Call) quad).args.forEach(x -> cpargs.add(Cpval(x)));
			copyInst = new Call(((Call) quad).funcName, (Reg) Cpval(((Call) quad).ret), cpargs);
		}
		else if (quad instanceof Comment) {
			copyInst = new Comment(((Comment) quad).content);
		}
		else if (quad instanceof Jump) {
			assert BBMap.containsKey(((Jump) quad).target);
			copyInst = new Jump (BBMap.get(((Jump) quad).target));
		}
		else if (quad instanceof Load) {
			copyInst = new Load ( (Reg) Cpval(((Load) quad).val), (Reg) Cpval(((Load) quad).addr));
		}
		else if (quad instanceof Malloc) {
			copyInst = new Malloc((Reg) Cpval(((Malloc) quad).memAddr), Cpval(((Malloc) quad).size_));
		}
		else if (quad instanceof Mov) {
			copyInst = new Mov ((Reg) Cpval(((Mov) quad).dst), Cpval(((Mov) quad).src));
		}
		else if (quad instanceof ParallelCopy) {
			assert false;
		}
		else if (quad instanceof Phi) {
			assert false;
		}
		else if (quad instanceof Ret) {
			copyInst = new Ret (Cpval(((Ret) quad).val));
		}
		else if (quad instanceof Store) {
			copyInst = new Store ((Reg) Cpval(((Store) quad).dst), Cpval(((Store) quad).src));
		}
		else if (quad instanceof Unary) {
			copyInst = new Unary((Reg) Cpval(((Unary) quad).ans), ((Unary) quad).op, Cpval(((Unary) quad).src));
		}
		else {
			assert false;
		}
		assert copyInst != null;
		copyInst.blk = blk;
		return copyInst;
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
	
	private void ScanInlineCall(IrProg ir) {
		inlineCall.clear();
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
	
	private BasicBlock SplitBByCall (Call call) {
		BasicBlock cur = call.blk;
		BasicBlock exit = new BasicBlock(cur.name + "_exit", cur.parentFunct, cur.loopLevel);
		// list insertion is all about linked list.
		cur.parentFunct.bbs.list.InsertAfter(cur, exit);
		// give up maintain cfg. build from scratch later on.
		int index = cur.quads.indexOf(call) + 1;
		while (index < cur.quads.size()) {
			exit.quads.add(cur.quads.get(index));
			cur.quads.remove(index);
		}
		exit.quads.forEach(x -> x.blk = exit);
		return exit;
	}
	
	// handle return value. if has return value, alloca a memory to it,
	// at the head of copied function, because copied function's entry
	// dominates the use of ret.
	// return the return value handler.
	private Reg AllocaLoadRet (Call irCall, IrFunct copyFunct, BasicBlock irExit) {
		// alloca retval at copy's entry.
		Reg retval = new Reg ("ret_" + copyFunct.name + inlineSuffix);
		Alloca allocaRet = new Alloca(retval);
		allocaRet.blk = copyFunct.bbs.list.Head();
		copyFunct.bbs.list.Head().quads.add(0, allocaRet);
		// load it at the beginning of exit.
		Load loadRet = new Load (irCall.ret, retval);
		loadRet.blk = irExit;
		irExit.quads.add(0, loadRet);
		
		return retval;
	}
	
	private void Ret2Exit (Reg retval, IrFunct copyFunct, BasicBlock irExit) {
		// scan function to be inlined, replace return by a jump to exit.
		for (BasicBlock BBinl = copyFunct.bbs.list.Head(); BBinl != null; BBinl = BBinl.next) {
			for (int i = 0; i < BBinl.quads.size(); ++i) {
				Quad quad = BBinl.quads.get(i);
				if (quad instanceof Ret) {
					assert BBinl.quads.indexOf(quad) == BBinl.quads.size() - 1;
					// remove ret.
					BBinl.quads.remove(quad);
					
					Ret ret = (Ret) quad;
					// if it need to return a value, add an move statement.
					if (retval != null) {
						// add a store to it.
						Store storeRet;
						if (ret.val instanceof Reg && ((Reg) ret.val).name.equals("@null"))
							storeRet = new Store (retval, new Constant(0));
						else
							storeRet = new Store (retval, ret.val);
						storeRet.blk = BBinl;
						BBinl.quads.add(i++, storeRet);
					}
					Jump jmp2exit = new Jump (irExit);
					jmp2exit.blk = BBinl;
					BBinl.quads.add(i++, jmp2exit);
				}
			}
		}
	}
	
	private void PlaceInlineFunction(BasicBlock marker, IrFunct irFunct, IrFunct copyFunct) {
		BasicBlock cursor = marker;
		BasicBlock nextInline;
		
		for (BasicBlock BBinl = copyFunct.bbs.list.Head(); BBinl != null; BBinl = nextInline) {
			BBinl.parentFunct = irFunct;
			nextInline = BBinl.next;
			irFunct.bbs.list.InsertAfter(cursor, BBinl);
			cursor = BBinl;
		}
	}
}


class InlineInfo {
	Integer instNo = 0;
	Integer maxLoop = 0;
	Integer callNo = 0;
	
	boolean HeuristicInline () {
		return instNo < 30 && callNo <= 2;
	}
}