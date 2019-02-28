package ast.node.exp;

import ast.usage.AstBaseVisitor;

public class MethodCallExp extends FunctCallExp {
  public Exp objInstance;

  public MethodCallExp(Exp objInstance, String methodName) {
    super(methodName);
    this.objInstance = objInstance;
  }

  @Override
  public String toString() {
    return objInstance.toString() + "." + functName + arguments.toString();
  }

  @Override
  public void Accept(AstBaseVisitor visitor) {
    visitor.visit(this);
  }
}
