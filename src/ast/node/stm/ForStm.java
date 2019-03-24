package ast.node.stm;


import ast.node.dec.VarDec;
import ast.node.exp.Exp;
import ast.usage.AstBaseVisitor;

import java.util.List;

public class ForStm extends Stm {
	public List<VarDec> initDec;
	public List<Exp> initExps;
	public boolean initIsDec;
	public Exp check;
	public List<Exp> updateExps;
	
	public Stm body;
	
	public void AssginControl(ForControl control) {
		initDec = control.initDec;
		initExps = control.initExps;
		initIsDec = control.initIsDec;
		check = control.check;
		updateExps = control.updateExps;
	}
	
	@Override
	public <T> T Accept(AstBaseVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
