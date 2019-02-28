package ast.node.dec.variable;

import ast.node.dec.Dec;
import ast.usage.AstBaseVisitor;

import java.util.LinkedList;
import java.util.List;

public class VarDecList extends Dec {
  public List<VarDec> varDecs = new LinkedList<>();

  public VarDecList() {}
  public VarDecList(VarDecList other) {
    varDecs = other.varDecs;
  }

  /**
   * Search for a variable from Variable Declaration List.
   * Return null if the declaration cannot be found.
   * */
  public VarDec GetVarDec(String varName) {
    for (int i = 0; i < varDecs.size(); ++i) {
      if (varDecs.get(i).varName.equals(varName))
        return varDecs.get(i);
    }
    return null;
  }

  @Override
  public String toString() {
    String selfie = varDecs.get(0).toString();
    for (int i = 1; i < varDecs.size(); ++i) {
      selfie += ", " + varDecs.get(i).varName;
      if (varDecs.get(i).inital != null)
        selfie += " = " + varDecs.get(i).inital.toString();
    }
    return selfie;
  }

  @Override
  public void Accept(AstBaseVisitor visitor) {
    visitor.visit(this);
  }
}
