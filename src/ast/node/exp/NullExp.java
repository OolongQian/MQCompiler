package ast.node.exp;

import ast.type.Type;
import ast.usage.AstBaseVisitor;

public class NullExp extends Exp {
	
	public NullExp() {
		type = Type.CreateBaseType("null");
	}
	
	@Override
	public <T> T Accept(AstBaseVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
