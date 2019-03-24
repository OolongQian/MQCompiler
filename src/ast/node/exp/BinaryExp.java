package ast.node.exp;

abstract public class BinaryExp extends Exp {
	public Exp lhs;
	public Exp rhs;
	public String op;
}
