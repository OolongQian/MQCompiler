package ast.node.exp;

import ast.usage.AstBaseVisitor;
import ast.util.InvokeArgs;

import java.util.LinkedList;
import java.util.List;

public class FunctCallExp extends Exp {
  public String functName;
  public InvokeArgs arguments = new InvokeArgs();

  public FunctCallExp(String functName) {
    this.functName = functName;
  }

  public FunctCallExp(String functName, List<Exp> arguments) {
    this.functName = functName;
    this.arguments.args = arguments;
  }

  @Override
  public String toString() {
    return functName + arguments.toString();
  }

  @Override
  public void Accept(AstBaseVisitor visitor) {
    visitor.visit(this);
  }
}
