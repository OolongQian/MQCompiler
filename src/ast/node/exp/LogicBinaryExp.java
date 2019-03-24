package ast.node.exp;


import ast.usage.AstBaseVisitor;

public class LogicBinaryExp extends BinaryExp {
	@Override
	public <T> T Accept(AstBaseVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
