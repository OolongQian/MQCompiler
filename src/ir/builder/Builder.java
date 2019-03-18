package ir.builder;

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
import semantic.AstTraverseVisitor;
import java.util.*;

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
    visit(prog);
  }


  private Reg MakeReg(String name, int mode) {
    // mode 1: global, 2: local, 3: member, 4: temp
    switch (mode) {
      case 1:
        return new GlobalReg('@' + name);
      case 2:
        return new LocalReg('$' + name);
      case 3:
        break;
    }
    return null;
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
    return new Branch((Reg) cond, T, F);
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
    Reg objAddr = ctx.GetTmpLocalReg();
    IntLiteral size_ = MakeInt(ctx.CalTypeSize(type));
    ctx.EmplaceInst(new Malloc(objAddr, size_));
    // invoke constructor after memory space mallocating.
    // since it is a user-defined object, it has a classDec, and maybe a constructor.
    ClassDec userClass = type.baseType;
    String ctor = AddPrefix(userClass, type.typeName);
    if (ctx.FuncLookup(ctor) != null) {
      ctx.EmplaceInst(new Call(ctor, MakeReg("null", 1), objAddr));
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
	  	return MakeReg("null", 1);
	  
	  // allocate a temp register to hold the allocated memory address.
	  Reg arrAddr = ctx.GetTmpLocalReg();
	  // malloc some memory with suitable dimension, and let arrAddr takes on its address.
	  IrValue dim = dims.poll();
	  // add 1 to dim to hold array size.
	  Reg exDim = ctx.GetTmpLocalReg();
	  ctx.EmplaceInst(new Binary(exDim, ADD, dim, MakeInt(1)));
	  // multiply 8 to get the actual offset.
	  Reg dimByte = ctx.GetTmpLocalReg();
	  ctx.EmplaceInst(new Binary(dimByte, MUL, exDim, MakeInt(8)));
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
	  Reg arrEndMark = ctx.GetTmpLocalReg();
	  Reg endByte = ctx.GetTmpLocalReg();
	  ctx.EmplaceInst(new Binary(endByte, MUL, dim, MakeInt(8)));
	  ctx.EmplaceInst(new Binary(arrEndMark, ADD, arrAddr, endByte));
	  
	  // before enter the loop, initialize a loop iterator.
	  Reg iter = MakeReg("nIter", 2);
	  FunctCtx func = ctx.GetCurFunc();
	  ctx.InsertQuadFront(func.GetHeadBB(), new Alloca(iter, 8));
	  // store the address of the first element to it.
	  Reg initAddr = ctx.GetTmpLocalReg();
	  IrValue initByte = MakeInt(8);
	  ctx.EmplaceInst(new Binary(initAddr, ADD, arrAddr, initByte));
	  ctx.EmplaceInst(new Store(iter, initAddr));
	
	  BasicBlock cond = func.NewBBAfter(ctx.GetCurBB(), "new cond");
	  BasicBlock then = func.NewBBAfter(cond, "new then");
	  BasicBlock after = func.NewBBAfter(then, "new after");
	  // when we haven't exceed the array to be constructed, continue to stay in then BB.
	  ctx.SetCurBB(cond);
	  Reg curAddr = ctx.GetTmpLocalReg();
	  ctx.EmplaceInst(new Load(curAddr, iter));
	  Reg cmp = ctx.GetTmpLocalReg();
	  ctx.EmplaceInst(new Binary(cmp, GT, curAddr, arrEndMark));
	  // if exceed, break loop, else continue looping.
	  ctx.EmplaceInst(new Branch(cmp, after, then));
	  
	  // now we've entered the loop, we get sub-addr and store it in the address specified by curAddr.
	  ctx.SetCurBB(then);
	  Reg subArr = MakeNewArray(dims);
	  // FIXME : not sure whether recursion changes curBB.
	  ctx.EmplaceInst(new Store(curAddr, subArr));
	  // increment curAddr and store it in iter.
	  Reg increAddr = ctx.GetTmpLocalReg();
	  ctx.EmplaceInst(new Binary(increAddr, ADD, curAddr, MakeInt(8)));
	  ctx.EmplaceInst(new Store(iter, increAddr));
	  // redirect to condition check.
	  ctx.EmplaceInst(new Jump(cond));
	  
	  return arrAddr;
  }

  
  private IrValue GetArithResult(Exp node) {
    if (node.getIrValue() == null) {
      Reg addr = node.getIrAddr();
      assert addr != null;

      // load the value into a tmpVal.
      Reg tmpVal = ctx.GetTmpLocalReg();
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
   */
  @Override
  public Void visit(StringLiteralExp node) {
    // get the string address from which we could find the string head pointer.
    Reg strAddr = ctx.TraceGlobalString(node.value);
    node.setIrAddr(strAddr);
    return null;
  }

  @Override
  public Void visit(NullExp node) {
    node.setIrValue(MakeReg("null", 1));
    return null;
  }

  /**
   * Alloca a specially marked global register, holding
   * pointer or value for suitable type.
   * If has initial exp, do it in a separated function,
   * because only function can have quad.
   */
  private void AddGlobalVar(VarDec node) {
    GlobalReg addr = (GlobalReg) MakeReg(node.varName, 1);
    addr.EmplaceInit(new Alloca(addr, MakeInt(4)));

    if (node.inital != null) {
      // add a functctx to hold the initialization quad
      String initName = "_init_" + node.varName;
      FunctCtx func = new FunctCtx(initName, null);
      // setBB directly because now we are outside of functions.
      ctx.SetCurBB(func.GetNewBB(null));
      node.inital.Accept(this);
      // store initial value into reg
      IrValue initAddr = GetArithResult(node.inital);
      ctx.EmplaceInst(new Store(addr, initAddr));
      ctx.EmplaceInst(new Ret(MakeReg("null", 1)));
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
   */
  @Override
  public Void visit(FunctDec node) {
    // create FunctCtx, which needs args string.
    List<String> args = new LinkedList<>();
    if (node instanceof MethodDec)
      args.add("this");
    node.arguments.varDecs.forEach(x -> args.add(x.varName));

    // append and set cur function
    FunctCtx func = new FunctCtx(node.functName, args);
    ctx.AddFunct(func);
    ctx.SetCurFunc(func.name);
    ctx.SetCurBB(func.GetNewBB(null));

    // start generate function code.
    // Alloca a memory space for 'this'.
    if (node instanceof MethodDec) {
      Reg reg = MakeReg("this", 2);
      ctx.EmplaceInst(new Alloca(reg, MakeInt(4)));
      ctx.EmplaceInst(new Store(reg, MakeReg("0", 2)));
    }

    // Alloca tmp args to actual registers. First alloca, then store.
    for (int i = 0; i < func.args.size(); ++i) {
      int thisOffset = (node instanceof MethodDec) ? 1 : 0;
      Reg tmpArg = MakeReg(Integer.toString(i + thisOffset), 2);
      Reg reg = MakeReg(func.args.get(i), 2);
      ctx.InsertQuadFront(func.GetHeadBB(), new Alloca(reg, MakeInt(4)));
      ctx.EmplaceInst(new Store(reg, tmpArg));
    }

    // visit the rest of the statement.
    // NOTE : avoid generate dead code.
    for (Stm stm : node.functBody) {
      stm.Accept(this);
      if (ctx.GetCurBB().IsCompleted())
        break;
    }
    if (!ctx.GetCurBB().IsCompleted())
      ctx.EmplaceInst(new Ret(MakeReg("null", 1)));
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
    node.children.forEach(x -> x.Accept(this));
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
    Reg addr = MakeReg(node.varName, 2);
    ctx.InsertQuadFront(func.GetHeadBB(), new Alloca(addr, MakeInt(4)));

    if (node.inital != null && !node.inital.varTypeRef.isNull()) {
      node.inital.Accept(this);
      IrValue initVal = GetArithResult(node.inital);
      ctx.EmplaceInst(new Store(addr, initVal));
    } else if (node.varType.isInt() || node.varType.isBool()) {
      ;
    }
    else {
      // emplace a null mark in empty pointer slot.
      ctx.EmplaceInst(new Store(addr, MakeReg("null", 1)));
    }

    ctx.BindVarToAddr(node, addr);
    return null;
  }

  @Override
  public Void visit(VarExp node) {
    // NOTE : shouldn't load it at this stage.
    VarDec var = node.varDec;
    Reg addr = ctx.TraceRegAddr(var);
    node.setIrAddr(addr);
    return null;
  }

  /**
   * This is a really brilliant thing to do.
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

    IrValue curDim = (type.dim == null) ? MakeReg("null", 1) : GetArithResult(type.dim);
    dims.add(curDim);

    GetArrayDimension(type.innerType, dims);
  }

  /**
   * inspired from Chen Lequn.
   */
  @Override
  public Void visit(IfStm node) {
    BasicBlock ifTrue = ctx.GetCurFunc().GetNewBB("then");
    BasicBlock ifMerge = ctx.GetCurFunc().GetNewBB("merge");
		// set initial control flow jump target.
	  node.condition.ifTrue = ifTrue;
		if (node.elseBody != null) {
			node.condition.ifFalse = ctx.GetCurFunc().NewBBAfter(ifTrue, "else");
		} else {
			node.condition.ifFalse = ifMerge;
		}
    node.condition.Accept(this);

    ctx.SetCurBB(ifTrue);
    node.thenBody.Accept(this);
    // then bb needs jump if there's else next.
    if (node.elseBody != null) {
	    ctx.EmplaceInst(new Jump(ifMerge));
    }

    if (node.elseBody != null) {
      ctx.SetCurBB(node.condition.ifFalse);
      node.elseBody.Accept(this);
      ctx.EmplaceInst(new Jump(ifMerge));
    }

    ctx.SetCurBB(ifMerge);
    return null;
  }

  
  
  @Override
  public Void visit(WhileStm node) {

    BasicBlock cond = ctx.GetCurFunc().GetNewBB("cond");
    BasicBlock step = ctx.GetCurFunc().GetNewBB("step");
    BasicBlock after = ctx.GetCurFunc().GetNewBB("after");

    ctx.SetCurBB(cond);
    node.condition.ifTrue = step;
    node.condition.ifFalse = after;
    node.condition.Accept(this);

    // record loop info for break and continue
    ctx.RecordLoop(cond, after);

    ctx.SetCurBB(step);
    node.whileBody.Accept(this);
    ctx.EmplaceInst(new Jump(after));

    ctx.ExitLoop();
    ctx.SetCurBB(after);
    return null;
  }

  @Override
  public Void visit(ForStm node) {
    if (!node.forControl.isInitNull()) {
      if (node.forControl.initIsDec) {
        // visit a for dec
        node.forControl.initDec.Accept(this);
      } else {
        // visit a series of expr
        node.forControl.initExps.forEach(x -> x.Accept(this));
      }
    }

    BasicBlock check = ctx.GetCurFunc().GetNewBB("cond");
    BasicBlock step = ctx.GetCurFunc().GetNewBB("step");
    BasicBlock after = ctx.GetCurFunc().GetNewBB("after");

    ctx.RecordLoop(check, after);

    ctx.SetCurBB(check);
    node.forControl.check.ifTrue = step;
    node.forControl.check.ifFalse = after;
    node.forControl.check.Accept(this);

    ctx.SetCurBB(step);
    node.forBody.Accept(this);
    ctx.EmplaceInst(new Jump(after));

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
    Reg increVal = ctx.GetTmpLocalReg();

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
        Reg newVal = ctx.GetTmpLocalReg();
        Op op = (node.op.equals("++")) ? ADD : SUB;
        ctx.EmplaceInst(new Binary(newVal, op, oldVal, MakeInt(1)));
        ctx.EmplaceInst(new Store(addr, newVal));
        // prefix is still a lvalue.
        node.setIrValue(newVal);
        node.setIrAddr(addr);
        break;
      case "~": case "-":
        Reg newVal_ = ctx.GetTmpLocalReg();
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
        assert (node.ifTrue == null) == (node.ifFalse == null);
        if (node.ifTrue == null) {
          // value evaluation mode.
          node.objInstance.Accept(this);
          IrValue boo = GetArithResult(node.objInstance);
          Reg negVal = ctx.GetTmpLocalReg();
          ctx.EmplaceInst(new Unary(negVal, NEG, boo));
          node.setIrValue(negVal);
        }
        else {
          // control flow evaluation mode.
          node.objInstance.ifTrue = node.ifFalse;
          node.objInstance.ifFalse = node.ifTrue;
          node.objInstance.Accept(this);
          // generate branch if inner exp isn't logicEval anymore.
          if (!isLogicEval(node.objInstance)) {
            // condition must not be IntImmediate, otherwise it could be Jump directly.
            IrValue cond = GetArithResult(node.objInstance);
            Quad br = NewBranchJump(cond, node.objInstance.ifTrue, node.objInstance.ifFalse);
            ctx.EmplaceInst(br);
            // TODO : deal with curBB setting.
          }
        }
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
    binaryOpMap.put(">", GT);
	  binaryOpMap.put(">=", GE);
	  binaryOpMap.put("<", LT);
	  binaryOpMap.put("<=", LE);
	  binaryOpMap.put("==", EQ);
	  binaryOpMap.put("!=", NE);
  }

  @Override
  public Void visit(LogicBinaryExp node) {
    assert (node.ifTrue == null) == (node.ifFalse == null);

    if (node.ifFalse == null) {
      // initial control flow evaluation mode.
      BasicBlock merge = ctx.GetCurFunc().GetNewBB("merge");
      node.ifTrue = merge;
      node.ifFalse = merge;
    }
    // push down branch information.
    Binary.Op op = binaryOpMap.get(node.op);
    BasicBlock rhsBB = ctx.GetCurFunc().NewBBAfter(ctx.GetCurBB(), node.op + "rhs");
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
    // visit lhs and rhs
    // TODO : deal with curBB setting.
    node.lhs.Accept(this);
    // if lhs is not logicEval anymore, generate jump
    if (!isLogicEval(node.lhs)) {
      IrValue cond = GetArithResult(node.lhs);
      Quad br = NewBranchJump(cond, node.lhs.ifTrue, node.lhs.ifFalse);
      ctx.EmplaceInst(br);
    }
    ctx.SetCurBB(rhsBB);
    node.rhs.Accept(this);
	  if (!isLogicEval(node.lhs)) {
		  IrValue cond = GetArithResult(node.rhs);
		  Quad br = NewBranchJump(cond, node.rhs.ifTrue, node.rhs.ifFalse);
		  ctx.EmplaceInst(br);
	  }
    return null;
  }


  @Override
  public Void visit(ArithBinaryExp node) {
    node.lhs.Accept(this);
    node.rhs.Accept(this);

    Reg ans = ctx.GetTmpLocalReg();
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
    Reg elemAddr = ctx.GetTmpLocalReg();

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

    Reg baseAddr = node.arrInstance.getIrAddr();
//    IrValue index = GetArithResult(node.accessor);

    // NOTE : These are simple tricks, later we do it.
    // NOTE : if no offset, no need to get element.
//    if (index instanceof IntLiteral && ((IntLiteral) index).val == 0) {
//      node.setIrAddr(baseAddr);
//      return null;
//    }

    Reg elemAddr = ctx.GetTmpLocalReg();
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
    Reg offset = ctx.GetTmpLocalReg();
    ctx.EmplaceInst(new Binary(offset, MUL, offset, elemSize));
    ctx.EmplaceInst(new Binary(elemAddr, ADD, baseAddr, offset));
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

  @Override
  public Void visit(FunctCallExp node) {
    List<IrValue> args = new LinkedList<>();
    for (Exp arg : node.arguments.args) {
      arg.Accept(this);
      args.add(GetArithResult(arg));
    }

    // note that this is the pointer to the return address. because we can also do field or array access to it.
    Reg retAddr = (node.varTypeRef.isVoid()) ?
        MakeReg("null", 1) : MakeReg("ret", 2);

    if (!node.isMethodCall()) {
      ctx.EmplaceInst(new Call(node.functName, retAddr, args));
    } else {
      /**
       * If we are in a class method already, and we are invoking another class method.
       * note that by default 'this' register is $0.
       * */
      MethodCallExp mNode = (MethodCallExp) node;
      String mName = AddPrefix(mNode.calledClass, mNode.functName);
      ctx.EmplaceInst(new Call(mName, retAddr, MakeReg("0", 2), args));
    }
    node.setIrAddr(retAddr);
    return null;
  }

  @Override
  public Void visit(MethodCallExp node) {
    node.objInstance.Accept(this);
    Reg instAddr = node.objInstance.getIrAddr();

    List<IrValue> args = new LinkedList<>();
    for (Exp arg : node.arguments.args) {
      arg.Accept(this);
      args.add(GetArithResult(arg));
    }
    // note that this is the pointer to the return address. because we can also do field or array access to it.
    Reg retAddr = (node.varTypeRef.isVoid()) ?
        MakeReg("null", 1) : ctx.GetTmpLocalReg();

    String mName = AddPrefix(node.calledClass, node.functName);
    ctx.EmplaceInst(new Call(mName, retAddr, instAddr, args));
    return null;
  }


  @Override
  public Void visit(ReturnStm node) {
    IrValue retVal;
    if (node.retVal != null) {
      node.retVal.Accept(this);
      retVal = GetArithResult(node.retVal);
    } else {
      retVal = MakeReg("null", 1);
    }
    ctx.EmplaceInst(new Ret(retVal));
    // TODO : basicBlock completion shouldn't be done in this way.
    ctx.GetCurBB().Complete();
    return null;
  }
  
  

}
