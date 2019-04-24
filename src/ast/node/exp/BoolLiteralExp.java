package ast.node.exp;

import ast.type.Type;
import ast.usage.AstBaseVisitor;

public class BoolLiteralExp extends LiteralExp {
	public boolean val;
	
	public BoolLiteralExp(boolean val) {
		this.val = val;
		this.type = Type.CreateBaseType("bool");
	}
	
	@Override
	public <T> T Accept(AstBaseVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
