package ast.node.dec;

import ast.usage.AstBaseVisitor;

import java.util.LinkedList;
import java.util.List;

/**
 * Every variable declaration is wrapped with this VarDecList as its data structure.
 * It is used in Global, Field, Local, and ForInit.
 * */
public class VarDecList extends Dec {
	public List<VarDec> varDecs = new LinkedList<>();
	
	@Override
	public <T> T Accept(AstBaseVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
