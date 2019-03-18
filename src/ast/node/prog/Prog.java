package ast.node.prog;

import ast.node.Ast;
import ast.node.dec.Dec;
import ast.node.dec.class_.ClassDec;
import ast.node.dec.function.FunctDec;
import ast.usage.AstBaseVisitor;
import semantic.symbol_table.Symbol;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class Prog extends Ast {
  public List<Dec> children = new LinkedList<>();

  public void AddDec(Dec dec) {
    children.add(dec);
  }

  private Hashtable<Symbol, ClassDec> classTable = new Hashtable<>();

  private Hashtable<Symbol, FunctDec> functTable = new Hashtable<>();

  @Override
  protected String SelfDeclare() {
    return "Prog: \n";
  }

  @Override
  public String toString() {
    String selfie = "";
    for (int i = 0; i < children.size(); ++i)
      selfie += children.get(i).PrettyPrint();
    return selfie;
  }

  @Override
  public String PrettyPrint() {
    return Formatter(this, this.toString()) + "\n";
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
