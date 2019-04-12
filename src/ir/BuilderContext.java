package ir;

import ast.node.dec.ClassDec;
import ast.node.dec.FunctDec;
import ast.node.dec.VarDec;
import ir.quad.*;
import ir.structure.BasicBlock;
import ir.structure.IrFunct;
import ir.structure.Reg;
import ir.structure.StringLiteral;

import java.util.*;

/**
 * record ir building results and a bunch of utilities.
 * */
public class BuilderContext {
	
	/**
	 * ir is operated by this building context class, thus is a member of it.
	 * */
	public IrProg ir = new IrProg();
	
	/**
	 * Utilities to associate AST def to Reg of use.
	 * */
	public Map<VarDec, Reg> varTracer = new HashMap<>();
	/**
	 * Use type name to find class entity.
	 * */
	public Map<String, ClassDec> irClassTable = new HashMap<>();
	
	/**
	 * Inherited from AST, to determine whether constructor exists.
	 * It can also trace built-in function and methods.
	 * */
	public Hashtable<String, FunctDec> functTable;
	
	public IrFunct cFun;
	
	/************************* IrFunct and Method **********************/
	
	/*************************** constructor **********************************/
	public BuilderContext(Hashtable<String, FunctDec> functTable) {
		this.functTable = functTable;
	}
	
	/**
	 * Insert a function with given name into functs map.
	 * return the function entity out.
	 * */
	public IrFunct FuncGen(String name) {
		IrFunct func = new IrFunct(name);
		ir.functs.put(name, func);
		return func;
	}
	public void SetCurFunc(IrFunct func) {
		cFun = func;
	}
	
	public void EmplaceInst(Quad quad) {
//		assert !cFun.curBB.complete;
		quad.blk = cFun.curBB;
		
		if (quad instanceof Alloca)
			cFun.bbs.list.Head().quads.add(0, quad);
		else
			cFun.curBB.quads.add(quad);
		
//		if (quad instanceof Jump || quad instanceof Branch)
//			cFun.brjp.add(quad);
		
		if (quad instanceof Ret) ;
//			CompleteCurBB();
	}
	
	public void CompleteCurBB() {
//		assert !cFun.curBB.complete;
		cFun.curBB.complete = true;
	}

	
	/************************ Basic block, instruction, var naming ******************/
	/**
	 * Basic block management methods.
	 * */
	public void SetCurBB(BasicBlock bb) {
		assert bb.parentFunct == cFun;
		cFun.curBB = bb;
	}
	
	/**
	 * Create a BB after specified BB.
	 * It will be renamed to avoid naming issue.
	 * */
	public BasicBlock NewBBAfter(BasicBlock bb, String name) {
		assert cFun == bb.parentFunct;
		String renameBB = cFun.GetBBName(name);
		BasicBlock nb = new BasicBlock(renameBB, bb.parentFunct);
		
		cFun.bbs.list.InsertAfter(bb, nb);
		return nb;
	}
	
	
	/******************** control flow's break and continue ******************/
	/**
	 * Continue and break utility
	 * */
	// record continue and break target
	// use for trace continue break target.
	private Stack<BasicBlock> contStack = new Stack<>();
	private Stack<BasicBlock> breakStack = new Stack<>();
	public void RecordLoop(BasicBlock check, BasicBlock after) {
		contStack.push(check);
		breakStack.push(after);
	}
	public void ExitLoop() {
		contStack.pop();
		breakStack.pop();
	}
	public BasicBlock GetContin() {
		assert !contStack.isEmpty();
		return contStack.peek();
	}
	public BasicBlock GetBreak() {
		assert !breakStack.isEmpty();
		return breakStack.peek();
	}
	
	/*************************** global static data **********************************/
	public void AddGlobalVar(VarDec gDec, Reg gVar) {
		varTracer.put(gDec, gVar);
		ir.globals.put(gVar.name, gVar);
		assert cFun.name.equals("_init_");
//		cFun.AddLocalVar(gVar);
	}
	
	public StringLiteral TraceGlobalString(String str) {
		if (!ir.stringPool.containsKey(str))
			ir.stringPool.put(str, new StringLiteral(str));
		return ir.stringPool.get(str);
	}
	

}
