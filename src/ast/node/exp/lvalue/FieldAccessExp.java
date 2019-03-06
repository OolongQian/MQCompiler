package ast.node.exp.lvalue;

import ast.node.exp.Exp;
import ast.usage.AstBaseVisitor;

public class FieldAccessExp extends LValueExp {
  public Exp objInstance;
  public String memberName;

  public FieldAccessExp(Exp objInstance, String memberName) {
    this.objInstance = objInstance;
    this.memberName = memberName;
  }

  @Override
  public String toString() {
    return objInstance.toString() + "." + memberName;
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }

}
