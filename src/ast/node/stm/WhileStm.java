package ast.node.stm;


import ast.node.exp.Exp;
import ast.usage.AstBaseVisitor;

/**
 * Condition can't be null.
 * */
public class WhileStm extends Stm {
	public Exp cond;
	public Stm body;
	
	@Override
	public <T> T Accept(AstBaseVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
