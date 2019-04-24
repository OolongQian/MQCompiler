package ast.node.stm;
import ast.node.dec.VarDecList;
import ast.node.exp.Exp;
import ast.usage.AstBaseVisitor;
import java.util.LinkedList;
import java.util.List;

public class ForControl extends Stm {
	public VarDecList initDec;
	public List<Exp> initExps = new LinkedList<>();
	public boolean initIsDec;
	public Exp check;
	public List<Exp> updateExps = new LinkedList<>();
	
	@Override
	public <T> T Accept(AstBaseVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
