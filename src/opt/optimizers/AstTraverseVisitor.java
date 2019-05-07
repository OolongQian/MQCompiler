package opt.optimizers;

import ast.node.Prog;
import ast.node.dec.*;
import ast.node.exp.*;
import ast.node.stm.*;
import ast.type.Type;
import ast.usage.AstBaseVisitor;
import ir.structure.*;

import java.util.*;

public class AstTraverseVisitor extends AstBaseVisitor<Void> {
	
	public void BreakShortCircuit(Prog ast) {
		visit(ast);
	}
	
	@Override
	public Void visit(Prog node) {
		for (Dec child : node.decs) {
			child.Accept(this);
		}
		return null;
	}
	
	@Override
	public Void visit(VarDecList node) {
		node.varDecs.forEach(x -> x.Accept(this));
		return null;
	}
	
	@Override
	public Void visit(VarDec node) {
		if (node.inital != null) {
			node.inital.Accept(this);
		}
		return null;
	}
	
	@Override
	public Void visit(FunctDec node) {
		for (Stm stm : node.body) {
			stm.Accept(this);
		}
		return null;
	}
	
	@Override
	public Void visit(LocalVarDecStm node) {
		node.varDecs.forEach(x -> x.Accept(this));
		return null;
	}
	
	@Override
	public Void visit(ClassDec node) {
		node.methods.forEach(x -> x.Accept(this));
		return null;
	}
	
	@Override
	public Void visit(VarExp node) {
		return null;
	}
	
	@Override
	public Void visit(SuffixExp node) {
		node.obj.Accept(this);
		return null;
	}
	
	@Override
	public Void visit(PrefixExp node) {
		node.obj.Accept(this);
		return null;
	}
	
	
	@Override
	public Void visit(ArithBinaryExp node) {
		node.lhs.Accept(this);
		node.rhs.Accept(this);
		return null;
	}
	
	@Override
	public Void visit(CreationExp node) {
		return null;
	}
	
	@Override
	public Void visit(FieldAccessExp node) {
		node.obj.Accept(this);
		return null;
	}
	
	
	@Override
	public Void visit(ArrayAccessExp node) {
		node.arr.Accept(this);
		node.subscript.Accept(this);
		return null;
	}
	
	
	@Override
	public Void visit(AssignExp node) {
		node.dst.Accept(this);
		node.src.Accept(this);
		return null;
	}
	
	@Override
	public Void visit(FunctCallExp node) {
		List<IrValue> args = new LinkedList<>();
		for (Exp arg : node.args) {
			arg.Accept(this);
		}
		return null;
	}
	
	@Override
	public Void visit(MethodCallExp node) {
		node.obj.Accept(this);
		return null;
	}
	
	
	@Override
	public Void visit(ReturnStm node) {
		if (node.ret != null)
			node.ret.Accept(this);
		return null;
	}
	
	@Override
	public Void visit(ThisExp node) {
		return null;
	}
	
	@Override
	public Void visit(LogicBinaryExp node) {
		return null;
	}
	
	
	@Override
	public Void visit(IfStm node) {
		node.cond.Accept(this);
		node.thenBody.Accept(this);
		if (node.elseBody != null) {
			node.elseBody.Accept(this);
		}
		return null;
	}
	
	
	
	@Override
	public Void visit(WhileStm node) {
		if (node.cond instanceof LogicBinaryExp) {
			node.cond.Accept(this);
		}
		node.body.Accept(this);
		return null;
	}
	
	@Override
	public Void visit(ForStm node) {
		if (node.initIsDec) {
			if (node.initDec != null) node.initDec.Accept(this);
		} else {
			if (node.initExps != null) node.initExps.forEach(x -> x.Accept(this));
		}
		if (node.check != null) {
			node.check.Accept(this);
		}
		node.body.Accept(this);
		node.updateExps.forEach(x -> x.Accept(this));
		return null;
	}
	@Override
	public Void visit(ContinueStm node) {
		return null;
	}
	
	@Override
	public Void visit(BreakStm node) {
		return null;
	}
	
	
	/********************** Primitives and Literals *********************/
	@Override
	public Void visit(IntLiteralExp node) {
		return null;
	}
	
	@Override
	public Void visit(BoolLiteralExp node) {
		return null;
	}
	
	@Override
	public Void visit(StringLiteralExp node) {
		return null;
	}
	
	@Override
	public Void visit(NullExp node) {
		return null;
	}
	
	
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
	
	
}
