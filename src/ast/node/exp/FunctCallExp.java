package ast.node.exp;

import ast.node.dec.function.FunctDec;
import ast.node.dec.function.MethodDec;
import ast.usage.AstBaseVisitor;
import ast.util.InvokeArgs;

import java.util.LinkedList;
import java.util.List;

public class FunctCallExp extends Exp {
  public String functName;
  public InvokeArgs arguments = new InvokeArgs();

  /**
   * This can be FunctDec or MethodDec
   * */
  public FunctDec functDec;

  public FunctCallExp(String functName) {
    this.functName = functName;
  }

  public FunctCallExp(String functName, List<Exp> arguments) {
    this.functName = functName;
    this.arguments.args = arguments;
  }

  public boolean isMethodCall() {
    return functDec instanceof MethodDec;
  }

  @Override
  public String toString() {
    return functName + arguments.toString();
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
