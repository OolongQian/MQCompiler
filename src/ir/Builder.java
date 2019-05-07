package ir;

import ast.node.Prog;
import ast.node.dec.*;
import ast.node.exp.*;
import ast.node.stm.*;
import ast.type.Type;
import ast.usage.AstBaseVisitor;
import ir.quad.*;
import ir.structure.*;

import java.util.*;

import static config.Config.INT_SIZE;
import static ir.Utility.MakeGreg;
import static ir.Utility.MakeInt;
import static ir.quad.Binary.Op.*;
import static ir.quad.Unary.Op.BITNOT;
import static ir.quad.Unary.Op.NEG;
import static semantic.Utility.AddPrefix;

public class Builder extends AstBaseVisitor<Void> {
	
	private BuilderContext ctx;
	
	public Builder(BuilderContext ctx) {
		this.ctx = ctx;
	}
	
	public void Build(Prog ast) {
		MakeClassTable(ast);
		BuildIr(ast);
		// clarify control flow
		for (IrFunct funct : ctx.ir.functs.values()) {
			funct.bbs.CleanAfterRet();
			funct.bbs.AddFallThroughJump();
			funct.EnsureFunctRet();
		}
	}
	
	private void MakeClassTable(Prog ast) {
		for (Dec dec : ast.decs) {
			if (dec instanceof ClassDec)
				ctx.irClassTable.put(((ClassDec) dec).name, (ClassDec) dec);
		}
	}
	
	private void BuildIr(Prog ast) {
		visit(ast);
	}
	
	/**
	 * Create a initialization function for global variable initialization.
	 * */
	@Override
	public Void visit(Prog node) {
		// init global variable in _init_ function.
		IrFunct init = ctx.FuncGen("_init_");
		ctx.SetCurFunc(init);
		for (Dec child : node.decs) {
			if (child instanceof VarDecList) {
				for (VarDec dec : ((VarDecList) child).varDecs) {
					assert dec.isGlobal();
					dec.Accept(this);
				}
			}
		}
		
		for (Dec child : node.decs) {
			if (!(child instanceof VarDecList))
				child.Accept(this);
		}
		return null;
	}
	
	/**
	 * **************************************** Decs Defs ******************************************
	 * Global variables have no naming collision.
	 *
	 * Note that declaration is wrapped by VarDecList
	 * */
	@Override
	public Void visit(VarDecList node) {
		node.varDecs.forEach(x -> x.Accept(this));
		return null;
	}
	
	/**
	 * If node isn't initialized and is not primitive type, assign it to be null.
	 * */
	@Override
	public Void visit(VarDec node) {
		if (node.isField()) return null;
		
		if (node.isGlobal()) {
			Reg var = MakeGreg(node.name);
			ctx.AddGlobalVar(node, var);
			
			if (node.inital != null) {
				noJumpLogic = node.inital.weile;
				node.inital.Accept(this);
				noJumpLogic = false;
				IrValue initVal = GetArithResult(node.inital);
				ctx.EmplaceInst(new Store(var, initVal));
			}
		}
		// do nothing to field.
		else {
			node.name = ctx.cFun.RenameLocal(node.name);
			Reg var = new Reg(node.name);
			ctx.EmplaceInst(new Alloca(var));
//			ctx.cFun.AddLocalVar(var);
			if (node.inital != null && !node.inital.type.isNull()) {
				noJumpLogic = !node.inital.weile;
				node.inital.Accept(this);
				noJumpLogic = false;
				IrValue initVal = GetArithResult(node.inital);
				ctx.EmplaceInst(new Store(var, initVal));
			}
			ctx.varTracer.put(node, var);
		}
		return null;
	}
	
	@Override
	public Void visit(FunctDec node) {
		// a new function is about to be generated, set the last BB of the last function to be completed.
//		if (!ctx.cFun.curBB.complete)
//			ctx.CompleteCurBB();
		
		String renaming = node.funcTableKey;
		IrFunct func = ctx.FuncGen(renaming);
		ctx.SetCurFunc(func);
		if (node.retType.isInt())
			ctx.cFun.retInt = true;
		
		// do renaming directly on Ast.
		node.args.forEach(x -> x.name = ctx.cFun.RenameLocal(x.name));
		if (node.isMethod()) node.args.add(0, new VarDec("this"));
		node.args.forEach(x -> func.formalArgs.add(x.name));
		
		for (int i = 0; i < func.formalArgs.size(); ++i) {
			Reg var = new Reg(func.formalArgs.get(i));
			Reg reg = ctx.cFun.GetTmpReg();
			func.regArgs.add(reg);
			
			ctx.EmplaceInst(new Alloca(var));
			ctx.EmplaceInst(new Store(var, reg));
			if (func.formalArgs.get(i).equals("this"))
				func.this_ = var;
			// FIXME : why we need to do this.
			ctx.varTracer.put(node.args.get(i), var);
		}
		for (Stm stm : node.body) {
			stm.Accept(this);
		}
		return null;
	}
	
	/**
	 * Avoid bug in ast reconstruction
	 * */
	@Override
	public Void visit(LocalVarDecStm node) {
		node.varDecs.forEach(x -> x.Accept(this));
		return null;
	}
	
	/**
	 * Be careful about class member access.
	 * Do nothing about field.
	 * */
	@Override
	public Void visit(ClassDec node) {
		node.methods.forEach(x -> x.Accept(this));
		return null;
	}
	
	
	/**
	 * ********************************** Exp Uses *******************************
	 * We would encounter defined local variable, or class field
	 * in class methods, distinguish them.
	 * */
	// FIXME : Here we should consider creating getElemPtr quad.
	@Override
	public Void visit(VarExp node) {
		VarDec varDec = node.resolve;
		if (varDec.isField()) {
			Reg this_ = ctx.cFun.this_;
			Mumble("start a field eval");
			// load this.
			Reg headPtr = ctx.cFun.GetTmpReg();
			ctx.EmplaceInst((new Load(headPtr, this_)));
			// find offset of field in class layout
			ClassDec layout = varDec.GetParentClass();
			// field name hasn't been renamed.
			// it shouldn't be renamed.
			String fieldName = node.name;
			Constant offset = new Constant(layout.GetFieldOffset(fieldName));
			Reg fieldPtr = ctx.cFun.GetTmpReg();
			ctx.EmplaceInst(new Binary(fieldPtr, ADD, headPtr, offset));
			node.setIrAddr(fieldPtr);
		}
		else {
			Reg var = ctx.varTracer.get(varDec);
			node.setIrAddr(var);
		}
		return null;
	}
	
	
	@Override
	public Void visit(SuffixExp node) {
		node.obj.Accept(this);
		// NOTE : it is addr because node is a lvalue.
		Reg var = node.obj.getIrAddr();
		
		IrValue oldVal = GetArithResult(node.obj);
		Reg increVal = ctx.cFun.GetTmpReg();
		
		Binary.Op op = (node.op.equals("++")) ? ADD : SUB;
		ctx.EmplaceInst(new Binary(increVal, op, oldVal, MakeInt(1)));
		ctx.EmplaceInst(new Store(var, increVal));
		
		// set IrAddr to null just for clarity, showing that it cannot be queried, otherwise leads to assertion failure.
		node.setIrAddr(null);
		node.setIrValue(oldVal);
		return null;
	}
	
	
	@Override
	public Void visit(PrefixExp node) {
		node.obj.Accept(this);
		
		IrValue oldVal = GetArithResult(node.obj);
		switch (node.op) {
			case "++": case "--":
				Reg var = node.obj.getIrAddr();
				// similar to Suffix
				Reg newVal = ctx.cFun.GetTmpReg();
				Binary.Op op = (node.op.equals("++")) ? ADD : SUB;
				ctx.EmplaceInst(new Binary(newVal, op, oldVal, MakeInt(1)));
				ctx.EmplaceInst(new Store(var, newVal));
				// prefix is still a lvalue.
				node.setIrValue(newVal);
				node.setIrAddr(var);
				break;
			case "~": case "-":
				Reg newVal_ = ctx.cFun.GetTmpReg();
				Unary.Op op_ = (node.op.equals("~")) ? BITNOT : NEG;
				ctx.EmplaceInst(new Unary(newVal_, op_, oldVal));
				
				node.setIrAddr(null);
				node.setIrValue(newVal_);
				break;
			case "!":
				assert (node.ifTrue == null) == (node.ifFalse == null);
				IrValue bool = GetArithResult(node.obj);
				Reg oppoVal = ctx.cFun.GetTmpReg();
				// this is a little bit tricky.
				ctx.EmplaceInst(new Binary(oppoVal, XOR, bool, new Constant(1)));
				node.setIrAddr(null);
				node.setIrValue(oppoVal);
				break;
			default:
				throw new RuntimeException();
		}
		return null;
	}
	
	@Override
	public Void visit(ArithBinaryExp node) {
		node.lhs.Accept(this);
		node.rhs.Accept(this);
		
		Reg ans = ctx.cFun.GetTmpReg();
		IrValue lVal = GetArithResult(node.lhs);
		IrValue rVal = GetArithResult(node.rhs);
		
		
		if (node.lhs.type.isString()) {
			List<IrValue> args = new LinkedList<>();
			args.add(lVal); args.add(rVal);
			Call stringBin = null;
			switch (node.op) {
				case "<":
					stringBin = new Call("~-string#lt", ans, args);
					break;
				case "<=":
					stringBin = new Call("~-string#le", ans, args);
					break;
				case ">":
					stringBin = new Call("~-string#gt", ans, args);
					break;
				case ">=":
					stringBin = new Call("~-string#ge", ans, args);
					break;
				case "==":
					stringBin = new Call("~-string#eq", ans, args);
					break;
				case "!=":
					stringBin = new Call("~-string#neq", ans, args);
					break;
				case "+":
					stringBin = new Call("~-string#add", ans, args);
					break;
				default:
					assert false;
			}
			ctx.EmplaceInst(stringBin);
		}
		else {
			Binary.Op op;
			op = binaryOpMap.get(node.op);
			ctx.EmplaceInst(new Binary(ans, op, lVal, rVal));
		}
		
		node.setIrValue(ans);
		return null;
	}
	
	/**
	 * This is a really brilliant thing to do.
	 * */
	@Override
	public Void visit(CreationExp node) {
		if (!node.type.isArray()) {
			Reg instPtr = MakeNewNonArray(node.type);
			// this node's value is a register containing a pointer to the obj.
			node.setIrValue(instPtr);
		}
		else {
			// array creation need type and dimension info.
			// use a queue to hold array dimension because first in first out.
			node.type.Accept(this);
			Queue<IrValue> dimInfo = new ArrayDeque<>();
			GetArrayDimension(node.type, dimInfo);
			// note : In Java, we regard array as pointers, thus no explicit class info is required.
			Reg arrPtr = MakeNewArray(dimInfo);
			node.setIrValue(arrPtr);
		}
		return null;
	}
	
	/**
	 * Sub-function used by visit(CreationExp node)
	 * */
	private void GetArrayDimension(Type type, Queue<IrValue> dims)  {
		// varTypeRef has been traversed.
		if (type.innerType == null)
			return;
		
		if (type.dim != null) {
			dims.add(GetArithResult(type.dim));
			GetArrayDimension(type.innerType, dims);
		}
	}
	
	/**
	 * makeNewNonArray can only be invoked by user-defined class.
	 * */
	private Reg MakeNewNonArray(Type type) {
		// tmpVar is going to be returned to var, and be stored.
		assert !type.isArray();
		
		Reg tmpVar = ctx.cFun.GetTmpReg();
		Constant size_ = MakeInt(CalTypeSize(type));
		ctx.EmplaceInst(new Malloc(tmpVar, size_));
		
		ClassDec userClass = ctx.irClassTable.get(type.GetBaseTypeName());
		String ctor = AddPrefix(userClass, type.typeName);
		if (ctx.functTable.containsKey(ctor)) {
			List<IrValue> args = new LinkedList<>(); args.add(tmpVar);
			ctx.EmplaceInst(new Call(ctor, MakeGreg("null"), args));
		}
		return tmpVar;
	}
	
	private Reg MakeNewArray(Queue<IrValue> dims) {
		// recursion stopper.
		if (dims.isEmpty()) {
			return MakeGreg("null");
		}
		// allocate a temp register to hold the allocated memory address.
		Reg arrTmp = ctx.cFun.GetTmpReg();
		// malloc some memory with suitable dimension, and let arrAddr takes on its address.
		IrValue dim = dims.poll();
		// add 1 to dim to hold array size.
		Reg exDim = ctx.cFun.GetTmpReg();
		ctx.EmplaceInst(new Binary(exDim, ADD, dim, MakeInt(1)));
		// multiply INT_SIZE to get the actual offset, use << for efficiency.
		Reg exDimByte = ctx.cFun.GetTmpReg();
		ctx.EmplaceInst(new Binary(exDimByte, SHL, exDim, MakeInt(3)));
//		ctx.EmplaceInst(new Binary(exDimByte, MUL, exDim, MakeInt(8)));
		// malloc mem space, let arrAddr take on the address.
		ctx.EmplaceInst(new Malloc(arrTmp, exDimByte));
		
		if (dims.isEmpty()) {
			// if there's no other dimensions to create, stop and return.
			// clear currently created array to all zero.
			List<IrValue> args = new LinkedList<>();
			args.add(arrTmp); args.add(MakeInt(0)); args.add(exDimByte);
			ctx.EmplaceInst(new Call("~memset", MakeGreg("null"), args));
			// remember to record the array length.
			ctx.EmplaceInst(new Store(arrTmp, dim));
			return arrTmp;
		}
		
		// record array dimension after recording, right at the head of malloc_ed mem.
		ctx.EmplaceInst(new Store(arrTmp, dim));
		
		// otherwise, construct a loop to create pointers for inner dimensions.
		// collect and store the addresses malloc_ed during sub-problems into currently malloc_ed address.
		
		// first, get a endMarker, when we at the endMarker, we are right outside the array.
		// which means if (curMarker == endMarker) break;
		Reg arrEndMark = ctx.cFun.GetTmpReg();
		ctx.EmplaceInst(new Binary(arrEndMark, ADD, arrTmp, exDimByte));
		
		
		// before enter the loop, initialize a loop iterator.
		Reg iter = ctx.cFun.GetReserveReg("itr");
		ctx.EmplaceInst(new Alloca(iter));
		// init the iter to be the addr of 1st element.
		Reg initAddr = ctx.cFun.GetTmpReg();
		IrValue initByte = MakeInt(INT_SIZE);
		ctx.EmplaceInst(new Binary(initAddr, ADD, arrTmp, initByte));
		ctx.EmplaceInst(new Store(iter, initAddr));
		// ctx.CompleteCurBB();
		
		assert ctx.cFun.curBB.loopLevel != null;
		BasicBlock preheader = ctx.NewBBAfter(ctx.cFun.curBB, "pre_arrCond_n", ctx.cFun.curBB.loopLevel);
		BasicBlock cond = ctx.NewBBAfter(preheader, "arrCond_n", ctx.cFun.curBB.loopLevel);
		BasicBlock then = ctx.NewBBAfter(cond, "arrThen_n", ctx.cFun.curBB.loopLevel + 1);
		BasicBlock after = ctx.NewBBAfter(then, "arrAfter_n", ctx.cFun.curBB.loopLevel);
		
		// when we haven't exceed the array to be constructed, continue to stay in then BB.
		ctx.SetCurBB(cond);
		Reg cursor = ctx.cFun.GetTmpReg();
		ctx.EmplaceInst(new Load(cursor, iter));
		Reg cmp = ctx.cFun.GetTmpReg();
		ctx.EmplaceInst(new Binary(cmp, EQ, cursor, arrEndMark));
		// if equal, break the loop
		ctx.EmplaceInst(new Branch(cmp, after, then));
		// ctx.CompleteCurBB();
		
		// now we've entered the loop, we get sub-addr and store it in the address specified by curAddr.
		ctx.SetCurBB(then);
		Reg subArrTmp = MakeNewArray(dims);
		// setCurBB to be after when this funct ends, thus we end up in safe basic block.
		ctx.EmplaceInst(new Store(cursor, subArrTmp));
		// increment curAddr and store it in iter.
		Reg increCursor = ctx.cFun.GetTmpReg();
		ctx.EmplaceInst(new Binary(increCursor, ADD, cursor, MakeInt(INT_SIZE)));
		ctx.EmplaceInst(new Store(iter, increCursor));
		// redirect to condition check.
		ctx.EmplaceInst(new Jump(cond));
		// ctx.CompleteCurBB();

		ctx.SetCurBB(after);
		
		return arrTmp;
	}
	
	/************************* subscript / field ************************************/
	@Override
	public Void visit(FieldAccessExp node) {
		node.obj.Accept(this);
		
		IrValue instAddr = GetArithResult(node.obj);
		Reg elemAddr = ctx.cFun.GetTmpReg();
		
		// get offset
		Type instType = node.obj.type;
		assert !instType.isArray();
		String member = node.memberName;
		ClassDec class_ = ctx.irClassTable.get(instType.GetBaseTypeName());
		IrValue offset = MakeInt(class_.GetFieldOffset(member));
		
		ctx.EmplaceInst(new Binary(elemAddr, ADD, instAddr, offset));
		
		node.setIrAddr(elemAddr);
		return null;
	}
	
	
	@Override
	public Void visit(ArrayAccessExp node) {
		node.arr.Accept(this);
		node.subscript.Accept(this);
		
		IrValue baseAddr = GetArithResult(node.arr);
		
		// NOTE : These are simple tricks, later we do it.
		// NOTE : if no offset, no need to get element.
		Reg elemAddr = ctx.cFun.GetTmpReg();
//		IrValue acsIndex = GetArithResult(node.subscript);
//		List<IrValue> args = new LinkedList<>();
//		args.add(baseAddr);
//		args.add(acsIndex);
//		 size per index
//		args.add(MakeInt(8));
//		 offset
//		args.add(MakeInt(8));
//		ctx.EmplaceInst(new Call("~getElementPointer", elemAddr, args));
//		node.setIrAddr(elemAddr);

		// FIXME : when we use java convention, all array type element is a pointer or built-in type.
//		VarTypeRef arrElemType = node.arrInstance.varTypeRef.innerType;
//		IrValue elemSize = MakeInt(arrElemType.GetTypeSpace());
		IrValue elemSize = MakeInt(8);
		
		// now we need a MUL quad for offset calculation
		// shift index, because the first one is reserved to be array size.
		IrValue lenSpare = MakeInt(8);
		IrValue acsIndex = GetArithResult(node.subscript);
		Reg offsetNoLen = ctx.cFun.GetTmpReg();
		Reg offsetWithLen = ctx.cFun.GetTmpReg();
		
		// calculate array member access and reserve for length shift, add them up.
		ctx.EmplaceInst(new Binary(offsetNoLen, SHL, acsIndex, MakeInt(3)));
//		ctx.EmplaceInst(new Binary(offsetNoLen, MUL, acsIndex, elemSize));
		ctx.EmplaceInst(new Binary(offsetWithLen, ADD, offsetNoLen, lenSpare));
		ctx.EmplaceInst(new Binary(elemAddr, ADD, baseAddr, offsetWithLen));
		node.setIrAddr(elemAddr);
		
		return null;
	}
	
	private boolean noJumpLogic = false;
	@Override
	public Void visit(AssignExp node) {
		node.dst.Accept(this);
		
		// refuse to do short circuit evaluation if it's on the right
		// handside of an assignment.
		
		noJumpLogic = !node.src.weile;
		node.src.Accept(this);
		noJumpLogic = false;
		
		Reg dstPtr = node.dst.getIrAddr();
		IrValue srcVal = GetArithResult(node.src);
		
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
		for (Exp arg : node.args) {
			arg.Accept(this);
			args.add(GetArithResult(arg));
		}
		
		Reg retVal = (node.type.isVoid()) ?
						MakeGreg("null") : ctx.cFun.GetTmpReg();
		
		// handle function name format -- built-in ? method ?
//		String mName = (node.isCallMethod()) ?
//						AddPrefix(((MethodCallExp) node).getClassResolve(), node.funcName) :
//						node.functName;
		String mName = node.funcTableKey;
		String btinPref = "~";
		String btmName = (node.getFuncResolve().builtIn) ? (btinPref + mName) : mName;
		
		// handle method 'this' pointer problem.
		if (!node.isCallMethod()) {
			ctx.EmplaceInst(new Call(btmName, retVal, args));
		} else {
			/**
			 * If we are in a class method already, and we are invoking another class method.
			 * note that by default 'this' register is $0.
			 * */
			Reg thisVal = ctx.cFun.GetTmpReg();
			ctx.EmplaceInst(new Load(thisVal, ctx.cFun.this_));
			args.add(0, thisVal);
			ctx.EmplaceInst(new Call(btmName, retVal, args));
		}
		node.setIrValue(retVal);
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
		node.obj.Accept(this);
		// FIXME : here should be instValue, while instAddr is the address where the instance pointer is stored.
		// FIXME : instAddr must be a Reg, because it holds an address.

//    Reg instAddr = node.objInstance.getIrAddr();
		Reg instAddr = (Reg) GetArithResult(node.obj);
		
		List<IrValue> args = new LinkedList<>();
		args.add(instAddr);
		for (Exp arg : node.args) {
			arg.Accept(this);
			args.add(GetArithResult(arg));
		}
		
		Reg retVal = (node.type.isVoid()) ?
						MakeGreg("null") : ctx.cFun.GetTmpReg();
		
		// add prefix ~ as marker for built-in function, this helps interpreter to parse.
		String mName = node.funcTableKey;
		String btmName = (node.getFuncResolve().builtIn) ? "~" + mName : mName;
		
		ctx.EmplaceInst(new Call(btmName, retVal, args));
		node.setIrValue(retVal);
		return null;
	}
	
	@Override
	public Void visit(ReturnStm node) {
		IrValue retVal;
		if (node.ret != null) {
			node.ret.Accept(this);
			retVal = GetArithResult(node.ret);
		} else {
			retVal = MakeGreg("null");
		}
		ctx.EmplaceInst(new Ret(retVal));
		ctx.EarlyStopCurBB();
		return null;
	}
	
	@Override
	public Void visit(ThisExp node) {
		node.setIrAddr(ctx.cFun.this_);
		return null;
	}
	
	
	/******************************* Control flow ********************************/
	/**
	 * local register used for short-circuit eval result storage.
	 * */
	private Stack<Reg> logicVars = new Stack<>();
	
	@Override
	public Void visit(LogicBinaryExp node) {
		if (noJumpLogic) {
			Reg ans = ctx.cFun.GetTmpReg();
			node.lhs.Accept(this);
			node.rhs.Accept(this);
			IrValue lVal = GetArithResult(node.lhs);
			IrValue rVal = GetArithResult(node.rhs);
			Binary.Op op = (node.op.equals("||")) ? OR : AND;
			ctx.EmplaceInst(new Binary(ans, op, lVal, rVal));
			node.setIrValue(ans);
			return null;
		}
		
		assert (node.ifTrue == null) == (node.ifFalse == null);
		
		boolean startLogic = node.ifFalse == null;
		
		if (startLogic) {
			// create logicVar to place logic result.
			Reg logi = ctx.cFun.GetReserveReg("logi");
			ctx.EmplaceInst(new Alloca(logi));
			logicVars.push(logi);
			
			// create merge collector block.
			assert ctx.cFun.curBB.loopLevel != null;
			BasicBlock cur = ctx.cFun.curBB;
			BasicBlock assignTrue = ctx.NewBBAfter(ctx.cFun.curBB, "asgT", ctx.cFun.curBB.loopLevel);
			BasicBlock assignFalse = ctx.NewBBAfter(assignTrue, "asgF", ctx.cFun.curBB.loopLevel);
			BasicBlock merge = ctx.NewBBAfter(assignFalse, "merge", ctx.cFun.curBB.loopLevel);
			
			// assign logi in assignTrue and assignFalse.
			ctx.SetCurBB(assignTrue);
			ctx.EmplaceInst(new Store(logi, new Constant(1)));
			ctx.EmplaceInst(new Jump(merge));
			ctx.SetCurBB(assignFalse);
			ctx.EmplaceInst(new Store(logi, new Constant(0)));
			ctx.EmplaceInst(new Jump(merge));
			ctx.SetCurBB(cur);
			
			node.ifTrue = assignTrue;
			node.ifFalse = assignFalse;
			
			// distribute jump target.
			assert ctx.cFun.curBB.loopLevel != null;
			String opStr = (node.op.equals("&&")) ? "and_" : "or_";
			BasicBlock rhsBB = ctx.NewBBAfter(ctx.cFun.curBB, opStr + "rhs", ctx.cFun.curBB.loopLevel);
			PushDownTargetBB(node, rhsBB);
			// child evaluation and record jump target&value pair.
			ChildEvaluation(node.lhs);
			// ctx.CompleteCurBB();
			
			// for rhs
			ctx.SetCurBB(rhsBB);
			ChildEvaluation(node.rhs);
			
			ctx.SetCurBB(merge);
			
			node.setIrAddr(logi);
			// recursion back to it.
			logicVars.pop();
		}
		else {
			// distribute jump target.
			assert ctx.cFun.curBB.loopLevel != null;
			String opStr = (node.op.equals("&&")) ? "and_" : "or_";
			BasicBlock rhsBB = ctx.NewBBAfter(ctx.cFun.curBB, opStr + "rhs", ctx.cFun.curBB.loopLevel);
			PushDownTargetBB(node, rhsBB);
			
			// downstream evaluation, and record jump target&value pair.
			ChildEvaluation(node.lhs);
			// ctx.CompleteCurBB();
			
			ctx.SetCurBB(rhsBB);
			ChildEvaluation(node.rhs);
			// ctx.CompleteCurBB();
			// I don't need to care about where I end up to be, because we have 'ctx.SetCurBB(rhsBB);'
		}
		return null;
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
	private void ChildEvaluation(Exp node) {
		// leaf logic node, directly evaluate its expr.
		if (!(node instanceof LogicBinaryExp)) {
			node.Accept(this);
			IrValue val = GetArithResult(node);
//			Reg logi = logicVars.peek();
//			ctx.EmplaceInst(new Store(logi, val));
			ctx.EmplaceInst(new Branch(val, node.ifTrue, node.ifFalse));
		}
		// next short node, set and evaluate.
		else {
			node.Accept(this);
		}
	}
	
	@Override
	public Void visit(IfStm node) {
		assert ctx.cFun.curBB.loopLevel != null;
		BasicBlock then = ctx.NewBBAfter(ctx.cFun.curBB, "ifThen", ctx.cFun.curBB.loopLevel);
		BasicBlock merge = ctx.NewBBAfter(then, "ifMerge", ctx.cFun.curBB.loopLevel);
		BasicBlock ifFalse = (node.elseBody != null) ? ctx.NewBBAfter(then, "ifElse", ctx.cFun.curBB.loopLevel) : merge;
//	/*
		if (node.cond instanceof LogicBinaryExp) {
			// if for short-circuit evaluation.
			node.cond.ifTrue = then;
			node.cond.ifFalse = ifFalse;
			node.cond.Accept(this);
		}
		else {
			// if for ordinary cond.
			node.cond.Accept(this);
			IrValue brCond = GetArithResult(node.cond);
			// cond links to then and ifFalse for CFG.
			ctx.EmplaceInst(new Branch(brCond, then, ifFalse));
		}
		
		// where I'm upon returning is not sure. do codegen.
		ctx.SetCurBB(then);
		node.thenBody.Accept(this);
		if (node.elseBody != null) {
			ctx.EmplaceInst(new Jump(merge));
			ctx.SetCurBB(ifFalse);
			node.elseBody.Accept(this);
		}
		// set BB to merge.
		ctx.SetCurBB(merge);
//*/
		/*
		// condition may set us to curBB or mergeBB, we resume to genIR.
		node.cond.Accept(this);
		IrValue brCond = GetArithResult(node.cond);
		// cond links to then and ifFalse for CFG.
		ctx.EmplaceInst(new Branch(brCond, then, ifFalse));
		
		// then irGen, assume no curBB recovery.
		ctx.SetCurBB(then);
		node.thenBody.Accept(this);
		
		// emplace 'jump' from then to merge if necessary.
		if (node.elseBody == null) {
			// ctx.CompleteCurBB();
		} else {
			ctx.EmplaceInst(new Jump(merge));
			// ctx.CompleteCurBB();
			
			ctx.SetCurBB(ifFalse);
			node.elseBody.Accept(this);
			// ctx.CompleteCurBB();
		}
		// set BB to merge
		ctx.SetCurBB(merge);
		*/
		return null;
	}
	
	
	
	@Override
	public Void visit(WhileStm node) {
		assert ctx.cFun.curBB.loopLevel != null;
		BasicBlock preheader = ctx.NewBBAfter(ctx.cFun.curBB, "pre_whileCond", ctx.cFun.curBB.loopLevel);
		BasicBlock cond = ctx.NewBBAfter(preheader, "whileCond", ctx.cFun.curBB.loopLevel);
		BasicBlock step = ctx.NewBBAfter(cond, "whileStep", ctx.cFun.curBB.loopLevel + 1);
		BasicBlock after = ctx.NewBBAfter(step, "whileAfter", ctx.cFun.curBB.loopLevel);
		ctx.RecordLoop(cond, after);
		
		ctx.SetCurBB(cond);
		if (node.cond instanceof LogicBinaryExp) {
			// while for short-circuit evaluation.
			node.cond.ifTrue = step;
			node.cond.ifFalse = after;
			node.cond.Accept(this);
		}
		else {
			// while for ordinary cond.
			node.cond.Accept(this);
			IrValue brCond = GetArithResult(node.cond);
			// cond links to then and ifFalse for CFG.
			ctx.EmplaceInst(new Branch(brCond, step, after));
		}
		
		// record loop info for break and continue
		ctx.SetCurBB(step);
		node.body.Accept(this);
		ctx.EmplaceInst(new Jump(cond));
		// ctx.CompleteCurBB();
		
		ctx.ExitLoop();
		ctx.SetCurBB(after);
		return null;
	}
	
	@Override
	public Void visit(ForStm node) {
		assert ctx.cFun.curBB.loopLevel != null;
		BasicBlock preheader = ctx.NewBBAfter(ctx.cFun.curBB, "pre_forCond", ctx.cFun.curBB.loopLevel);
		BasicBlock check = ctx.NewBBAfter(preheader, "forCond", ctx.cFun.curBB.loopLevel);
		BasicBlock step = ctx.NewBBAfter(check, "forStep", ctx.cFun.curBB.loopLevel + 1);
		BasicBlock after = ctx.NewBBAfter(step, "forAfter", ctx.cFun.curBB.loopLevel);
		ctx.RecordLoop(check, after);

		ctx.SetCurBB(preheader);
		// forInit is executed before loop BB
		if (node.initIsDec) {
			if (node.initDec != null) node.initDec.Accept(this);
		} else {
			if (node.initExps != null) node.initExps.forEach(x -> x.Accept(this));
		}

		ctx.SetCurBB(check);
		if (node.check != null) {
			if (node.check instanceof LogicBinaryExp) {
				node.check.ifTrue = step;
				node.check.ifFalse = after;
				node.check.Accept(this);
			}
			else {
				node.check.Accept(this);
				IrValue brCond = GetArithResult(node.check);
				ctx.EmplaceInst(new Branch(brCond, step, after));
			}
		}
		// ctx.CompleteCurBB();
		
		ctx.SetCurBB(step);
		node.body.Accept(this);
		node.updateExps.forEach(x -> x.Accept(this));
		ctx.EmplaceInst(new Jump(check));
		// ctx.CompleteCurBB();
		
		ctx.ExitLoop();
		ctx.SetCurBB(after);
		return null;
	}
	@Override
	public Void visit(ContinueStm node) {
		BasicBlock contin = ctx.GetContin();
		ctx.EmplaceInst(new Jump(contin));
		ctx.EarlyStopCurBB();
		return null;
	}
	
	@Override
	public Void visit(BreakStm node) {
		BasicBlock after = ctx.GetBreak();
		ctx.EmplaceInst(new Jump(after));
		ctx.EarlyStopCurBB();
		return null;
	}
	
	
	/********************** Primitives and Literals *********************/
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
	
	@Override
	public Void visit(StringLiteralExp node) {
		// get the string address from which we could find the string head pointer.
		// NOTE why here is strAddr, see above explanation.
//		String cleanStr = unescape(node.val);
		Reg strAddr = ctx.TraceGlobalString(node.val);
		node.setIrAddr(strAddr);
		return null;
	}
	
	@Override
	public Void visit(NullExp node) {
		node.setIrValue(MakeGreg("null"));
		return null;
	}
	
	
	private static Map<String, Binary.Op> binaryOpMap = new HashMap<>();
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
	
	
	
	/***************** explicit Traversal Utility **********************/
	@Override
	public Void visit(ExpStm node) {
		node.exp.Accept(this);
		return null;
	}
	
	@Override
	public Void visit(BlockStm node) {
		for (Stm stm : node.stms) {
			stm.Accept(this);
		}
		return null;
	}
	
	@Override
	public Void visit(Type node) {
		if (node.dim != null) node.dim.Accept(this);
		if (node.innerType != null) node.innerType.Accept(this);
		return null;
	}
	
	/**
	 * ******************** Utility **************************
	 * A brilliant result, exploit values attached on AST node.
	 * */
	private IrValue GetArithResult(Exp node) {
		if (node.getIrValue() == null) {
			Reg addr = node.getIrAddr();
			assert addr != null;
			
			// load the value into a tmpVal.
			Reg tmpVal = ctx.cFun.GetTmpReg();
			ctx.EmplaceInst(new Load(tmpVal, addr));
			node.setIrValue(tmpVal);
			return tmpVal;
		}
		return node.getIrValue();
	}
	
	/**
	 * Get global register with hintName neatly.
	 * */
	private void Mumble(String str) {
		ctx.EmplaceInst(new Comment(str));
	}
	
	
	public int CalTypeSize(Type type) {
		assert !type.isNull() && !type.isVoid();
		
		int typeSpace = 0;
		if (type.isArray() || type.isInt() || type.isBool() || type.isString())
			typeSpace = INT_SIZE;
		else {
			ClassDec class_ = ctx.irClassTable.get(type.GetBaseTypeName());
			for (VarDec field : class_.fields) typeSpace += INT_SIZE;
		}
		return typeSpace;
	}
}
