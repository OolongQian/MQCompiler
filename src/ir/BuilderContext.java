package ir;

import ast.node.dec.FunctDec;
import ast.node.dec.VarDec;
import ir.quad.Quad;
import ir.structure.BasicBlock;
import ir.structure.Function;
import ir.structure.Reg;
import ir.structure.StringLiteral;

import java.util.*;

/**
 * We want to disentangle from AST, use String as key.
 *
 * Every variable, both global and local, must be renamed to be accepted into Ir builder.
 *
 * BuilderContext is an interface of resource storage, information collection,
 * a recorder of current code generation cursor, and wrapper of function.
 * */
public class BuilderContext {
	
	/**
	 * IR's basic building blocks are functions.
	 */
	private Map<String, Function> functs = new HashMap<>();
	
	/**
	 * global information recording.
	 *
	 * Assign id for global strings. Strings are represented by stringLiteral,
	 * which contains string and its id. It is encoded as *id.
	 * Every string literal is a var, which means it is associated with a memory address.
	 * Thus it inherits from class Reg.
	 * */
	private Map<String, StringLiteral> stringPool = new HashMap<>();
	/**
	 * Use globals to record gvar, for output for interpreter.
	 * */
	private Map<String, Reg> globals = new HashMap<>();
	
	/**
	 * Utilities to associate AST def to use.
	 * */
	private Map<VarDec, Reg> varTracer = new HashMap<>();
	public void BindVarToDec(VarDec var, Reg reg) {
		varTracer.put(var, reg);
	}
	public Reg TraceVar(VarDec var) {
		return varTracer.get(var);
	}
	
	/**
	 * Inherited from AST, to determine whether constructor exists.
	 * It can also trace built-in function and methods.
	 * */
	public Hashtable<String, FunctDec> functTable;
	
	/*************************** constructor **********************************/
	public BuilderContext(Hashtable<String, FunctDec> functTable) {
		this.functTable = functTable;
	}
	
	/*************************** global static data **********************************/
	public void AddGlobalVar(VarDec gDec, Reg gVar) {
		BindVarToDec(gDec, gVar);
		globals.put(gVar.name, gVar);
	}
	public StringLiteral TraceGlobalString(String str) {
		if (!stringPool.containsKey(str))
			stringPool.put(str, new StringLiteral(str));
		return stringPool.get(str);
	}
	
	/************************* Function and Method **********************/
	/**
	 * Set current code generation context. Like a cursor.
	 * */
	public Function cFun;
	/**
	 * Insert a function with given name into functs map.
	 * */
	public Function FuncGen(String name) {
		Function func = new Function(name);
		functs.put(name, func);
		return func;
	}
	public void SetCurFunc(Function func) {
		cFun = func;
	}

	/**
	 * Get the variable representing 'this' in current function.
	 * assertion error if this function isn't method.
	 * */
	public Reg GetThis() {
		assert cFun.this_ != null;
		return cFun.this_;
	}
	
	/************************ Basic block, instruction, var naming ******************/
	/**
	 * Basic block management methods.
	 * */
	public void SetCurBB(BasicBlock bb) {
		assert bb.parentFunct == cFun;
		cFun.SetCurBB(bb);
	}
	public BasicBlock GetCurBB() {
		return cFun.GetCurBB();
	}

	/**
	 * Create a BB after specified BB.
	 * It will be renamed to avoid naming issue.
	 * */
	public BasicBlock NewBBAfter(BasicBlock bb, String name) {
		assert cFun == bb.parentFunct;
		return cFun.NewBBAfter(bb, name);
	}
	
	/**
	 * Instruction management methods.
	 * attach current BB onto quad.
	 * */
	public void EmplaceInst(Quad quad) {
		cFun.EmplaceInst(quad);
	}
	
	/**
	 * Register management methods.
	 * */
	public String RenameLocal(String name) {
		return cFun.GetLocalName(name);
	}
	
	public Reg GetTmpReg() {
		return new Reg(cFun.GetTmpName());
	}
	
	public Reg GetReserveReg(String name) {
		return new Reg(cFun.GetReserveName(name));
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
	
	/******************** CFG construction ******************/
	public void ConstructCFG() {
		functs.values().forEach(Function::ConstructCFG);
	}
	
	/******************** interface for printer ******************/
	public void Print(Printer printer) {
		stringPool.values().forEach(printer::print);
		globals.values().forEach(printer::print);
		functs.values().forEach(printer::print);
		printer.getFout().println();
	}
	
	public Map<String, Function> GetFuncts() {
		return functs;
	}
	
	/********************* pass stringPool to interpreter ***********/
	public Map<String, StringLiteral> PassStringPoolForInterp() {
		return stringPool;
	}
}
