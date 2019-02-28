package ast.util;

import ast.node.dec.variable.VarDecList;
import ast.node.exp.Exp;
import ast.usage.AstBaseVisitor;

import java.util.LinkedList;
import java.util.List;

public class InvokeArgs {
  public List<Exp> args = new LinkedList<>();

  @Override
  public String toString() {
    String selfie = "(";
    if (args.size() > 0) selfie += args.get(0).toString();
    for (int i = 1; i < args.size(); ++i) {
      selfie += ", " + args.get(i).toString();
    }
    selfie += ")";
    return selfie;
  }
}
