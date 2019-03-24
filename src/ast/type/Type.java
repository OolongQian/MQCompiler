package ast.type;

import ast.node.Ast;
import ast.node.dec.ClassDec;
import ast.node.exp.Exp;
import ast.usage.AstBaseVisitor;

public class Type extends Ast {
	public Type innerType;
	public String typeName; // int*
	public Exp dim;
	
	
	public static Type CreateBaseType(String name) {
		return new Type(name);
	}
	public static Type CreateArrayType(Type baseType, Exp bound) {
		Type arrType = new Type();
		arrType.innerType = baseType;
		arrType.dim = bound;
		arrType.typeName = baseType.typeName + "*";
		return arrType;
	}
	public boolean isNull() {
		return !isArray() && typeName.equals("null");
	}
	public boolean isInt() {
		return !isArray() && typeName.equals("int");
	}
	public boolean isBool() {
		return !isArray() && typeName.equals("bool");
	}
	public boolean isString() {
		return !isArray() && typeName.equals("string");
	}
	public boolean isVoid() {
		return !isArray() && typeName.equals("void");
	}
	public boolean isArray() {
		return innerType != null;
	}
	
	public Type() { }
	public Type(String typeName) {
		this.typeName = typeName;
	}
	
	public String GetBaseTypeName() {
		Type cur = this;
		while (cur.innerType != null) cur = cur.innerType;
		return cur.typeName;
	}
	
	
	public boolean Equal(Type rhs) {
		return typeName.equals(rhs.typeName);
	}
	public boolean Assignable(Type rhs) {
		if (rhs.isNull()) {
			return !(isInt() || isBool() || isString());
		}
		else {
			return Equal(rhs);
		}
	}
	public static boolean Returnable(Type lhs, Type rhs) {
		if (lhs.isVoid()) {
			return rhs == null;
		}
		else {
			return rhs != null && lhs.Assignable(rhs);
		}
	}
	
	@Override
	public <T> T Accept(AstBaseVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
