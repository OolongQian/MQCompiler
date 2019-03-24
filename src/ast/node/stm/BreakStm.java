package ast.node.stm;


import ast.usage.AstBaseVisitor;

public class BreakStm extends Stm {
	@Override
	public <T> T Accept(AstBaseVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
