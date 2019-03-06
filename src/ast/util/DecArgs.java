package ast.util;

import ast.node.dec.variable.VarDecList;
import ast.usage.AstBaseVisitor;

public class DecArgs extends VarDecList {
//  public List<VarDec> args = new LinkedList<>();

//  @Override
//  public String toString() {
//    String selfie = "(";
//    if (args.size() > 0) selfie += args.get(0).toString();
//    for (int i = 1; i < args.size(); ++i) {
//      selfie += ", " + args.get(i).toString();
//    }
//    selfie += ")";
//    return selfie;
//  }

  @Override
  public String toString() {
    String selfie = "(";
    if (varDecs.size() > 0) selfie += varDecs.get(0).toString();
    for (int i = 1; i < varDecs.size(); ++i) {
      selfie += ", " + varDecs.get(i).toString();
    }
    selfie += ")";
    return selfie;
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
