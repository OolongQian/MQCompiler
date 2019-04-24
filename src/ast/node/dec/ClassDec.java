package ast.node.dec;

import ast.usage.AstBaseVisitor;

import java.util.LinkedList;
import java.util.List;

import static config.Config.INT_SIZE;

public class ClassDec extends Dec {
	public String name;
	public List<VarDec> fields = new LinkedList<>();
	public List<FunctDec> methods = new LinkedList<>();
	public FunctDec ctor = null;
	
	public boolean builtIn = false;
	
	public void MarkBuiltIn() {
		builtIn = true;
		methods.forEach(x -> x.builtIn = true);
	}
	
	public ClassDec(String name) {
		this.name = name;
	}
	
	public VarDec FindField(String fieldName) {
		for (VarDec field : fields) {
			if (field.name.equals(fieldName))
				return field;
		}
		return null;
	}
	
	public FunctDec FindMethod(String methodName) {
		for (FunctDec method : methods) {
			if (method.name.equals(methodName))
				return method;
		}
		return null;
	}
	
	public int GetFieldOffset(String fieldName) {
		int offset = 0;
		for (int i = 0; i < fields.size(); ++i) {
			if (fields.get(i).name.equals(fieldName)) break;
			offset += INT_SIZE;
		}
		return offset;
	}
	
	@Override
	public <T> T Accept(AstBaseVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
