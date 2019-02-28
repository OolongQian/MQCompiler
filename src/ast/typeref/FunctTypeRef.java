package ast.typeref;

import ast.usage.AstBaseVisitor;

/**
 * This class is nothing but a function's name.
 * */
public class FunctTypeRef extends TypeRef {
  @Override
  public String toString() {
    return typeName;
  }

  @Override
  public void Accept(AstBaseVisitor visitor) {
    visitor.visit(this);
  }
}
