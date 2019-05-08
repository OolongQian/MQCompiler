package ast;

import antlr_tools.MgBaseVisitor;
import antlr_tools.MgParser;
import ast.node.Ast;
import ast.node.Prog;
import ast.node.dec.*;
import ast.node.exp.*;
import ast.node.stm.*;
import ast.type.Type;

import java.util.Map;

/**
 * Constructor's return type is null.
 * */
public class Builder extends MgBaseVisitor<Ast> {
	
	@Override
	public Ast visitProg(MgParser.ProgContext ctx) {
		Prog prog = new Prog(); prog.SetPosition(ctx);
		ctx.dec().forEach(x -> prog.decs.add((Dec) visit(x)));
		return prog;
	}
	
	@Override
	public Ast visitDec(MgParser.DecContext ctx) {
		return visit(ctx.getChild(0));
	}

	// create plain VarDecs, and set them to be global
	@Override
	public Ast visitGlobalDec(MgParser.GlobalDecContext ctx) {
		VarDecList vDecs = (VarDecList) visit(ctx.varDec());
		vDecs.varDecs.forEach(VarDec::setGlobal);
		return vDecs;
	}
	
	@Override
	public Ast visitFieldDec(MgParser.FieldDecContext ctx) {
		VarDecList vDecs = (VarDecList) visit(ctx.varDec());
		vDecs.varDecs.forEach(VarDec::setField);
		return vDecs;
	}
	

	
	@Override
	public Ast visitVarDec(MgParser.VarDecContext ctx) {
		VarDecList varDecs = new VarDecList(); varDecs.SetPosition(ctx);
		Type type = (Type) visit(ctx.type());
		for (MgParser.VarDeclaratorContext vDec : ctx.varDeclaratorList().varDeclarator()) {
			VarDec varDec = new VarDec(vDec.Identifier().getText()); varDec.SetPosition(ctx);
			varDec.type = type;
			if (vDec.exp() != null) {
				varDec.inital = (Exp) visit(vDec.exp());
			}
			varDecs.varDecs.add(varDec);
		}
		return varDecs;
	}
	
	@Override
	public Ast visitFunctDec(MgParser.FunctDecContext ctx) {
		FunctDec func = new FunctDec(ctx.functName.getText()); func.SetPosition(ctx);
		func.retType = (Type) visit(ctx.type());
		
		if (ctx.functDecParaList() != null) {
			for (MgParser.FunctDecParaContext arg : ctx.functDecParaList().functDecPara()) {
				func.args.add((VarDec) visit(arg));
			}
		}
		ctx.blockStm().stm().forEach(x -> func.body.add((Stm) visit(x)));
		return func;
	}
	
	@Override
	public Ast visitClassDec(MgParser.ClassDecContext ctx) {
		ClassDec classDec = new ClassDec(ctx.className.getText()); classDec.SetPosition(ctx);
		
		// child can be ctor, method, field.
		Dec child;
		for (MgParser.ClassBodyDecContext subCtx : ctx.classBody().classBodyDec()) {
			child = (Dec) visit(subCtx);
			if (child instanceof VarDecList) {
				for (VarDec vDec : ((VarDecList) child).varDecs) {
					assert vDec.isField();
					classDec.fields.add(vDec);
				}
			}
			// child can be method, and ctor is set in constructorDecCtx.
			else if (child instanceof FunctDec) {
				FunctDec funChild = (FunctDec) child;
				assert funChild.isMethod();
				classDec.methods.add(funChild);
				// ctor is inserted twice.
				if ((funChild.isCtor()))
					classDec.ctor = funChild;
			}
			else {
				assert false;
			}
		}
		return classDec;
	}
	
	@Override
	public Ast visitClassBodyDec(MgParser.ClassBodyDecContext ctx) {
		return visit(ctx.getChild(0));
	}
	
	/**
	 * NOTE constructor has no type assigned yet !
	 * */
	@Override
	public Ast visitConstructorDec(MgParser.ConstructorDecContext ctx) {
		FunctDec ctor = new FunctDec(ctx.className.getText()); ctor.SetPosition(ctx);
		ctor.setMethod();
		ctor.setCtor();
		ctor.retType = Type.CreateBaseType("void");
		if (ctx.functDecParaList() != null) {
			for (MgParser.FunctDecParaContext arg : ctx.functDecParaList().functDecPara()) {
				ctor.args.add((VarDec) visit(arg));
			}
		}
		ctx.blockStm().stm().forEach(x -> ctor.body.add((Stm) visit(x)));
		return ctor;
	}
	
	@Override
	public Ast visitMethodDec(MgParser.MethodDecContext ctx) {
		FunctDec method = (FunctDec) visit(ctx.functDec());
		method.setMethod();
		return method;
	}
	
	@Override
	public Ast visitFunctDecPara(MgParser.FunctDecParaContext ctx) {
		VarDec varDec = new VarDec(ctx.Identifier().getText()); varDec.SetPosition(ctx);
		varDec.type = (Type) visit(ctx.type());
		return varDec;
	}
	
	@Override
	public Ast visitType(MgParser.TypeContext ctx) {
		return visit(ctx.getChild(0));
	}
	
	@Override
	public Ast visitSimpleType(MgParser.SimpleTypeContext ctx) {
		Type type = new Type(); type.SetPosition(ctx);
		type.typeName = ctx.getText();
		return type;
	}
	
	@Override
	public Ast visitArrayType(MgParser.ArrayTypeContext ctx) {
		Type type = new Type();
		type.typeName = ctx.simpleType().getText();
		for (MgParser.ArrayDimDecContext dimCtx : ctx.arrayDimDecList().arrayDimDec()) {
			type = Type.CreateArrayType(type, null);
		}
		return type;
	}
	
	/**
	 * create a shift between exp and stm.
	 * */
	@Override
	public Ast visitVarDecStm(MgParser.VarDecStmContext ctx) {
		VarDecList varDecList = (VarDecList) visit(ctx.varDec());
		return new LocalVarDecStm(varDecList.varDecs);
	}
	
	@Override
	public Ast visitIfStm(MgParser.IfStmContext ctx) {
		IfStm ifStm = new IfStm(); ifStm.SetPosition(ctx);
		ifStm.cond = (Exp) visit(ctx.exp());
		ifStm.thenBody = (Stm) visit(ctx.then_);
		ifStm.elseBody = (ctx.else_ != null) ? (Stm) visit(ctx.else_) : null;
		return ifStm;
	}
	
	@Override
	public Ast visitWhileStm(MgParser.WhileStmContext ctx) {
		WhileStm whileStm = new WhileStm(); whileStm.SetPosition(ctx);
		whileStm.cond = (Exp) visit(ctx.exp());
		whileStm.body = (Stm) visit(ctx.stm());
		return whileStm;
	}
	
	@Override
	public Ast visitForStm(MgParser.ForStmContext ctx) {
		ForStm forStm = new ForStm(); forStm.SetPosition(ctx);
		ForControl control = (ForControl) visit(ctx.forControl());
		forStm.AssginControl(control);
		
		forStm.body = (Stm) visit(ctx.stm());
		return forStm;
	}
	
	@Override
	public Ast visitForControl(MgParser.ForControlContext ctx) {
		ForControl cont = new ForControl(); cont.SetPosition(ctx);
		if (ctx.forInit() != null) {
			if (ctx.forInit().varDec() != null) {
				cont.initIsDec = true;
				cont.initDec = (VarDecList) visit(ctx.forInit().varDec());
			} else {
				// then, for init must be expList.
				cont.initIsDec = false;
				ctx.forInit().expList().exp().forEach(x -> cont.initExps.add((Exp) visit(x)));
			}
		}
		cont.check = (ctx.exp() != null) ? (Exp) visit(ctx.exp()) : null;
		if (ctx.forUpdate() != null) {
			ctx.forUpdate().expList().exp().forEach(x -> cont.updateExps.add((Exp) visit(x)));
		}
		return cont;
	}
	
	@Override
	public Ast visitBreakStm(MgParser.BreakStmContext ctx) {
		BreakStm breakStm = new BreakStm(); breakStm.SetPosition(ctx);
		return breakStm;
	}
	
	@Override
	public Ast visitContinueStm(MgParser.ContinueStmContext ctx) {
		ContinueStm continueStm = new ContinueStm();
		continueStm.SetPosition(ctx);
		return continueStm;
	}
	
	@Override
	public Ast visitReturnStm(MgParser.ReturnStmContext ctx) {
		ReturnStm returnStm = new ReturnStm(); returnStm.SetPosition(ctx);
		returnStm.ret = (ctx.exp() != null) ? (Exp) visit(ctx.exp()) : null;
		return returnStm;
	}
	
	@Override
	public Ast visitBlockStm(MgParser.BlockStmContext ctx) {
		BlockStm blockStm = new BlockStm(); blockStm.SetPosition(ctx);
		if (ctx.stm() != null) {
			ctx.stm().forEach(x -> blockStm.stms.add((Stm) visit(x)));
		}
		return blockStm;
	}
	
	@Override
	public Ast visitEmptyStm(MgParser.EmptyStmContext ctx) {
		EmptyStm emptyStm = new EmptyStm(); emptyStm.SetPosition(ctx);
		return emptyStm;
	}
	
	@Override
	public Ast visitSuffixExp(MgParser.SuffixExpContext ctx) {
		SuffixExp suffixExp = new SuffixExp(); suffixExp.SetPosition(ctx);
		suffixExp.obj = (Exp) visit(ctx.exp());
		suffixExp.op = ctx.op.getText();
		return suffixExp;
	}
	
	@Override
	public Ast visitPrefixExp(MgParser.PrefixExpContext ctx) {
		PrefixExp prefixExp = new PrefixExp(); prefixExp.SetPosition(ctx);
		prefixExp.obj = (Exp) visit(ctx.exp());
		prefixExp.op = ctx.op.getText();
		return prefixExp;
	}
	
	@Override
	public Ast visitArithBinaryExp(MgParser.ArithBinaryExpContext ctx) {
		ArithBinaryExp arithBin = new ArithBinaryExp(); arithBin.SetPosition(ctx);
		arithBin.lhs = (Exp) visit(ctx.lhs);
		arithBin.rhs = (Exp) visit(ctx.rhs);
		arithBin.op = ctx.op.getText();
		return arithBin;
	}
	
	@Override
	public Ast visitLogicBinaryExp(MgParser.LogicBinaryExpContext ctx) {
		LogicBinaryExp logicBin = new LogicBinaryExp(); logicBin.SetPosition(ctx);
		logicBin.lhs = (Exp) visit(ctx.lhs);
		logicBin.rhs = (Exp) visit(ctx.rhs);
		logicBin.op = ctx.op.getText();
		return logicBin;
	}
	
	@Override
	public Ast visitAssignExp(MgParser.AssignExpContext ctx) {
		AssignExp assignExp = new AssignExp(); assignExp.SetPosition(ctx);
		assignExp.dst = (Exp) visit(ctx.lhs);
		assignExp.src = (Exp) visit(ctx.rhs);
		return assignExp;
	}
	
	@Override
	public Ast visitPrimitiveExp(MgParser.PrimitiveExpContext ctx) {
		return visit(ctx.primaryExp());
	}
	
	@Override
	public Ast visitParenPrimaryExp(MgParser.ParenPrimaryExpContext ctx) {
		return visit(ctx.exp());
	}
	
	@Override
	public Ast visitSelfPrimaryExp(MgParser.SelfPrimaryExpContext ctx) {
		ThisExp thisExp = new ThisExp(); thisExp.SetPosition(ctx);
		return thisExp;
	}
	
	@Override
	public Ast visitLiteral(MgParser.LiteralContext ctx) {
		return visit(ctx.getChild(0));
	}
	
	@Override
	public Ast visitIntegerLiteral(MgParser.IntegerLiteralContext ctx) {
		int intVal = Integer.parseInt(ctx.PosIntegerConstant().getText());
		IntLiteralExp intLiteralExp = new IntLiteralExp(intVal); intLiteralExp.SetPosition(ctx);
		return intLiteralExp;
	}
	
	@Override
	public Ast visitStringLiteral(MgParser.StringLiteralContext ctx) {
		String str = ctx.StringConstant().getText();
		StringLiteralExp stringLiteralExp = new StringLiteralExp(str); stringLiteralExp.SetPosition(ctx);
		return stringLiteralExp;
	}
	
	@Override
	public Ast visitLogicLiteral(MgParser.LogicLiteralContext ctx) {
		boolean bool = ctx.LogicConstant().getText().equals("true");
		BoolLiteralExp boolLiteralExp = new BoolLiteralExp(bool); boolLiteralExp.SetPosition(ctx);
		return boolLiteralExp;
	}
	
	@Override
	public Ast visitCreatePrimaryExp(MgParser.CreatePrimaryExpContext ctx) {
		return visit(ctx.creator());
	}
	
	@Override
	public Ast visitCreator(MgParser.CreatorContext ctx) {
		CreationExp newExp = new CreationExp(); newExp.SetPosition(ctx);
		// new A, new A(). shit...
		if (ctx.arrayCreatorDim() != null && ctx.arrayCreatorDim().size() != 0) {
			Type arrType = new Type();
			arrType.typeName = ctx.simpleType().getText(); // create base type first.
			// NOTE : for arrayType, the right most dimension is its base dimension, we should create arrayType from right to left.
			for (int i = ctx.arrayCreatorDim().size() - 1; i >= 0; --i) {
				Exp bound = (ctx.arrayCreatorDim(i).exp() != null) ?
								(Exp) visit(ctx.arrayCreatorDim(i).exp()) : null;
				arrType = Type.CreateArrayType(arrType, bound);
			}
			newExp.type = arrType;
		}
		else {
			newExp.type = (Type) visit(ctx.simpleType());
		}
		return newExp;
	}
	
	@Override
	public Ast visitNullExp(MgParser.NullExpContext ctx) {
		NullExp nullExp = new NullExp();
		nullExp.SetPosition(ctx);
		return nullExp;
	}
	
	@Override
	public Ast visitVarPrimaryExp(MgParser.VarPrimaryExpContext ctx) {
		VarExp varExp = new VarExp(); varExp.SetPosition(ctx);
		varExp.name = ctx.Identifier().getText();
		return varExp;
	}
	
	@Override
	public Ast visitMemberSel(MgParser.MemberSelContext ctx) {
		FieldAccessExp fieldAcs = new FieldAccessExp(); fieldAcs.SetPosition(ctx);
		fieldAcs.obj = (Exp) visit(ctx.primaryExp());
		fieldAcs.memberName = ctx.memberName.getText();
		return fieldAcs;
	}
	
	@Override
	public Ast visitMethodSel(MgParser.MethodSelContext ctx) {
		MethodCallExp methodCall = new MethodCallExp(); methodCall.SetPosition(ctx);
		methodCall.obj = (Exp) visit(ctx.primaryExp());
		methodCall.funcName = ctx.Identifier().getText();
		if (ctx.arguments().expList() != null) {
			ctx.arguments().expList().exp().forEach(x -> methodCall.args.add((Exp) visit(x)));
		}
		return methodCall;
	}
	
	@Override
	public Ast visitArrayAcsExp(MgParser.ArrayAcsExpContext ctx) {
		ArrayAccessExp arrAcs = new ArrayAccessExp(); arrAcs.SetPosition(ctx);
		arrAcs.arr = (Exp) visit(ctx.primaryExp());
		arrAcs.subscript = (Exp) visit(ctx.arrayAccessor());
		return arrAcs;
	}
	
	@Override
	public Ast visitFunctCallExp(MgParser.FunctCallExpContext ctx) {
		FunctCallExp functCall = new FunctCallExp(); functCall.SetPosition(ctx);
		functCall.funcName = ctx.Identifier().getText();
		if (ctx.arguments().expList() != null) {
			ctx.arguments().expList().exp().forEach(x -> functCall.args.add((Exp) visit(x)));
		}
		return functCall;
	}
	
	@Override
	public Ast visitStm(MgParser.StmContext ctx) {
		return visit(ctx.getChild(0));
	}
	
	@Override
	public Ast visitExpStm(MgParser.ExpStmContext ctx) {
		ExpStm expStm = new ExpStm(); expStm.SetPosition(ctx);
		expStm.exp = (Exp) visit(ctx.exp());
		return expStm;
	}
	
	@Override
	public Ast visitArrayAccessor(MgParser.ArrayAccessorContext ctx) {
		return visit(ctx.exp());
	}
	
}
