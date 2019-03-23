package ir;

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
import ir.structure.*;
import semantic.AstTraverseVisitor;

import java.util.*;

import static ir.Config.INT_SIZE;
import static ir.Utility.*;
import static ir.quad.Binary.Op.*;
import static ir.quad.Unary.Op.BITNOT;
import static ir.quad.Unary.Op.NEG;


/**
 * For building actions.
 *
 * NOTE : we use `name to mark the reserved internal local variable, such as the results of
 * NOTE : short-circuit evaluation, or the iterator inside new T[][]. Why we do this?
 * NOTE : because they need to be stored in mem.
 * NOTE : their namespace should be safe without collision inside a function, and maybe we don't
 * NOTE : expect to do optimization between functions.
 * */
public class Builder extends AstTraverseVisitor<Void> {

	/**
	 * For all building dependencies and results.
	 * */
	private BuilderContext ctx;
	
	private Prog ast;
	
	public Builder(BuilderContext ctx) {
		this.ctx = ctx;
	}
	
	public void build(Prog prog) {
		ast = prog;
		BuildIR();
		ctx.ConstructCFG();
	}
	
	private void BuildIR() {
		visit(ast);
	}
	
	@Override
	public Void visit(Prog node) {
		Function init = ctx.FuncGen("_init_");
		ctx.SetCurFunc(init);
		for (Dec child : node.children) {
			if (child instanceof GlobalVarDec)
				child.Accept(this);
		}
		ctx.GetCurBB().Complete();
		
		for (Dec child : node.children) {
			if (!(child instanceof GlobalVarDec))
				child.Accept(this);
		}
		return null;
	}
	
	/**
	 * **************************************** Decs Defs ******************************************
	 * Global variables have no naming collision.
	 * */
	private void AddGlobalVar(VarDec node) {
		Reg var = MakeGreg(node.varName);
		// global variable don't need to be alloca, since they don't need to be Ssa.
//		ctx.EmplaceInst(new Alloca(var));
		ctx.AddGlobalVar(node, var);
		
		// initialization ir for global var are set in function _init_.
		if (node.inital != null) {
			node.inital.Accept(this);
			IrValue initVal = GetArithResult(node.inital);
			ctx.EmplaceInst(new Store(var, initVal));
		}
	}
	
	@Override
	public Void visit(GlobalVarDec node) {
		node.varDecs.forEach(this::AddGlobalVar);
		return null;
	}
	
	/**
	 * Note : we do renaming.
	 * */
	@Override
	public Void visit(FunctDec node) {
		// a new function is about to be generated, set the last BB of the last function to be completed.
		if (!ctx.GetCurBB().complete) ctx.GetCurBB().Complete();
		Function func = ctx.FuncGen(node.functName);
		ctx.SetCurFunc(func);
		
		node.arguments.varDecs.forEach(x -> x.varName = ctx.RenameLocal(x.varName));
		node.arguments.varDecs.forEach(x -> func.formalArgs.add(x.varName));
		
		for (int i = 0; i < func.formalArgs.size(); ++i) {
			Reg var = new Reg(func.formalArgs.get(i));
			Reg reg = ctx.GetTmpReg();
			func.regArgs.add(reg);
			ctx.EmplaceInst(new Alloca(var));
			ctx.EmplaceInst(new Store(var, reg));
			ctx.BindVarToDec(node.arguments.varDecs.get(i), var);
		}
		
		for (Stm stm : node.functBody) {
			stm.Accept(this);
		}
		return null;
	}
	
	/**
	 * NOTE : we do method renaming, to be -class#method
	 * */
	@Override
	public Void visit(MethodDec node) {
		String methodRenaming = AddPrefix(node.parentClass, node.functName);
		Function func = ctx.FuncGen(methodRenaming);
		ctx.SetCurFunc(func);
		
		node.arguments.varDecs.forEach(x -> x.varName = ctx.RenameLocal(x.varName));
		node.arguments.varDecs.forEach(x -> func.formalArgs.add(x.varName));
		func.formalArgs.add(0, "this");
		
		for (int i = 0; i < func.formalArgs.size(); ++i) {
			Reg var = new Reg(func.formalArgs.get(i));
			Reg reg = ctx.GetTmpReg();
			func.regArgs.add(reg);
			ctx.EmplaceInst(new Alloca(var));
			ctx.EmplaceInst(new Store(var, reg));
			if (func.formalArgs.get(i).equals("this"))
				func.this_ = var;
			else
				ctx.BindVarToDec(node.arguments.varDecs.get(i), var);
		}
		
		for (Stm stm : node.functBody) {
			stm.Accept(this);
		}
		return null;
	}
	
	/**
	 * Be careful about class member access.
	 * Do nothing about field.
	 * */
	@Override
	public Void visit(ClassDec node) {
		node.method.forEach(x -> x.Accept(this));
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
		node.varName = ctx.RenameLocal(node.varName);
		Reg var = new Reg(node.varName);
		ctx.EmplaceInst(new Alloca(var));
		
		if (node.inital != null && !node.inital.varTypeRef.isNull()) {
			node.inital.Accept(this);
			IrValue initVal = GetArithResult(node.inital);
			ctx.EmplaceInst(new Store(var, initVal));
		}
		
		ctx.BindVarToDec(node, var);
		return null;
	}
	
	/**
	 * ********************************** Exp Uses *******************************
	 * We would encounter defined local variable, or class field
	 * in class methods, distinguish them.
	 * */
	@Override
	public Void visit(VarExp node) {
		VarDec varDec = node.varDec;
		if (varDec.parentClass != null) {
			Reg this_ = ctx.GetThis();
			Mumble("start a field eval");
			// load this.
			Reg headPtr = ctx.GetTmpReg();
			ctx.EmplaceInst(new Load(headPtr, this_));
			// find offset of field in class layout
			ClassDec layout = varDec.parentClass;
			// field name hasn't been renamed.
			// it shouldn't be renamed.
			String fieldName = node.varName;
			Constant offset = new Constant(layout.classType.GetFieldOffset(fieldName));
			Reg fieldPtr = ctx.GetTmpReg();
			ctx.EmplaceInst(new Binary(fieldPtr, ADD, headPtr, offset));
			node.setIrAddr(fieldPtr);
		}
		else {
			Reg var = ctx.TraceVar(varDec);
			node.setIrAddr(var);
		}
		
		return null;
	}
	
	@Override
	public Void visit(SuffixExp node) {
		node.objInstance.Accept(this);
		// NOTE : it is addr because node is a lvalue.
		Reg var = node.objInstance.getIrAddr();
		
		IrValue oldVal = GetArithResult(node.objInstance);
		Reg increVal = ctx.GetTmpReg();
		
		ctx.EmplaceInst(new Binary(increVal, ADD, oldVal, MakeInt(1)));
		ctx.EmplaceInst(new Store(var, increVal));
		
		// set IrAddr to null just for clarity, showing that it cannot be queried, otherwise leads to assertion failure.
		node.setIrAddr(null);
		node.setIrValue(oldVal);
		return null;
	}
	
	@Override
	public Void visit(PrefixExp node) {
		node.objInstance.Accept(this);
		
		IrValue oldVal = GetArithResult(node.objInstance);
		switch (node.op) {
			case "++": case "--":
				Reg var = node.objInstance.getIrAddr();
				// similar to Suffix
				Reg newVal = ctx.GetTmpReg();
				Binary.Op op = (node.op.equals("++")) ? ADD : SUB;
				ctx.EmplaceInst(new Binary(newVal, op, oldVal, MakeInt(1)));
				ctx.EmplaceInst(new Store(var, newVal));
				// prefix is still a lvalue.
				node.setIrValue(newVal);
				node.setIrAddr(var);
				break;
			case "~": case "-":
				Reg newVal_ = ctx.GetTmpReg();
				Unary.Op op_ = (node.op.equals("~")) ? BITNOT : NEG;
				ctx.EmplaceInst(new Unary(newVal_, op_, oldVal));
				
				node.setIrAddr(null);
				node.setIrValue(newVal_);
				break;
			case "!":
				assert (node.ifTrue == null) == (node.ifFalse == null);
				IrValue bool = GetArithResult(node.objInstance);
				Reg oppoVal = ctx.GetTmpReg();
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
		
		Reg ans = ctx.GetTmpReg();
		IrValue lVal = GetArithResult(node.lhs);
		IrValue rVal = GetArithResult(node.rhs);
		
		Binary.Op op;
		if (node.op.equals("+") && node.lhs.varTypeRef.isString())
			op = CONCAT;
		else
			op = binaryOpMap.get(node.op);
		
		ctx.EmplaceInst(new Binary(ans, op, lVal, rVal));
		node.setIrValue(ans);
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
	 * Sub-function used by visit(CreationExp node)
	 * */
	private void GetArrayDimension(VarTypeRef type, Queue<IrValue> dims)  {
		// varTypeRef has been traversed.
		if (type.innerType == null)
			return;
		
		if (type.dim != null) {
			dims.add(GetArithResult(type.dim));
			GetArrayDimension(type.innerType, dims);
		}
	}
	
	private Reg MakeNewNonArray(VarTypeRef type) {
		// tmpVar is going to be returned to var, and be stored.
		Reg tmpVar = ctx.GetTmpReg();
		Constant size_ = MakeInt(CalTypeSize(type));
		ctx.EmplaceInst(new Malloc(tmpVar, size_));
		
		ClassDec userClass = type.baseType;
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
		Reg arrTmp = ctx.GetTmpReg();
		// malloc some memory with suitable dimension, and let arrAddr takes on its address.
		IrValue dim = dims.poll();
		// add 1 to dim to hold array size.
		Reg exDim = ctx.GetTmpReg();
		ctx.EmplaceInst(new Binary(exDim, ADD, dim, MakeInt(1)));
		// multiply INT_SIZE to get the actual offset, use << for efficiency.
		Reg exDimByte = ctx.GetTmpReg();
		ctx.EmplaceInst(new Binary(exDimByte, SHL, exDim, MakeInt(2)));
		// malloc mem space, let arrAddr take on the address.
		ctx.EmplaceInst(new Malloc(arrTmp, exDimByte));
		// record array dimension after recording, right at the head of malloc_ed mem.
		ctx.EmplaceInst(new Store(arrTmp, dim));
		
		// if there's no other dimensions to create, stop and return.
		if (dims.isEmpty()) {
			return arrTmp;
		}
		
		// otherwise, construct a loop to create pointers for inner dimensions.
		// collect and store the addresses malloc_ed during sub-problems into currently malloc_ed address.
		
		// first, get a endMarker, when we at the endMarker, we are right outside the array.
		// which means if (curMarker == endMarker) break;
		Reg arrEndMark = ctx.GetTmpReg();
		ctx.EmplaceInst(new Binary(arrEndMark, ADD, arrTmp, exDimByte));
		
		
		// before enter the loop, initialize a loop iterator.
		Reg iter = ctx.GetReserveReg("itr");
		ctx.EmplaceInst(new Alloca(iter));
		// init the iter to be the addr of 1st element.
		Reg initAddr = ctx.GetTmpReg();
		IrValue initByte = MakeInt(INT_SIZE);
		ctx.EmplaceInst(new Binary(initAddr, ADD, arrTmp, initByte));
		ctx.EmplaceInst(new Store(iter, initAddr));
		ctx.GetCurBB().Complete();
		
		BasicBlock cond = ctx.NewBBAfter(ctx.GetCurBB(), "cond_n");
		BasicBlock then = ctx.NewBBAfter(cond, "then_n");
		BasicBlock after = ctx.NewBBAfter(then, "after_n");
		
		// when we haven't exceed the array to be constructed, continue to stay in then BB.
		ctx.SetCurBB(cond);
		Reg cursor = ctx.GetTmpReg();
		ctx.EmplaceInst(new Load(cursor, iter));
		Reg cmp = ctx.GetTmpReg();
		ctx.EmplaceInst(new Binary(cmp, EQ, cursor, arrEndMark));
		// if equal, break the loop
		ctx.EmplaceInst(new Branch(cmp, after, then));
		ctx.GetCurBB().Complete();
		
		// now we've entered the loop, we get sub-addr and store it in the address specified by curAddr.
		ctx.SetCurBB(then);
		Reg subArrTmp = MakeNewArray(dims);
		// setCurBB to be after when this funct ends, thus we end up in safe basic block.
		ctx.EmplaceInst(new Store(cursor, subArrTmp));
		// increment curAddr and store it in iter.
		Reg increCursor = ctx.GetTmpReg();
		ctx.EmplaceInst(new Binary(increCursor, ADD, cursor, MakeInt(INT_SIZE)));
		ctx.EmplaceInst(new Store(iter, increCursor));
		// redirect to condition check.
		ctx.EmplaceInst(new Jump(cond));
		ctx.GetCurBB().Complete();
		
		ctx.SetCurBB(after);
		
		return arrTmp;
	}
	
	/************************* subscript / field ************************************/
	@Override
	public Void visit(FieldAccessExp node) {
		node.objInstance.Accept(this);
		
		Reg instAddr = node.objInstance.getIrAddr();
		Reg elemAddr = ctx.GetTmpReg();
		
		// get offset
		VarTypeRef instType = node.objInstance.varTypeRef;
		String member = node.memberName;
		IrValue offset = MakeInt(instType.GetFieldOffset(member));
		
		ctx.EmplaceInst(new Binary(elemAddr, ADD, instAddr, offset));
		
		node.setIrAddr(elemAddr);
		return null;
	}
	
	@Override
	public Void visit(ArrayAccessExp node) {
		node.arrInstance.Accept(this);
		node.accessor.Accept(this);
		
		IrValue baseAddr = GetArithResult(node.arrInstance);
		
		// NOTE : These are simple tricks, later we do it.
		// NOTE : if no offset, no need to get element.
		
		Reg elemAddr = ctx.GetTmpReg();
		// FIXME : when we use java convention, all array type element is a pointer or built-in type.
//		VarTypeRef arrElemType = node.arrInstance.varTypeRef.innerType;
//		IrValue elemSize = MakeInt(arrElemType.GetTypeSpace());
		IrValue elemSize = MakeInt(4);
		
		// now we need a MUL quad for offset calculation
		// shift index, because the first one is reserved to be array size.
		IrValue lenSpare = MakeInt(4);
		IrValue acsIndex = GetArithResult(node.accessor);
		Reg offsetNoLen = ctx.GetTmpReg();
		Reg offsetWithLen = ctx.GetTmpReg();
		
		// calculate array member access and reserve for length shift, add them up.
		ctx.EmplaceInst(new Binary(offsetNoLen, MUL, acsIndex, elemSize));
		ctx.EmplaceInst(new Binary(offsetWithLen, ADD, offsetNoLen, lenSpare));
		ctx.EmplaceInst(new Binary(elemAddr, ADD, baseAddr, offsetWithLen));
		node.setIrAddr(elemAddr);
		
		return null;
	}
	
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
		
		Reg retVal = (node.varTypeRef.isVoid()) ?
						MakeGreg("null") : ctx.GetTmpReg();
		
		// handle function name format -- built-in ? method ?
		String mName = (node.isMethodCall()) ?
						AddPrefix(((MethodCallExp) node).calledClass, node.functName) :
						node.functName;
		String btinPref = "~";
		String btmName = (node.functDec.builtIn) ? (btinPref + mName) : mName;
		
		// handle method 'this' pointer problem.
		if (!node.isMethodCall()) {
			ctx.EmplaceInst(new Call(btmName, retVal, args));
		} else {
			/**
			 * If we are in a class method already, and we are invoking another class method.
			 * note that by default 'this' register is $0.
			 * */
			Reg thisVal = ctx.GetTmpReg();
			ctx.EmplaceInst(new Load(thisVal, ctx.GetThis()));
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
		node.objInstance.Accept(this);
		// FIXME : here should be instValue, while instAddr is the address where the instance pointer is stored.
		// FIXME : instAddr must be a Reg, because it holds an address.

//    Reg instAddr = node.objInstance.getIrAddr();
		Reg instAddr = (Reg) GetArithResult(node.objInstance);
		
		List<IrValue> args = new LinkedList<>();
		args.add(instAddr);
		for (Exp arg : node.arguments.args) {
			arg.Accept(this);
			args.add(GetArithResult(arg));
		}
		
		Reg retVal = (node.varTypeRef.isVoid()) ?
						MakeGreg("null") : ctx.GetTmpReg();
		
		// add prefix ~ as marker for built-in function, this helps interpreter to parse.
		String mName = AddPrefix(node.calledClass, node.functName);
		String btmName = (node.functDec.builtIn) ? "~" + mName : mName;
		
		ctx.EmplaceInst(new Call(btmName, retVal, args));
		node.setIrValue(retVal);
		return null;
	}
	
	@Override
	public Void visit(ReturnStm node) {
		IrValue retVal;
		if (node.retVal != null) {
			node.retVal.Accept(this);
			retVal = GetArithResult(node.retVal);
		} else {
			retVal = MakeGreg("null");
		}
		ctx.EmplaceInst(new Ret(retVal));
		return null;
	}
	
	@Override
	public Void visit(ThisExp node) {
		node.setIrAddr(ctx.GetThis());
		return null;
	}
	
	
	/******************************* Control flow ********************************/
	/**
	 * local register used for short-circuit eval result storage.
	 * */
	private Stack<Reg> logicVars = new Stack<>();
	
	@Override
	public Void visit(LogicBinaryExp node) {
		assert (node.ifTrue == null) == (node.ifFalse == null);
		
		boolean startLogic = node.ifFalse == null;
		
		if (startLogic) {
			// create logicVar to place logic result.
			Reg logi = ctx.GetReserveReg("logi");
			ctx.EmplaceInst(new Alloca(logi));
			logicVars.push(logi);
			
			// create merge collector block.
			BasicBlock merge = ctx.NewBBAfter(ctx.GetCurBB(), "merge");
			node.ifTrue = merge;
			node.ifFalse = merge;
			
			// distribute jump target.
			BasicBlock rhsBB = ctx.NewBBAfter(ctx.GetCurBB(), node.op + "rhs");
			PushDownTargetBB(node, rhsBB);
			// child evaluation and record jump target&value pair.
			ChildEvaluation(node.lhs);
			ctx.GetCurBB().Complete();
			
			// for rhs
			ctx.SetCurBB(rhsBB);
			ChildEvaluation(node.rhs);
			ctx.GetCurBB().Complete();

			ctx.SetCurBB(merge);
			node.setIrAddr(logi);
			// recursion back to it.
			logicVars.pop();
		}
		else {
			// distribute jump target.
			BasicBlock rhsBB = ctx.NewBBAfter(ctx.GetCurBB(), node.op + "rhs");
			PushDownTargetBB(node, rhsBB);
			
			// downstream evaluation, and record jump target&value pair.
			ChildEvaluation(node.lhs);
			ctx.GetCurBB().Complete();
			
			ctx.SetCurBB(rhsBB);
			ChildEvaluation(node.rhs);
			ctx.GetCurBB().Complete();
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
			Reg logi = logicVars.peek();
			ctx.EmplaceInst(new Store(logi, val));
			ctx.EmplaceInst(new Branch(val, node.ifTrue, node.ifFalse));
			ctx.GetCurBB().Complete();
		}
		// next short node, set and evaluate.
		else {
			node.Accept(this);
		}
	}
	
	@Override
	public Void visit(IfStm node) {
		BasicBlock then = ctx.NewBBAfter(ctx.GetCurBB(), "then");
		BasicBlock merge = ctx.NewBBAfter(then, "merge");
		BasicBlock ifFalse = (node.elseBody != null) ? ctx.NewBBAfter(then, "else") : merge;
		
		// condition may set us to curBB or mergeBB, we resume to genIR.
		node.condition.Accept(this);
		IrValue brCond = GetArithResult(node.condition);
		// cond links to then and ifFalse for CFG.
		ctx.EmplaceInst(new Branch(brCond, then, ifFalse));
		ctx.GetCurBB().Complete();
		
		// then irGen, assume no curBB recovery.
		ctx.SetCurBB(then);
		node.thenBody.Accept(this);
		
		// emplace 'jump' from then to merge if necessary.
		if (node.elseBody == null) {
			ctx.GetCurBB().Complete();
		} else {
			ctx.EmplaceInst(new Jump(merge));
			ctx.GetCurBB().Complete();
			
			ctx.SetCurBB(ifFalse);
			node.elseBody.Accept(this);
			ctx.GetCurBB().Complete();
		}
		// set BB to merge
		ctx.SetCurBB(merge);
		
		return null;
	}
	
	
	
	@Override
	public Void visit(WhileStm node) {
		
		BasicBlock cond = ctx.NewBBAfter(ctx.GetCurBB(), "cond");
		BasicBlock step = ctx.NewBBAfter(cond, "step");
		BasicBlock after = ctx.NewBBAfter(step, "after");
		ctx.RecordLoop(cond, after);
		ctx.GetCurBB().Complete();
		
		ctx.SetCurBB(cond);
		node.condition.Accept(this);
		IrValue brCond = GetArithResult(node.condition);
		// emplace branch
		ctx.EmplaceInst(new Branch(brCond, step, after));
		ctx.GetCurBB().Complete();
		
		// record loop info for break and continue
		ctx.SetCurBB(step);
		node.whileBody.Accept(this);
		ctx.EmplaceInst(new Jump(cond));
		ctx.GetCurBB().Complete();
		
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
		ctx.GetCurBB().Complete();
		
		BasicBlock check = ctx.NewBBAfter(ctx.GetCurBB(), "cond");
		BasicBlock step = ctx.NewBBAfter(check, "step");
		BasicBlock after = ctx.NewBBAfter(step, "after");
		ctx.RecordLoop(check, after);
		
		ctx.SetCurBB(check);
		if (node.forControl.check != null) {
			node.forControl.check.Accept(this);
			IrValue brCond = GetArithResult(node.forControl.check);
			ctx.EmplaceInst(new Branch(brCond, step, after));
		}
		ctx.GetCurBB().Complete();
		
		ctx.SetCurBB(step);
		node.forBody.Accept(this);
		node.forControl.updateExps.forEach(x -> x.Accept(this));
		ctx.EmplaceInst(new Jump(check));
		ctx.GetCurBB().Complete();
		
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
		String cleanStr = unescape(node.value);
		Reg strAddr = ctx.TraceGlobalString(cleanStr);
		node.setIrAddr(strAddr);
		return null;
	}
	
	@Override
	public Void visit(NullExp node) {
		node.setIrValue(MakeGreg("null"));
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
			Reg tmpVal = ctx.GetTmpReg();
			ctx.EmplaceInst(new Load(tmpVal, addr));
			node.setIrValue(tmpVal);
			return tmpVal;
		}
		return node.getIrValue();
	}
	
	private void Mumble(String str) {
		ctx.EmplaceInst(new Comment(str));
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
}
