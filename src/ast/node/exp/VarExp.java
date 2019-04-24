package ast.node.exp;

import ast.node.dec.VarDec;
import ast.usage.AstBaseVisitor;

public class VarExp extends Exp {
	public String name;
	public VarDec resolve;
	
	@Override
	public <T> T Accept(AstBaseVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
