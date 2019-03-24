package ast.node.dec;

import ast.usage.AstBaseVisitor;

import java.util.LinkedList;
import java.util.List;

public class VarDecList extends Dec {
	public List<VarDec> varDecs = new LinkedList<>();
	
	@Override
	public <T> T Accept(AstBaseVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
