package ast.node.exp;

import ast.node.dec.ClassDec;
import ast.node.dec.FunctDec;
import ast.usage.AstBaseVisitor;

public class MethodCallExp extends FunctCallExp {
	
	public Exp obj;
	private ClassDec classResolve;
	
	public ClassDec getClassResolve() {
		assert classResolve != null;
		return classResolve;
	}
	public void setClassResolve(ClassDec classResolve) {
		this.classResolve = classResolve;
	}
	@Override
	public void setFuncResolve(FunctDec funcResolve) {
		assert funcResolve.isMethod();
		super.setFuncResolve(funcResolve);
	}
	
	@Override
	public <T> T Accept(AstBaseVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
