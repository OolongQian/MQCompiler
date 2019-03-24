package ast.node.exp;

import ast.node.dec.ClassDec;
import ast.node.dec.VarDec;
import ast.usage.AstBaseVisitor;

public class FieldAccessExp extends Exp {
	public Exp obj;
	public String memberName;
	
	private ClassDec classResolve;
	private VarDec fieldResolve;
	
	public ClassDec getClassResolve() {
		assert classResolve != null;
		return classResolve;
	}
	public void setClassResolve(ClassDec classResolve) {
		this.classResolve = classResolve;
	}
	public VarDec getFieldResolve() {
		assert fieldResolve != null;
		return fieldResolve;
	}
	public void setFieldResolve(VarDec fieldResolve) {
		assert fieldResolve.isField();
		this.fieldResolve = fieldResolve;
	}
	
	@Override
	public <T> T Accept(AstBaseVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
