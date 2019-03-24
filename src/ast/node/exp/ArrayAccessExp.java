package ast.node.exp;

import ast.usage.AstBaseVisitor;

public class ArrayAccessExp extends Exp  {
	public Exp arr;
	public Exp subscript;
	@Override
	public <T> T Accept(AstBaseVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
