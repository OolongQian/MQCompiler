package ast.node.stm;

import ast.node.exp.Exp;
import ast.type.Type;
import ast.usage.AstBaseVisitor;

public class ReturnStm extends Stm {
	public Type retType;
	public Exp ret;
	
	@Override
	public <T> T Accept(AstBaseVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
