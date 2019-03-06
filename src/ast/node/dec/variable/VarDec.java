package ast.node.dec.variable;

import ast.node.dec.Dec;
import ast.node.exp.Exp;
import ast.typeref.VarTypeRef;
import ast.usage.AstBaseVisitor;

public class VarDec extends Dec {
  // wait to be constructed, unclear whether is simple or array.
  public VarTypeRef varType;
  public String varName;
  public Exp inital;

  public String hintName;

  public VarDec() { }

  public VarDec(VarTypeRef varType, String varName) {
    this.varType = varType;
    this.varName = varName;
  }

  public VarDec(VarTypeRef varType, String varName, Exp inital) {
    this.varType = varType;
    this.varName = varName;
    this.inital = inital;
  }

  @Override
  public String toString() {
    String selfie = varType.toString() + " " + varName;
    if (inital != null) selfie += " = " + inital.toString();
    return selfie;
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }

}
