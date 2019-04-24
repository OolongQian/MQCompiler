package ast.node.exp;

import ast.type.Type;
import ast.usage.AstBaseVisitor;

public class StringLiteralExp extends LiteralExp {
	public String val;
	
	public StringLiteralExp(String val) {
		this.val = val;
		this.type = Type.CreateBaseType("string");
	}
	
	@Override
	public <T> T Accept(AstBaseVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
