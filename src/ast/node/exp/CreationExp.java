package ast.node.exp;

import ast.usage.AstBaseVisitor;

public class CreationExp extends Exp {
	@Override
	public <T> T Accept(AstBaseVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
