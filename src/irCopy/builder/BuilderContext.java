package ir.builder;

import ast.node.dec.class_.ClassDec;
import ast.node.dec.function.FunctDec;
import ast.node.dec.function.MethodDec;
import ast.node.dec.variable.VarDec;
import ast.typeref.VarTypeRef;
import ir.quad.Quad;
import ir.reg.GlobalReg;
import ir.reg.Reg;
import ir.reg.StringLiteral;
import ir.util.BasicBlock;
import ir.util.FunctCtx;
import jdk.nashorn.internal.codegen.MethodEmitter;
import semantic.FormatCheckVisitor;

import java.lang.reflect.Method;
import java.util.*;

public class BuilderContext {

  public FunctDec FuncLookup(String name) {
    return functTable.getOrDefault(name, null);
  }

  /**
   * This method is ugly because it should be encapsulated aside from VarTypeRef itself.
   * */
  public int CalTypeSize(VarTypeRef type) {
    return type.GetTypeSpace();
  }

  public void BindVarToAddr(VarDec var, Reg reg) {
    varTracer.put(var, reg);
  }

  public Reg TraceRegAddr(VarDec var) {
    return varTracer.get(var);
  }
  
  public void BindThisToMethod(MethodDec method, Reg reg) {
  	thisTracer.put(method, reg);
  }
  
  public Reg TraceThisAddr(MethodDec method) {
  	return thisTracer.get(method);
  }

  public void InsertQuadFront(BasicBlock b, Quad q) {
    b.quads.add(0, q);
  }

  public void AddFunct(FunctCtx func) {
    functs.put(func.name, func);
  }

  public void AddGlobalVar(GlobalReg reg) {
  	globalRegs.put(reg.getText(), reg);
  }

  public BasicBlock GetCurBB() {
    return curbb;
  }

  public void SetCurFunc(String name) {
    curFunc = functs.get(name);
  }

  public FunctCtx GetCurFunc() {
    return curFunc;
  }

  public void SetCurBB(BasicBlock bb) {
    curbb = bb;
  }

  public void EmplaceInst(Quad quad) {
    curbb.quads.add(quad);
  }

  public StringLiteral TraceGlobalString(String str) {
    if (!globalStringPool.containsKey(str))
      globalStringPool.put(str, new StringLiteral(str));
    return globalStringPool.get(str);
  }

  // use for trace continue break target.
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

  public void Print(Printer printer) {
//  	globalRegs.values().forEach(printer::print);
	  // global variable initialization has been done in functs's -globalInit.
    globalStringPool.values().forEach(printer::print);
    functs.values().forEach(printer::print);
  }

	// collect global variable allocation, and make an extra function called -globalInit
  public void WrapGlobalVarInit() {
    FunctCtx gInit = new FunctCtx("-globalInit", null);
	  for (GlobalReg reg : globalRegs.values()) {
		  BasicBlock head = gInit.GetNewBB(reg.getText());
		  head.quads.addAll(reg.GetInit());
	  }
	  AddFunct(gInit);
  }
  
  public BuilderContext(FormatCheckVisitor vis) {
    this.classTable = vis.classTable;
    this.functTable = vis.functTable;
  }


  private Map<String, GlobalReg> globalRegs = new HashMap<>();
  public Map<String, StringLiteral> globalStringPool = new HashMap<>();
  private Map<String, FunctCtx> functs = new HashMap<>();
  private Map<VarDec, Reg> varTracer = new HashMap<>();
  private Map<MethodDec, Reg> thisTracer = new HashMap<>();

  public Hashtable<String, ClassDec> classTable;
  public Hashtable<String, FunctDec> functTable;

  private FunctCtx curFunc;
  private BasicBlock curbb;

  // record continue and break target
  private Stack<BasicBlock> contStack = new Stack<>();
  private Stack<BasicBlock> breakStack = new Stack<>();
}
