package ast.node.stm;

import ast.node.exp.Exp;
import ast.typeref.VarTypeRef;
import ast.usage.AstBaseVisitor;

public class ReturnStm extends Stm {
  public Exp retVal;
  public VarTypeRef retType;

  public ReturnStm() {
  }

  public ReturnStm(Exp value) {
    retVal = value;
  }

  @Override
  protected String SelfDeclare() {
    return "ReturnStm: \n";
  }

  @Override
  public String toString() {
    if (retVal != null)
      return "return " + retVal.toString() + ";";
    else
      return "return ;";
  }

  @Override
  public String PrettyPrint() {
    return Formatter(this, toString() + "\n");
  }

  @Override
  public void Accept(AstBaseVisitor visitor) {
    visitor.visit(this);
  }
}
