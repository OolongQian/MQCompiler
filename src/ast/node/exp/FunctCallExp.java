package ast.node.exp;
import ast.node.dec.FunctDec;
import ast.usage.AstBaseVisitor;

import java.util.LinkedList;
import java.util.List;

public class FunctCallExp extends Exp {
	public String funcName;
	// modified funcName, as functTable key.
	public String funcTableKey;
	private FunctDec funcResolve;
	public List<Exp> args = new LinkedList<>();
	
	public boolean isCallMethod() {
		assert funcResolve != null;
		return funcResolve.isMethod();
	}
	
	public FunctDec getFuncResolve() {
		assert funcResolve != null;
		return funcResolve;
	}
	public void setFuncResolve(FunctDec funcResolve) {
		this.funcResolve = funcResolve;
	}
	
	@Override
	public <T> T Accept(AstBaseVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
