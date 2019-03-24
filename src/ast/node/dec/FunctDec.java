package ast.node.dec;

import ast.node.stm.Stm;
import ast.type.Type;
import ast.usage.AstBaseVisitor;

import java.util.LinkedList;
import java.util.List;

public class FunctDec extends Dec {
	public String name;
	public Type retType;
	public List<VarDec> args = new LinkedList<>();
	public List<Stm> body = new LinkedList<>();
	
	public FunctDec() { }
	public FunctDec(String name) {
		this.name = name;
	}
	
	private boolean mtdMark;
	private boolean ctorMark;
	public void setMethod() {
		mtdMark = true;
	}
	public void setCtor() {
		ctorMark = true;
	}
	public boolean isMethod() {
		return mtdMark;
	}
	public boolean isCtor() {
		return ctorMark;
	}
	
	public boolean builtIn = false;
	public void MarkBuiltIn() {
		builtIn = true;
	}
	
	private ClassDec classResolve;
	public void SetParentClass(ClassDec parentClass) {
		assert isMethod();
		this.classResolve = parentClass;
	}
	public ClassDec GetParentClass() {
		assert isMethod() && classResolve != null;
		return classResolve;
	}
	
	
	
	@Override
	public <T> T Accept(AstBaseVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
