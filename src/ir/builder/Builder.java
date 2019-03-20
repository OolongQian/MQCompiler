package ir.builder;

import ast.node.dec.Dec;
import ast.node.dec.class_.ClassDec;
import ast.node.dec.function.FunctDec;
import ast.node.dec.function.MethodDec;
import ast.node.dec.variable.GlobalVarDec;
import ast.node.dec.variable.LocalVarDec;
import ast.node.dec.variable.VarDec;
import ast.node.exp.*;
import ast.node.exp.binary.ArithBinaryExp;
import ast.node.exp.binary.LogicBinaryExp;
import ast.node.exp.literal.BoolLiteralExp;
import ast.node.exp.literal.IntLiteralExp;
import ast.node.exp.literal.StringLiteralExp;
import ast.node.exp.lvalue.ArrayAccessExp;
import ast.node.exp.lvalue.FieldAccessExp;
import ast.node.exp.lvalue.ThisExp;
import ast.node.exp.lvalue.VarExp;
import ast.node.exp.unary.PrefixExp;
import ast.node.exp.unary.SuffixExp;
import ast.node.prog.Prog;
import ast.node.stm.*;
import ast.typeref.VarTypeRef;
import ir.quad.*;
import ir.quad.Binary.Op;
import ir.reg.*;
import ir.util.BasicBlock;
import ir.util.FunctCtx;
import ir.util.StringHandler;
import semantic.AstTraverseVisitor;

import java.util.*;

import static ir.builder.Config.INT_SIZE;
import static ir.quad.Binary.Op.*;
import static ir.quad.Unary.Op.BITNOT;
import static ir.quad.Unary.Op.NEG;


/**
 * Documentation for IrAddr -- GetNodeAddr() and IrVal for Exp.
 *
 * The way we build IR is hanging around on AST, hence we want to extract information by going around AST.
 * Hence we want to synthesis information from children to ancestor.
 *
 * The value recorded on AST node can have multiple types of interpretations:
 * such as,
 *    Literal: Immediate value, appears as immediate int in IR.
 *    Arithmetic intermediate value: temp registers containing intermediate value between calculation, say, a++.
 *    Address value: just like ++a. After doing ++a, what we want is... OK, we may want both its value and its address.
 *      example:
 *        int c = ++b; after doing ++b, we need its value.
 *        int c = ++(++b), after doing ++b, we want its address so that we can load it again.
 *
 *        So we need a method for us to record both information, and not all node kind have both data recorded on it.
 * The value and address info only need to be recorded on ExpNode of AST.
 *
 * But you know, sometimes when we are in Arithmetic calculation, what we have is uncertain, because it can be an
 * Immediate or a variable addr, so we want to integrate it in a single function. Let call it GetArithResult.
 *
 * If a function is builtin type, add a prefix ~.
 * If a function is method type, format it as -className#methodName.
 *
 * One important thing about control flow logic evaluation, where to put the branch quad?
 * Is it emplaced in child node Accept or after the call is returned ?
 *
 *
 * -- short-circuit evaluation:
 *    short-circuit evaluation provides an efficient way for logicBinaryExpr, i.e. && and ||.
 *
 *    it consists of two main situations, 1st is calculating values. It branches to different paths, and using a phi node to merge
 *    the values calculated, using phi's value to carry on expr value evaluation. The branch is pushed when encountering leaf of
 *    that structure.
 *
 *    2nd is control flow branching, taking on the roles of 'cond' in ifStm and loops. It jumps right to the corresponding next or
 *    merge basic block when branching can be determined.
 *
 *    1st can live inside the instance of 2nd.
 *
 *    ***** Implementation manual :
 *      LogicBinExpr : if it's a direct child of 'cond', its jump targets must have been set. if its targets are uninitialized, it
 *                    means its branch targets merge here, it can assign itself.
 *                    Now we generate code for a && b, FIXME : it can be removed certainly.
 *                    if its child is no longer control flow evaluation expression, that will be the time to generate code and
 *                    send back to this leaf control flow expr to stamp a branch Instruction.
 *      Loop 'cond' : if LogicBinExpr is its direct son, set jump targets directly, and let him automates the branch and jump.
 *                    otherwise implies a logic value evaluation case, just accept down cond, and receive a IrValue as a branch
 *                    condition value.
 *      Exprs : For '!' operator, if it's within a control flow evaluation process, it helps push down jump targets. Otherwise it
 *              emplace a XOR operation. For other expressions, do it as normal -- don't need to worry about LogicBinExpr (the
 *              beginning of control flow short-circuit evaluation), because it has handled 'uninitialized jump targets issue'
 *              itself.
 *
 *      ** pay attention to current basic block problem. Assume the least guarantee of current BB recovery.
 *
 *      !!! NOTE : we haven't implemented operator '!' currently, because that will not be important.
 * */
public class Builder extends AstTraverseVisitor<Void> {

  private BuilderContext ctx;

  public Builder(BuilderContext ctx) {
    this.ctx = ctx;
  }

  public BuilderContext getCtx() {
    return ctx;
  }

  public void build(Prog prog) {
  	// create globalInit function
	  // use this context to generate global vars
    visit(prog);
    // collect global variable allocation, and make an extra function called -globalInit
	  ctx.WrapGlobalVarInit();
  }


  /**
   * The thing is that, we won't make two globalReg with the same name, but
   * frequently make alias local vars, needing a table to record its names.
   * */
  private HashMap<String, Integer> namer = new HashMap<>();
  private Reg MakeReg(String name) {
    // mode 1: global, 2: local, 3: member, 4: temp
    return new GlobalReg('@' + name);
  }

  private IntLiteral MakeInt(int val) {
  	return new IntLiteral(val);
  }
  
  private void Mumble(String str) {
  	ctx.EmplaceInst(new Comment(str));
  }
  
  /**
   * Automate and simplify different situations in branch and jump quad generation.
   * */
  private Quad NewBranchJump(IrValue cond, BasicBlock T, BasicBlock F) {
    // if condition is constant, just analyze and jump.
    if (cond instanceof IntLiteral) {
      BasicBlock target = (((IntLiteral) cond).val == 1) ? T : F;
      return new Jump(target);
    }
    // if two targets are the same, just jump.
    if (T == F) {
      return new Jump(T);
    }
    // do non-trivial branch
    return new Branch(cond, T, F);
  }
  /**
   * This function check whether the node is LogicBinaryExp or UnaryExp with operator '!'
   * */
  private boolean isLogicEval(Exp node) {
    return node instanceof LogicBinaryExp || node instanceof PrefixExp && ((PrefixExp) node).op.equals("!");
  }

  /**
   * we can't allocate int or bool.
   * I don't know whether it's valid to new a string.
   * NOTE : I assume we cannot.
   * Thus here, we can only create new object.
   *
   * Basically, we just allocate a piece of space on heap, and
   * after getting its address from system call, invoke constructor on it.
   * */
  private Reg MakeNewNonArray(VarTypeRef type) {
    // allocate a tmp register to hold the address return by heap malloc
    Reg objAddr = ctx.GetCurFunc().GetTmpReg();
    IntLiteral size_ = MakeInt(ctx.CalTypeSize(type));
    ctx.EmplaceInst(new Malloc(objAddr, size_));
    // invoke constructor after memory space mallocating.
    // since it is a user-defined object, it has a classDec, and maybe a constructor.
    ClassDec userClass = type.baseType;
    String ctor = AddPrefix(userClass, type.typeName);
    if (ctx.FuncLookup(ctor) != null) {
      ctx.EmplaceInst(new Call(ctor, MakeReg("null"), objAddr));
    }
    // return the pointer to the constructed piece of objory.
    return objAddr;
  }

  private Reg MakeNewArray(Queue<IrValue> dims) {
  	// problem case: T a[][][] = new T[10][20][].
	  // this issue perfectly helps me understand what's going on when array is created.
	  // NOTE : the key is to forget anything about reference, dereference, steps, hops and so on...
	  // NOTE : clearly understand what LOAD and STORE means, understand what is a variable and what is a temp register.
	  // first, make it clear that (new exp) returns a temp register holding something, which will be stored into the very memory address represented by a.
	  // what we are essentially doing is to allocate something in the memory, and store some info so that a can find these allocated memory again.
	  // so what is held by the temp register is the address of the allocated space.
	  
	  // NOTE : only pointer type can be newed as array in JAVA, hence dimension info is all we need.
	  
	  // recursion stopper.
	  if (dims.isEmpty())
	  	return MakeReg("null");
	  
	  // allocate a temp register to hold the allocated memory address.
	  Reg arrAddr = ctx.GetCurFunc().GetTmpReg();
	  // malloc some memory with suitable dimension, and let arrAddr takes on its address.
	  IrValue dim = dims.poll();
	  // add 1 to dim to hold array size.
	  Reg exDim = ctx.GetCurFunc().GetTmpReg();
	  ctx.EmplaceInst(new Binary(exDim, ADD, dim, MakeInt(1)));
	  // multiply INT_SIZE to get the actual offset.
	  Reg dimByte = ctx.GetCurFunc().GetTmpReg();
	  ctx.EmplaceInst(new Binary(dimByte, MUL, exDim, MakeInt(INT_SIZE)));
	  // malloc mem space, let arrAddr take on the address.
	  ctx.EmplaceInst(new Malloc(arrAddr, dimByte));
	  // record array dimension after recording, right at the head of malloc_ed mem.
	  ctx.EmplaceInst(new Store(arrAddr, dim));
	  
	  // if there's no other dimensions to create, stop and return.
	  if (dims.isEmpty()) {
		  return arrAddr;
	  }
	  
	  // otherwise, construct a loop to create pointers for inner dimensions.
	  // collect and store the addresses malloc_ed during sub-problems into currently malloc_ed address.
	  
	  // first, get a endMarker.
	  Reg arrEndMark = ctx.GetCurFunc().GetTmpReg();
	  Reg endByte = ctx.GetCurFunc().GetTmpReg();
	  ctx.EmplaceInst(new Binary(endByte, MUL, dim, MakeInt(INT_SIZE)));
	  ctx.EmplaceInst(new Binary(arrEndMark, ADD, arrAddr, endByte));
	  
	  // before enter the loop, initialize a loop iterator.
	  Reg iter = ctx.GetCurFunc().GetLocReg("nIter");
	  FunctCtx func = ctx.GetCurFunc();
	  ctx.InsertQuadFront(func.GetHeadBB(), new Alloca(iter, 4));
	  // store the address of the first element to it.
	  Reg initAddr = ctx.GetCurFunc().GetTmpReg();
	  IrValue initByte = MakeInt(INT_SIZE);
	  ctx.EmplaceInst(new Binary(initAddr, ADD, arrAddr, initByte));
	  ctx.EmplaceInst(new Store(iter, initAddr));
	
	  BasicBlock cond = func.NewBBAfter(ctx.GetCurBB(), "new_cond");
	  BasicBlock then = func.NewBBAfter(cond, "new_then");
	  BasicBlock after = func.NewBBAfter(then, "new_after");
	  // when we haven't exceed the array to be constructed, continue to stay in then BB.
	  ctx.SetCurBB(cond);
	  Reg curAddr = ctx.GetCurFunc().GetTmpReg();
	  ctx.EmplaceInst(new Load(curAddr, iter));
	  Reg cmp = ctx.GetCurFunc().GetTmpReg();
	  ctx.EmplaceInst(new Binary(cmp, GT, curAddr, arrEndMark));
	  // if exceed, break loop, else continue looping.
	  ctx.EmplaceInst(new Branch(cmp, after, then));
	  
	  // now we've entered the loop, we get sub-addr and store it in the address specified by curAddr.
	  ctx.SetCurBB(then);
	  Reg subArr = MakeNewArray(dims);
	  // FIXME : not sure whether recursion changes curBB.
	  ctx.EmplaceInst(new Store(curAddr, subArr));
	  // increment curAddr and store it in iter.
	  Reg increAddr = ctx.GetCurFunc().GetTmpReg();
	  ctx.EmplaceInst(new Binary(increAddr, ADD, curAddr, MakeInt(INT_SIZE)));
	  ctx.EmplaceInst(new Store(iter, increAddr));
	  // redirect to condition check.
	  ctx.EmplaceInst(new Jump(cond));
	  ctx.SetCurBB(after);
	  return arrAddr;
  }

  
  private IrValue GetArithResult(Exp node) {
    if (node.getIrValue() == null) {
      Reg addr = node.getIrAddr();
      assert addr != null;

      // load the value into a tmpVal.
      Reg tmpVal = ctx.GetCurFunc().GetTmpReg();
      ctx.EmplaceInst(new Load(tmpVal, addr));
      // set value
      node.setIrValue(tmpVal);
      return tmpVal;
    }
    return node.getIrValue();
  }

  @Override
  public Void visit(IntLiteralExp node) {
    node.setIrValue(MakeInt(node.val));
    return null;
  }

  @Override
  public Void visit(BoolLiteralExp node) {
    node.setIrValue((node.val) ? MakeInt(1) : MakeInt(0));
    return null;
  }

  /**
   * NOTE : In bread bro's implementation, here we have lots of things to do,
   * but here we push this complexity down to the assembly phase.
   *
   * StringLiteral's name in IR is modeled as a variable whose value is the address
   * of the headAddr of a string residing in mem (static/heap).
   */
  @Override
  public Void visit(StringLiteralExp node) {
    // get the string address from which we could find the string head pointer.
    // NOTE why here is strAddr, see above explanation.
	  String cleanStr = StringHandler.unescape(node.value);
    Reg strAddr = ctx.TraceGlobalString(cleanStr);
    node.setIrAddr(strAddr);
    return null;
  }

  @Override
  public Void visit(NullExp node) {
    node.setIrValue(MakeReg("null"));
    return null;
  }

  /**
   * Make registers that binds to memory
   * */
  private Reg MakeLocReg(String name) {
  	return ctx.GetCurFunc().GetLocReg(name);
  }
  
  /**
   * Make tmp registers.
   * */
  private Reg MakeTmpReg() {
  	return ctx.GetCurFunc().GetTmpReg();
  }
  
  /**
   * Alloca a specially marked global register, holding
   * pointer or value for suitable type.
   * If has initial exp, do it in a separated function,
   * because only function can have quad.
   */
  private void AddGlobalVar(VarDec node) {
    GlobalReg addr = (GlobalReg) MakeReg(node.varName);
    addr.EmplaceInit(new Alloca(addr, MakeInt(INT_SIZE)));

    if (node.inital != null) {
      // add a functctx to hold the initialization quad
      String initName = "_init_" + node.varName;
      FunctCtx func = new FunctCtx(initName, null);
	    // add initial function to functs.
	    ctx.AddFunct(func);
      // setBB directly because now we are outside of functions.
      ctx.SetCurBB(func.GetNewBB(null));
      ctx.SetCurFunc(func.name);
      node.inital.Accept(this);
      // store initial value into reg
      IrValue initAddr = GetArithResult(node.inital);
      ctx.EmplaceInst(new Store(addr, initAddr));
      ctx.EmplaceInst(new Ret(MakeReg("null")));
      // record initial and alloca info directly on global variable's allocated pointer, addr.
      addr.EmplaceInit(new Call(func.name));

    }
    // record global into context
    ctx.AddGlobalVar(addr);
    // bind variable dec, find addr through declaration.
    ctx.BindVarToAddr(node, addr);
  }

  @Override
  public Void visit(GlobalVarDec node) {
    node.varDecs.forEach(this::AddGlobalVar);
    return null;
  }

  /**
   * I don't need to record type information on IR,
   * because I have had AST already.
   *
   * See notes, this is complicated.
   */
  @Override
  public Void visit(FunctDec node) {
    // create FunctCtx, which needs args string.
    List<String> args = new LinkedList<>();
    node.arguments.varDecs.forEach(x -> args.add(x.varName));
		List<Reg> tmpArgs = new LinkedList<>();
    // append and set cur function
	  // functCtx's args are to be assigned as tmpArgs.
    FunctCtx func = new FunctCtx(node.functName, null);
    ctx.AddFunct(func);
    ctx.SetCurFunc(func.name);
    ctx.SetCurBB(func.GetNewBB(null));

    // start generate function code.
    // Alloca a memory space for 'this'.
    if (node instanceof MethodDec) {
    	Reg regAddr = MakeLocReg("this");
    	Reg tmpReg = MakeTmpReg();
    	// record tmpRegs
    	tmpArgs.add(tmpReg);
      ctx.EmplaceInst(new Alloca(regAddr, MakeInt(INT_SIZE)));
      ctx.EmplaceInst(new Store(regAddr, tmpReg));
      ctx.BindThisToMethod((MethodDec) node, regAddr);
    }

    // Alloca tmp args to actual registers. First alloca, then store.
	  for (int i = 0; i < args.size(); ++i) {
		  Reg regAddr = MakeLocReg(args.get(i));
		  Reg tmpReg = MakeTmpReg();
		  tmpArgs.add(tmpReg);
		  ctx.EmplaceInst(new Alloca(regAddr, MakeInt(INT_SIZE)));
		  ctx.EmplaceInst(new Store(regAddr, tmpReg));
		  ctx.BindVarToAddr(node.arguments.varDecs.get(i), regAddr);
	  }
	  
	  func.args = tmpArgs;
    // visit the rest of the statement.
    // NOTE : avoid generate dead code.
    for (Stm stm : node.functBody) {
      stm.Accept(this);
      if (ctx.GetCurBB().IsCompleted())
        break;
    }
    if (!ctx.GetCurBB().IsCompleted())
      ctx.EmplaceInst(new Ret(MakeReg("null")));
    return null;
  }

  // methodDec has been incorporated into functionDec
  @Override
  public Void visit(MethodDec node) {
    visit((FunctDec) node);
    return null;
  }

  /**
   * Note : we do nothing to class fields, because that condition can be
   * note : safely handled by getElemPtr.
   */
  @Override
  public Void visit(ClassDec node) {
    node.method.forEach(x -> x.Accept(this));
    return null;
  }

  @Override
  public Void visit(Prog node) {
  	// first collect global
	  for (Dec child : node.children) {
		  if (child instanceof GlobalVarDec)
		  	child.Accept(this);
	  }
	  for (Dec child : node.children) {
		  if (!(child instanceof GlobalVarDec))
			  child.Accept(this);
	  }
    return null;
  }

  /**
   * For local variable.
   * Do nothing to class fields.
   * Do nothing to global variable.
   */
  @Override
  public Void visit(LocalVarDec node) {
    node.varDecs.forEach(x -> x.Accept(this));
    return null;
  }

  @Override
  public Void visit(VarDec node) {
    FunctCtx func = ctx.GetCurFunc();
    Reg addr = MakeLocReg(node.varName);
    ctx.InsertQuadFront(func.GetHeadBB(), new Alloca(addr, MakeInt(INT_SIZE)));

    if (node.inital != null && !node.inital.varTypeRef.isNull()) {
      node.inital.Accept(this);
      IrValue initVal = GetArithResult(node.inital);
      ctx.EmplaceInst(new Store(addr, initVal));
    } else if (node.varType.isInt() || node.varType.isBool()) {
      ;
    }
    else {
      // emplace a null mark in empty pointer slot.
      ctx.EmplaceInst(new Store(addr, MakeReg("null")));
    }

    ctx.BindVarToAddr(node, addr);
    return null;
  }

  /**
   * Now we are clear, since every variable expression is linked with a memory address,
   * we use ctx.TraceRegAddr to trace it, and record it on node.
   * */
  @Override
  public Void visit(VarExp node) {
    // NOTE : shouldn't load it at this stage.
    // record the address bounded with this variable onto the node.
    VarDec var = node.varDec;
    Reg addr = ctx.TraceRegAddr(var);
    node.setIrAddr(addr);
    return null;
  }

  /**
   * This is a really brilliant thing to do.
   *
   * NOTE : set value here because what is returned is the 'address' of the created instance already.
   * */
  @Override
  public Void visit(CreationExp node) {
    if (!node.varTypeRef.isArray()) {
      Reg instPtr = MakeNewNonArray(node.varTypeRef);
      // this node's value is a register containing a pointer to the obj.
      node.setIrValue(instPtr);
    }
    else {
      // array creation need type and dimension info.
      // use a queue to hold array dimension because first in first out.
      node.varTypeRef.Accept(this);
      Queue<IrValue> dimInfo = new ArrayDeque<>();
      GetArrayDimension(node.varTypeRef, dimInfo);
      // note : In Java, we regard array as pointers, thus no explicit class info is required.
      Reg arrPtr = MakeNewArray(dimInfo);
      node.setIrValue(arrPtr);
    }
    return null;
  }


  /**
   * Utility function used by visit(CreationExp node)
   * */
  private void GetArrayDimension(VarTypeRef type, Queue<IrValue> dims)  {
    // varTypeRef has been traversed.
    if (type.innerType == null)
      return;

    // we don't need to care about @null dimension and further ones.
//    IrValue curDim = (type.dim == null) ? MakeReg("null", 1) : GetArithResult(type.dim);
//    dims.add(curDim);
	  if (type.dim != null) {
		  dims.add(GetArithResult(type.dim));
		  GetArrayDimension(type.innerType, dims);
	  } else {
	  	return;
	  }
  }

  /**
   * inspired from Chen Lequn.
   */
  @Override
  public Void visit(IfStm node) {
	
	  BasicBlock then = ctx.GetCurFunc().NewBBAfter(ctx.GetCurBB(), "then");
	  BasicBlock merge = ctx.GetCurFunc().NewBBAfter(then, "merge");
	  BasicBlock else_ = (node.elseBody != null) ?
					  ctx.GetCurFunc().NewBBAfter(then, "else") : null;
    
    if (node.condition instanceof LogicBinaryExp) {
      // node.condition automates jump.
	    // set jump targets and accept, everything about short-circuit should have been done.
      node.condition.ifTrue = then;
      node.condition.ifFalse = (else_ != null) ? else_ : merge;
      node.condition.Accept(this);
    }
    else {
      // merge results and jump manually.
	    // accept condition to get the cond result for branch.
	    node.condition.Accept(this);
	    IrValue brCond = GetArithResult(node.condition);
	    BasicBlock ifFalse = (node.elseBody != null) ? else_ : merge;
	    // emplace branch.
	    ctx.EmplaceInst(new Branch(brCond, then, ifFalse));
	    
    }
    
	  // then irGen, assume no curBB recovery.
	  ctx.SetCurBB(then);
	  node.thenBody.Accept(this);
	  // emplace 'jump' from then to merge if necessary.
	  if (else_ != null) ctx.EmplaceInst(new Jump(merge));
	  // else irGen
	  if (node.elseBody != null) {
		  ctx.SetCurBB(else_);
		  node.elseBody.Accept(this);
	  }
	  // set BB to merge
	  ctx.SetCurBB(merge);
	  
    return null;
  }

  
  
  @Override
  public Void visit(WhileStm node) {
	
	  BasicBlock cond = ctx.GetCurFunc().NewBBAfter(ctx.GetCurBB(), "cond");
	  BasicBlock step = ctx.GetCurFunc().NewBBAfter(cond, "step");
	  BasicBlock after = ctx.GetCurFunc().NewBBAfter(step, "after");
		
    ctx.SetCurBB(cond);
    if (node.condition instanceof LogicBinaryExp) {
      // node.condition automates jump.
	    node.condition.ifTrue = step;
	    node.condition.ifFalse = after;
	    node.condition.Accept(this);
    }
    else {
      // merge results and jump manually.
	    node.condition.Accept(this);
	    IrValue brCond = GetArithResult(node.condition);
	    // emplace branch
	    ctx.EmplaceInst(new Branch(brCond, step, after));
    }
    
    // record loop info for break and continue
    ctx.RecordLoop(cond, after);

    ctx.SetCurBB(step);
    node.whileBody.Accept(this);
	  ctx.EmplaceInst(new Jump(cond));
    
    ctx.ExitLoop();
    ctx.SetCurBB(after);
    return null;
  }

  @Override
  public Void visit(ForStm node) {
  	// forInit is executed before loop BB
    if (!node.forControl.isInitNull()) {
      if (node.forControl.initIsDec) {
        // visit a for dec
        node.forControl.initDec.Accept(this);
      } else {
        // visit a series of expr
        node.forControl.initExps.forEach(x -> x.Accept(this));
      }
    }
	
	  BasicBlock check = ctx.GetCurFunc().NewBBAfter(ctx.GetCurBB(), "cond");
	  BasicBlock step = ctx.GetCurFunc().NewBBAfter(check, "step");
	  BasicBlock after = ctx.GetCurFunc().NewBBAfter(step, "after");
   
		ctx.SetCurBB(check);
    if (node.forControl.check instanceof LogicBinaryExp) {
    	node.forControl.check.ifTrue = step;
    	node.forControl.check.ifFalse = after;
    	node.forControl.check.Accept(this);
    }
    else {
    	node.forControl.check.Accept(this);
    	IrValue brCond = GetArithResult(node.forControl.check);
    	ctx.EmplaceInst(new Branch(brCond, step, after));
    }
    
    ctx.RecordLoop(check, after);

    ctx.SetCurBB(step);
    node.forBody.Accept(this);
    node.forControl.updateExps.forEach(x -> x.Accept(this));
    ctx.EmplaceInst(new Jump(check));

    ctx.ExitLoop();
    ctx.SetCurBB(after);
    return null;
  }

  @Override
  public Void visit(ContinueStm node) {
    BasicBlock contin = ctx.GetContin();
    ctx.EmplaceInst(new Jump(contin));
    return null;
  }

  @Override
  public Void visit(BreakStm node) {
    BasicBlock after = ctx.GetBreak();
    ctx.EmplaceInst(new Jump(after));
    return null;
  }

  /**
   * If node.objInstance is a literal, its irVal is the value.
   * if it's a variable whose value is int or bool, irVal is the register.
   * if it's an array or string or user-defined type, it's a register
   * containing the pointer points to the object instance.
   */
  // NOTE : I deprecate bread bro's method.
//    Reg addr = GetElementAddr(node.objInstance);
  @Override
  public Void visit(SuffixExp node) {
    node.objInstance.Accept(this);
    // NOTE : it is addr because node is a lvalue.
    Reg addr = node.objInstance.getIrAddr();

    // get values.
    IrValue oldVal = GetArithResult(node.objInstance);
    Reg increVal = ctx.GetCurFunc().GetTmpReg();

    ctx.EmplaceInst(new Binary(increVal, ADD, oldVal, MakeInt(1)));
    ctx.EmplaceInst(new Store(addr, increVal));

    // set IrAddr to null just for clarity, showing that it cannot be queried, otherwise leads to assertion failure.
    node.setIrAddr(null);
    node.setIrValue(oldVal);
    return null;
  }

  @Override
  public Void visit(PrefixExp node) {
    node.objInstance.Accept(this);

    // it has addr because it is a lvalue.
    Reg addr = node.objInstance.getIrAddr();
    IrValue oldVal = GetArithResult(node.objInstance);

    switch (node.op) {
      case "++": case "--":
        // similar to Suffix
        Reg newVal = ctx.GetCurFunc().GetTmpReg();
        Op op = (node.op.equals("++")) ? ADD : SUB;
        ctx.EmplaceInst(new Binary(newVal, op, oldVal, MakeInt(1)));
        ctx.EmplaceInst(new Store(addr, newVal));
        // prefix is still a lvalue.
        node.setIrValue(newVal);
        node.setIrAddr(addr);
        break;
      case "~": case "-":
        Reg newVal_ = ctx.GetCurFunc().GetTmpReg();
        Unary.Op op_ = (node.op.equals("~")) ? BITNOT : NEG;
        ctx.EmplaceInst(new Unary(newVal_, op_, oldVal));
        ctx.EmplaceInst(new Store(addr, newVal_));

        node.setIrAddr(null);
        node.setIrValue(newVal_);
        break;
      /**
       * If we call the operation with type 'bool' as logic expression, then logicExpr can be categorized
       * as two main classes, one is value evaluation and the other one is control flow evaluation.
       *
       * Value evaluation example : bool a = b & !c.
       * It's simple and straightforward to understand, and nearly identical to other binary arithmetic
       * expressions.
       * NOTE : Insight is that '!' itself solely doesn't have any control flow functionality.
       *
       * Control flow example : bool a = b && c.
       * It splits current bb quad list into separate basicBlocks.
       * No control flow starts without && and || logic evaluations. These initialize the mode of short-circuit
       * evaluation. The semantic of logic expression is encoded into the setting of ifTrue and ifFalse jump target.
       *
       * Thus we push down jump target information, and push the task right down beyond the scope of logical evaluation,
       * to the simple bool valued expressions.
       *
       * NOTE : One point needs special attention, the way we interpret it is different.
       * NOTE : In simple value evaluation, we always calculate ! using xor, but in control flow evaluation, ! isn't implemented
       * NOTE : explicitly, but simply alter the ifTrue and ifFalse jump target.
       *
       * Branch quad generation is handled in control flow node, because otherwise it will be ugly.
       * */
      case "!":
//        if (node.ifTrue == null) {
          // value evaluation mode.
	      assert (node.ifTrue == null) == (node.ifFalse == null);
//	      boolean logicEval = node.ifTrue != null;
	      
	      // similar to what we did in logicBinExpr.
//	      if (!logicEval) {
	      // NOTE : incorporate ! into logic expression is difficult, because we don't know whether it's && or ||.
	      // NOTE : that's OK, maybe we could do it twice, but that will be tedious. Do it later on.
	      node.objInstance.Accept(this);
	      IrValue boo = GetArithResult(node.objInstance);
	      Reg negVal = ctx.GetCurFunc().GetTmpReg();
	      ctx.EmplaceInst(new Unary(negVal, NEG, boo));
	      node.setIrValue(negVal);
//	      }
//	      else {
//
		      // leaf logic node, directly evaluate its expr
//	        if (!(node.objInstance instanceof LogicBinaryExp)) {
//		        node.objInstance.Accept(this);
//		        IrValue val = GetArithResult(node);
//		         note, jump targets are opposite, because of '!'.
//		        ctx.EmplaceInst(new Branch(val, node.ifFalse, node.ifTrue));
//
//	        }
	        // intermediate logic node, push down jump targets.
//	        else {
//		        node.objInstance.ifTrue = node.ifFalse;
//		        node.objInstance.ifFalse = node.ifTrue;
//
//	        }

//	      }
       
//        }
//        else {
//           control flow evaluation mode.
//          node.objInstance.ifTrue = node.ifFalse;
//          node.objInstance.ifFalse = node.ifTrue;
//          node.objInstance.Accept(this);
//           generate branch if inner exp isn't logicEval anymore.
//          if (!isLogicEval(node.objInstance)) {
//             condition must not be IntImmediate, otherwise it could be Jump directly.
//            IrValue cond = GetArithResult(node.objInstance);
//            Quad br = NewBranchJump(cond, node.objInstance.ifTrue, node.objInstance.ifFalse);
//            ctx.EmplaceInst(br);
//             TODO : deal with curBB setting.
//          }
//        }
        break;
      default:
        throw new RuntimeException();
    }
    return null;
  }

  private static Map<String, Op> binaryOpMap = new HashMap<>();
  static {
    binaryOpMap.put("+",  ADD);
    binaryOpMap.put("-",  SUB);
    binaryOpMap.put("*",  MUL);
    binaryOpMap.put("/",  DIV);
    binaryOpMap.put("%",  MOD);
    binaryOpMap.put("<<", SHL);
    binaryOpMap.put(">>", SHR);
    binaryOpMap.put("&",  AND);
    binaryOpMap.put("^",  XOR);
    binaryOpMap.put("|",  OR);
    binaryOpMap.put("&&", LAND);
	  binaryOpMap.put("||", LOR);
    binaryOpMap.put(">", GT);
	  binaryOpMap.put(">=", GE);
	  binaryOpMap.put("<", LT);
	  binaryOpMap.put("<=", LE);
	  binaryOpMap.put("==", EQ);
	  binaryOpMap.put("!=", NE);
  }

  private void PushDownTargetBB(LogicBinaryExp node, BasicBlock rhsBB) {
	  Binary.Op op = binaryOpMap.get(node.op);
	  switch (op) {
		  case LAND:
			  node.lhs.ifFalse = node.ifFalse;
			  node.lhs.ifTrue = rhsBB;
			  node.rhs.ifTrue = node.ifTrue;
			  node.rhs.ifFalse = node.ifFalse;
			  break;
		  case LOR:
			  node.lhs.ifTrue = node.ifTrue;
			  node.lhs.ifFalse = rhsBB;
			  node.rhs.ifTrue = node.ifTrue;
			  node.rhs.ifFalse = node.ifFalse;
			  break;
		  default:
	  }
  }
	
	/**
	 * associate jumping target and current node's value,
	 * collect info for phi node.
	 * */
  private void ChildEvaluation(Exp node, Binary.Op op, boolean lhs) {
	  // leaf logic node, directly evaluate its expr.
	  if (!(node instanceof LogicBinaryExp)) {
		  node.Accept(this);
		  IrValue val = GetArithResult(node);
		  ctx.EmplaceInst(NewBranchJump(val, node.ifTrue, node.ifFalse));
		  BasicBlock curBB = ctx.GetCurBB();
		  
		  // lhs jump has chance to short-circuit
		  // two jump targets can't be the same
		  if (lhs) {
		    if (op == LAND) {
			    node.ifTrue.EnterWithVal(curBB, GetArithResult(node));
			    node.ifFalse.EnterWithVal(curBB, new IntLiteral(0));
		    }
		    else {
			    node.ifFalse.EnterWithVal(curBB, GetArithResult(node));
			    node.ifTrue.EnterWithVal(curBB, new IntLiteral(1));
		    }
		  }
		  // rhs jump is deterministic. If two targets are different, they can be distinguished by immeidate.
		  // if two are the same, they cannot be distinguished unless using a register value representation.
		  else {
//			  if (op == LAND) {
			  // NOTE : rhs is deterministic, no need to distinguish operator.
			  if (node.ifTrue != node.ifFalse) {
				  node.ifTrue.EnterWithVal(curBB, new IntLiteral(1));
				  node.ifFalse.EnterWithVal(curBB, new IntLiteral(0));
			  }
			  else {
				  node.ifTrue.EnterWithVal(curBB, GetArithResult(node));
			  }
//			  }
//			  else {
//		  		 op == LOR
//				  if (node.ifTrue != node.ifFalse) {
//					  node.ifTrue.EnterWithVal(curBB, new IntLiteral(1));
//					  node.ifFalse.EnterWithVal(curBB, new IntLiteral(0));
//				  }
//				  else {
//					  node.ifTrue.EnterWithVal(curBB, GetArithResult(node));
//				  }
//			  }
		  }
	  }
	  // next short node, set and evaluate.
	  else {
		  node.Accept(this);
	  }
  }
  
  /**
   * The whole short-evaluation can be regarded as a list of logic-valued expressions.
   * And a short-evaluated ifTrue ifFalse jump targets are imposed as a structure over
   * this list of boolean expressions.
   * Hence, when the downstream expressions are no longer logic expression, we do branch
   * since it's a leaf expr in this structure.
   *
   * NOTE logicBinaryExp jumps.
   *
   * Only the starter of logicBinaryExp needs to do phi node.
   * The intermediate logicBinaryExp performs like a distributor, and the starter functions like a collector.
   * */
  @Override
  public Void visit(LogicBinaryExp node) {
    assert (node.ifTrue == null) == (node.ifFalse == null);
    
	  // initial control flow evaluation mode.
	  // FIXME : split basic block brutally, even though it may never be used.
	  boolean startLogic = node.ifFalse == null;
	  
	  if (startLogic) {
	  	// create merge collector block.
		  BasicBlock merge = ctx.GetCurFunc().GetNewBB("merge");
		  node.ifTrue = merge;
		  node.ifFalse = merge;
		  
		  // distribute jump target.
		  BasicBlock rhsBB = ctx.GetCurFunc().NewBBAfter(ctx.GetCurBB(), node.op + "rhs");
		  PushDownTargetBB(node, rhsBB);
		  // child evaluation and record jump target&value pair.
		  Binary.Op op = binaryOpMap.get(node.op);
		  ChildEvaluation(node.lhs, op, true);
		  // for rhs
		  ctx.SetCurBB(rhsBB);
		  ChildEvaluation(node.rhs, op, false);
		  // collect information
		  // TODO : actually, this phi node can be generated when calling.
		  ctx.SetCurBB(merge);
		  Reg phiReg = ctx.GetCurFunc().GetTmpReg();
		  ctx.EmplaceInst(new Phi(phiReg, merge.phiOptions));
		  node.setIrValue(phiReg);
	  }
	  else {
		  // distribute jump target.
		  BasicBlock rhsBB = ctx.GetCurFunc().NewBBAfter(ctx.GetCurBB(), node.op + "rhs");
		  PushDownTargetBB(node, rhsBB);
		  
		  // downstream evaluation, and record jump target&value pair.
		  Binary.Op op = binaryOpMap.get(node.op);
		  ChildEvaluation(node.lhs, op, true);
		
		  ctx.SetCurBB(rhsBB);
		  ChildEvaluation(node.rhs, op, false);
	  }
	  // NOTE : we don't guarantee where are we then.
    return null;
  }


  @Override
  public Void visit(ArithBinaryExp node) {
    node.lhs.Accept(this);
    node.rhs.Accept(this);

    Reg ans = ctx.GetCurFunc().GetTmpReg();
    IrValue lVal = GetArithResult(node.lhs);
    IrValue rVal = GetArithResult(node.rhs);
    Binary.Op op = binaryOpMap.get(node.op);

    ctx.EmplaceInst(new Binary(ans, op, lVal, rVal));
    node.setIrValue(ans);
    return null;
  }

  // NOTE : don't recursion in, just query address at the top level.
  @Override
  public Void visit(FieldAccessExp node) {
    node.objInstance.Accept(this);
    Reg instAddr = node.objInstance.getIrAddr();
    Reg elemAddr = ctx.GetCurFunc().GetTmpReg();

    // get offset
    VarTypeRef instType = node.objInstance.varTypeRef;
    String member = node.memberName;
    IrValue offset = MakeInt(instType.GetFieldOffset(member));

    ctx.EmplaceInst(new Binary(elemAddr, ADD, instAddr, offset));

    node.setIrAddr(elemAddr);
    return null;
  }

  // NOTE : bread bro has a different arr access implementation from me. But I don't want to listen to him.
  @Override
  public Void visit(ArrayAccessExp node) {
    node.arrInstance.Accept(this);
    node.accessor.Accept(this);

    IrValue baseAddr = GetArithResult(node.arrInstance);

    // NOTE : These are simple tricks, later we do it.
    // NOTE : if no offset, no need to get element.
//    if (index instanceof IntLiteral && ((IntLiteral) index).val == 0) {
//      node.setIrAddr(baseAddr);
//      return null;
//    }

    Reg elemAddr = ctx.GetCurFunc().GetTmpReg();
    VarTypeRef arrElemType = node.arrInstance.varTypeRef.innerType;
    IrValue elemSize = MakeInt(arrElemType.GetTypeSpace());

    // only need to ADD, skip MUL
//    if (index instanceof IntLiteral && ((IntLiteral) index).val == 1) {
//      ctx.EmplaceInst(new GetElemPtr(elemAddr, baseAddr, elemSize));
//      node.setIrAddr(elemAddr);
//      return null;
//    }

    // now we need a MUL quad for offset calculation
	  // shift index, because the first one is reserved to be array size.
    IrValue lenSpare = MakeInt(4);
    IrValue acsIndex = GetArithResult(node.accessor);
    Reg offsetNoLen = ctx.GetCurFunc().GetTmpReg();
    Reg offsetWithLen = ctx.GetCurFunc().GetTmpReg();

    // calculate array member access and reserve for length shift, add them up.
    ctx.EmplaceInst(new Binary(offsetNoLen, MUL, acsIndex, elemSize));
    ctx.EmplaceInst(new Binary(offsetWithLen, ADD, offsetNoLen, lenSpare));
    ctx.EmplaceInst(new Binary(elemAddr, ADD, baseAddr, offsetWithLen));
    node.setIrAddr(elemAddr);

    return null;
  }

  // FIXME : Is big bread bro's implementation good ?
//	@Override
//	public Void visit(ArrayAccessExp node) {
//		return null;
//	}
	
	@Override
  public Void visit(AssignExp node) {
    node.lhs.Accept(this);
    node.rhs.Accept(this);

    Reg dstPtr = node.lhs.getIrAddr();
    IrValue srcVal = GetArithResult(node.rhs);

    ctx.EmplaceInst(new Store(dstPtr, srcVal));

    node.setIrAddr(dstPtr);
    node.setIrValue(srcVal);
    return null;
  }

  /**
   * Args must be aligned with functDec.
   * */
  @Override
  public Void visit(FunctCallExp node) {
    List<IrValue> args = new LinkedList<>();
    for (Exp arg : node.arguments.args) {
      arg.Accept(this);
      args.add(GetArithResult(arg));
    }

    // note that this is the pointer to the return address. because we can also do field or array access to it.
    Reg retAddr = (node.varTypeRef.isVoid()) ?
        MakeReg("null") : ctx.GetCurFunc().GetTmpReg();
	
    // handle function name format -- built-in ? method ?
    String mName = (node.isMethodCall()) ?
				    AddPrefix(((MethodCallExp) node).calledClass, node.functName) :
				    node.functName;
	  String btinPref = "~";
    String btmName = (node.functDec.builtIn) ? (btinPref + mName) : mName;

    // handle method 'this' pointer problem.
    if (!node.isMethodCall()) {
		    ctx.EmplaceInst(new Call(btmName, retAddr, args));
    } else {
      /**
       * If we are in a class method already, and we are invoking another class method.
       * note that by default 'this' register is $0.
       * */
      ctx.EmplaceInst(new Call(btmName, retAddr, MakeLocReg("this"), args));
    }
    node.setIrValue(retAddr);
    return null;
  }

  /**
   * error example: this is correct
   * load %0 *"hello_world"
   * store $greeting %0
   * load %1 $greeting # this is key ! which means %1 is value instead of addr.
   * call -string#length %2 %1
   * */
  @Override
  public Void visit(MethodCallExp node) {
    node.objInstance.Accept(this);
    // FIXME : here should be instValue, while instAddr is the address where the instance pointer is stored.
	  // FIXME : instAddr must be a Reg, because it holds an address.
	  
//    Reg instAddr = node.objInstance.getIrAddr();
	  Reg instAddr = (Reg) GetArithResult(node.objInstance);

    List<IrValue> args = new LinkedList<>();
    for (Exp arg : node.arguments.args) {
      arg.Accept(this);
      args.add(GetArithResult(arg));
    }
    // note that this is the pointer to the return address. because we can also do field or array access to it.
    Reg retAddr = (node.varTypeRef.isVoid()) ?
        MakeReg("null") : ctx.GetCurFunc().GetTmpReg();
    
    // add prefix ~ as marker for built-in function, this helps interpreter to parse.
    String mName = AddPrefix(node.calledClass, node.functName);
    String btmName = (node.functDec.builtIn) ? "~" + mName : mName;
    
    ctx.EmplaceInst(new Call(btmName, retAddr, instAddr, args));
    node.setIrValue(retAddr);
    return null;
  }


  @Override
  public Void visit(ReturnStm node) {
    IrValue retVal;
    if (node.retVal != null) {
      node.retVal.Accept(this);
      retVal = GetArithResult(node.retVal);
    } else {
      retVal = MakeReg("null");
    }
    ctx.EmplaceInst(new Ret(retVal));
    // TODO : basicBlock completion shouldn't be done in this way.
    ctx.GetCurBB().Complete();
    return null;
  }
	
	@Override
	public Void visit(ThisExp node) {
  	node.setIrAddr(ctx.TraceThisAddr(node.thisMethod));
		return null;
	}
}
