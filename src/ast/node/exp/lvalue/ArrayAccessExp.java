package ast.node.exp.lvalue;

import ast.node.exp.Exp;
import ast.usage.AstBaseVisitor;

public class ArrayAccessExp extends LValueExp {
  public Exp arrInstance;
  public Exp accessor;

  public ArrayAccessExp(Exp arrInstance, Exp accessor) {
    this.arrInstance = arrInstance;
    this.accessor = accessor;
  }

  @Override
  public String toString() {
    return arrInstance.toString() + '[' + accessor.toString() + ']';
  }

  @Override
  public void Accept(AstBaseVisitor visitor) {
    visitor.visit(this);
  }


}
