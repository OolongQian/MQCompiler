package ast.node.exp;

import ast.node.dec.class_.ClassDec;
import ast.node.dec.function.MethodDec;
import ast.usage.AstBaseVisitor;

public class MethodCallExp extends FunctCallExp {
  public Exp objInstance;
  public ClassDec calledClass;
  public MethodDec calledMethod;

  public MethodCallExp(Exp objInstance, String methodName) {
    super(methodName);
    this.objInstance = objInstance;
  }

  @Override
  public String toString() {
    return objInstance.toString() + "." + functName + arguments.toString();
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
