package opt.optimizers;

import ast.node.Prog;
import ast.node.dec.Dec;
import ast.node.dec.FunctDec;
import ast.node.stm.BlockStm;
import ast.node.stm.ForStm;
import ast.node.stm.Stm;
import ast.usage.AstBaseVisitor;

import java.util.List;

public class ForIfMover extends AstBaseVisitor<Void> {
	
	public void MoveForIf(Prog ast) {
		for (Dec dec : ast.decs) {
			if (dec instanceof FunctDec)
				visit(dec);
		}
	}
	
	// traverse for forStm.
	@Override
	public Void visit(FunctDec node) {
		List<Stm> body = node.body;
		body.forEach(this::visit);
		return null;
	}
	
	// traverse down for block statement.
	@Override
	public Void visit(BlockStm node) {
		node.stms.forEach(this::visit);
		return null;
	}
	
	// if get forStm... we check it.
	@Override
	public Void visit(ForStm node) {
		// want this for node has only one
//		FormatCheck(node);
		return null;
	}
}
