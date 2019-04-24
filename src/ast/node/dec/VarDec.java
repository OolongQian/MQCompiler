package ast.node.dec;

import ast.node.exp.Exp;
import ast.type.Type;
import ast.usage.AstBaseVisitor;

import java.util.List;

public class VarDec extends Dec {
	public Type type;
	public String name;
	public Exp inital;
	
	private int mode;
	public void setGlobal() {
		mode = 1;
	}
	public void setField() {
		mode = 2;
	}
	public boolean isGlobal() {
		return mode == 1;
	}
	public boolean isField() {
		return mode == 2;
	}
	public void CheckModeSet() {
		assert mode == 1 || mode == 2;
	}
	
	public VarDec(String name) {
		this.name = name;
	}
	public VarDec(Type type, String name) {
		this.type = type;
		this.name = name;
		mode = 0;
		inital = null;
	}
	
	private ClassDec parentClass;
	public void SetParentClass(ClassDec parentClass) {
		assert isField();
		this.parentClass = parentClass;
	}
	public ClassDec GetParentClass() {
		assert isField() && parentClass != null;
		return parentClass;
	}
	public <T> T Accept(AstBaseVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
