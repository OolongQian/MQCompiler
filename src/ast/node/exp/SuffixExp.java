package ast.node.exp;

import ast.usage.AstBaseVisitor;

public class SuffixExp extends Exp {
	public Exp obj;
	public String op;
	
	@Override
	public <T> T Accept(AstBaseVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
