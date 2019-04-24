package ast.node.stm;


import ast.node.dec.VarDec;
import ast.usage.AstBaseVisitor;

import java.util.List;

public class LocalVarDecStm extends Stm {
	public List<VarDec> varDecs;
	
	public LocalVarDecStm(List<VarDec> varDecs) {
		this.varDecs = varDecs;
	}
	
	@Override
	public <T> T Accept(AstBaseVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
