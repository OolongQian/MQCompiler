package ast.node.exp.binary;

import ast.node.exp.Exp;

abstract public class BinaryExp extends Exp {

  public Exp lhs;
  public Exp rhs;
  public String op;

  public BinaryExp(Exp left, Exp right, String op) {
    lhs = left;
    rhs = right;
    this.op = op;
  }

  @Override
  public String toString() {
    return lhs.toString() + " " + op + " " + rhs.toString();
  }
}
