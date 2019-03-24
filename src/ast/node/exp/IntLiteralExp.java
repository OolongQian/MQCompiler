package ast.node.exp;

import ast.type.Type;
import ast.usage.AstBaseVisitor;

public class IntLiteralExp extends LiteralExp {
	public int val;
	
	public IntLiteralExp(int val) {
		this.val = val;
		this.type = Type.CreateBaseType("int");
	}
	
	@Override
	public <T> T Accept(AstBaseVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
