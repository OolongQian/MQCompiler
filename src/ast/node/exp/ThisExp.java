package ast.node.exp;

import ast.node.dec.FunctDec;
import ast.usage.AstBaseVisitor;

public class ThisExp extends Exp {
	private FunctDec thisMethod;
	
	public FunctDec GetThisMethod() {
		assert thisMethod != null;
		return thisMethod;
	}
	public void SetThisMethod(FunctDec method) {
		assert method.isMethod();
		this.thisMethod = method;
	}
	
	@Override
	public <T> T Accept(AstBaseVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
