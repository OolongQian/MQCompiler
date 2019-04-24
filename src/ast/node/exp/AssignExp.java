package ast.node.exp;

import ast.usage.AstBaseVisitor;

public class AssignExp extends Exp {
	public Exp dst;
	public Exp src;
	
	@Override
	public <T> T Accept(AstBaseVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
