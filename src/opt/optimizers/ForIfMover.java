package opt.optimizers;

import ast.node.Ast;
import ast.node.Prog;
import ast.node.dec.Dec;
import ast.node.dec.FunctDec;
import ast.node.dec.VarDec;
import ast.node.dec.VarDecList;
import ast.node.exp.*;
import ast.node.stm.*;
import ast.usage.AstBaseVisitor;

import java.util.*;

// movement won't be done when the if condition is 'this' or 'global variable' or 'ambiguous' value.
// oh, this is tolerable, because this is invariant.
public class ForIfMover extends AstBaseVisitor<Void> {

	private Stack<IfStm> mntIfs = new Stack<>();
	
	public void MoveForIf(Prog ast) {
		for (Dec dec : ast.decs) {
			if (dec instanceof FunctDec)
				visit(dec);
		}
	}
	
	// traverse for forStm.
	@Override
	public Void visit(FunctDec node) {
		for (int i = 0; i < node.body.size(); ++i) {
			visit(node.body.get(i));
			if (!mntIfs.isEmpty()) {
				node.body.set(i, mntIfs.pop());
			}
		}
		return null;
	}
	
	// traverse down for block statement.
	@Override
	public Void visit(BlockStm node) {
		for (int i = 0; i < node.stms.size(); ++i) {
			while (true) {
				visit(node.stms.get(i));
				if (!mntIfs.isEmpty()) node.stms.set(i, mntIfs.pop());
				else break;
			}
		}
		return null;
	}
	
	// if get forStm... we check it.
	public Void visit(ForStm for_) {
		while (true) {
			visit(for_.body);
			if (!mntIfs.isEmpty()) for_.body = mntIfs.pop();
			else break;
		}
		
		// want this for node has only one
		IfStm if_ = FormatCheck(for_);
		if (if_ != null) {
			// collect for loop invariant.
			VariantCollect(for_);
			if (CheckCondInvariant(if_.cond, variants)) {
				// rewrite program.
				RewriteIfThen(for_, if_);
				mntIfs.push(if_);
			}
		}
		return null;
	}
	
	@Override
	public Void visit(IfStm node) {
		while (true) {
			visit(node.thenBody);
			if (!mntIfs.isEmpty()) node.thenBody = mntIfs.pop();
			else break;
		}
		
		if (node.elseBody != null) {
			while (true) {
				visit(node.elseBody);
				if (!mntIfs.isEmpty()) node.elseBody = mntIfs.pop();
				else break;
			}
		}
		return null;
	}
	
	@Override
	public Void visit(WhileStm node) {
		while (true) {
			visit(node.body);
			if (!mntIfs.isEmpty()) node.body = mntIfs.pop();
			else break;
		}
		return null;
	}
	
	private void RewriteIfThen(ForStm for_, IfStm if_) {
		for_.body = if_.thenBody;
		if_.thenBody = for_;
	}
	
	// forStm to switch should be simple, thus only contains a single condition statement.
	private IfStm FormatCheck(ForStm node) {
		if (node.body instanceof BlockStm) {
			BlockStm blkStm = (BlockStm) node.body;
			if (blkStm.stms.size() != 1 || !(blkStm.stms.get(0) instanceof IfStm))
				return null;
		}
		else if (!(node.body instanceof IfStm)) {
			return null;
		}
		
		IfStm if_ = (node.body instanceof BlockStm) ?
						(IfStm) ((BlockStm) node.body).stms.get(0) :
						(IfStm) node.body;
		
		if (if_.elseBody != null)
			return null;
		
		return if_;
	}
	
	private boolean CheckCondInvariant(Exp cond, Set<VarDec> variants) {
		CondInvariantChecker invariantChecker = new CondInvariantChecker(cond, variants);
		return invariantChecker.CheckInvariant();
	}
	
	
	// collect all the defined variables in ForStm.
	Set<VarDec> variants;
	private void VariantCollect(ForStm node) {
		List<Ast> asts = new LinkedList<>();
		// variable defined in a forStm is not invariant.
		if (node.initDec != null) {
			asts.addAll(node.initDec.varDecs);
		}
		if (node.initExps != null) {
			asts.addAll(node.initExps);
		}
		// check won't be defined, thus don't care it in variant collect.
		// use visitor paradigm to
		asts.add(node.body);

		VariantCollector variantCollector = new VariantCollector();
		variantCollector.Analyze(asts);
		variants = variantCollector.getVariants();
	}
}

class VariantCollector extends AstBaseVisitor<Void> {
	
	void Analyze(List<Ast> nodes) {
		nodes.forEach(this::visit);
	}
	
	private Set<VarDec> variants = new HashSet<>();
	
	public Set<VarDec> getVariants() {
		return variants;
	}
	
	// only care about AssignExp.
	private boolean lhs = false;
	@Override
	public Void visit(AssignExp node) {
		lhs = true;
		assert node.dst.lValue;
		visit(node.dst);
		lhs = false;
		return null;
	}
	
	// working methods, can be at the left hand side of assign.
	@Override
	public Void visit(PrefixExp node) {
		visit(node.obj);
		return null;
	}
	
	// can only be reached by assign.
	// this node is not
	@Override
	public Void visit(VarExp node) {
		if (lhs)
			variants.add(node.resolve);
		return null;
	}
	
	// note : I didn't do this.
	@Override
	public Void visit(ThisExp node) {
		return null;
	}
	
	@Override
	public Void visit(VarDec node) {
		variants.add(node);
		return null;
	}
	
	// ambiguous stopper.
	@Override
	public Void visit(ArrayAccessExp node) {
		return null;
	}
	
	@Override
	public Void visit(FieldAccessExp node) {
		return null;
	}
	
	// push down methods.
	@Override
	public Void visit(BlockStm node) {
		node.stms.forEach(this::visit);
		return null;
	}
	
	@Override
	public Void visit(IfStm node) {
		// note : bet cond won't be assign.
		visit(node.cond);
		visit(node.thenBody);
		if (node.elseBody != null)
			visit(node.elseBody);
		return null;
	}
	
	@Override
	public Void visit(WhileStm node) {
		if (node.cond != null)
			visit(node.cond);
		visit(node.body);
		return null;
	}
	
	@Override
	public Void visit(ForStm node) {
		if (node.initDec != null)
			visit(node.initDec);
		if (node.initExps != null)
			node.initExps.forEach(this::visit);
		if (node.check != null)
			visit(node.check);
		if (node.updateExps != null)
			node.updateExps.forEach(this::visit);
		
		visit(node.body);
		return null;
	}
	
	@Override
	public Void visit(VarDecList node) {
		node.varDecs.forEach(this::visit);
		return null;
	}
	
	@Override
	public Void visit(LocalVarDecStm node) {
		node.varDecs.forEach(this::visit);
		return null;
	}
	
	@Override
	public Void visit(ExpStm node) {
		visit(node.exp);
		return null;
	}
}

class CondInvariantChecker extends AstBaseVisitor<Void> {
	private boolean invarCond = true;
	
	private Exp cond;
	private Set<VarDec> variants;
	
	CondInvariantChecker(Exp cond, Set<VarDec> variants) {
		this.cond = cond;
		this.variants = variants;
	}
	
	boolean CheckInvariant() {
		visit(cond);
		return invarCond;
	}
	
	@Override
	public Void visit(ArithBinaryExp node) {
		visit(node.lhs);
		visit(node.rhs);
		return null;
	}
	
	@Override
	public Void visit(LogicBinaryExp node) {
		visit(node.lhs);
		visit(node.rhs);
		return null;
	}
	
	@Override
	public Void visit(SuffixExp node) {
		visit(node.obj);
		return null;
	}
	
	@Override
	public Void visit(PrefixExp node) {
		visit(node.obj);
		return null;
	}
	
	@Override
	public Void visit(FieldAccessExp node) {
		invarCond = false;
		return null;
	}
	
	@Override
	public Void visit(ArrayAccessExp node) {
		invarCond = false;
		return null;
	}
	
	// if cannot be assigned.
	@Override
	public Void visit(ThisExp thisExp) {
		return null;
	}
	
	@Override
	public Void visit(AssignExp node) {
		invarCond = false;
		return null;
	}
	
	@Override
	public Void visit(MethodCallExp node) {
		invarCond = false;
		return null;
	}
	
	// we don't worry about function call. since global variable in condition has already been baned.
	@Override
	public Void visit(FunctCallExp node) {
//		invarCond = false;
		return null;
	}
	
	@Override
	public Void visit(CreationExp node) {
		invarCond = false;
		return null;
	}
	
	@Override
	public Void visit(VarExp node) {
		if (variants.contains(node.resolve) || node.resolve.isGlobal() || node.resolve.isField())
			invarCond = false;
		return null;
	}
}