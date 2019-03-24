package ast.node.stm;

import ast.node.exp.Exp;
import ast.usage.AstBaseVisitor;

import java.util.LinkedList;
import java.util.List;

public class IfStm extends Stm {
	public Exp cond;
	public Stm thenBody;
	public Stm elseBody;
	
	@Override
	public <T> T Accept(AstBaseVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
