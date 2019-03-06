package ast.typeref;

import ast.usage.AstBaseVisitor;

public class VarTypeRef extends TypeRef {

  /**
   * typeThread is filled during type resolution.
   * */

  public VarTypeRef(String typeName) {
    this.typeName = typeName;
  }

  public VarTypeRef() { }

  public boolean isInt() {
    return getClass() == VarTypeRef.class && typeName.equals("int");
  }

  public boolean isBool() {
    return getClass() == VarTypeRef.class && typeName.equals("bool");
  }

  public  boolean isString() {
    return getClass() == VarTypeRef.class && typeName.equals("string");
  }

  public  boolean isVoid() {
    return getClass() == VarTypeRef.class && typeName.equals("void");
  }

  public  boolean isNull() {
    return getClass() == VarTypeRef.class && typeName.equals("null");
  }

  @Override
  public String toString() {
    return typeName;
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }

  /**
   * Determine whether two simple varTypeRef equals, arrayTypeRef has inherited Equal method.
   * */
  public boolean Equal(VarTypeRef varTypeRef) {
    return varTypeRef.getClass() == VarTypeRef.class && typeName.equals(varTypeRef.typeName);
  }

  /**
   * Get new VarTypeRef after current array type being accessed per dimension.
   * Meant to be inherited by ArrayTypeRef, throw exception if this method is invoked in this base type.
   * */
  public VarTypeRef GetAccessedTypeRef () throws RuntimeException {
    throw new RuntimeException();
  }
}
