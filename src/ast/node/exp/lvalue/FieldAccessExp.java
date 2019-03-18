package ast.node.exp.lvalue;

import ast.node.dec.class_.ClassDec;
import ast.node.dec.variable.FieldDec;
import ast.node.dec.variable.VarDec;
import ast.node.exp.Exp;
import ast.typeref.VarTypeRef;
import ast.usage.AstBaseVisitor;

public class FieldAccessExp extends Exp {
  public Exp objInstance;
  public String memberName;

  public ClassDec objClassDec;
  public VarDec fieldDec;

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
