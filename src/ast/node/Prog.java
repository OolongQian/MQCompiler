package ast.node;

import ast.node.dec.Dec;
import ast.usage.AstBaseVisitor;

import java.util.LinkedList;
import java.util.List;

public class Prog extends Ast {
	public List<Dec> decs = new LinkedList<>();
	
	@Override
	public <T> T Accept(AstBaseVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
