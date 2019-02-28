package ast.typeref;

import ast.node.exp.Exp;
import ast.usage.AstBaseVisitor;

import java.util.LinkedList;
import java.util.List;

public class ArrayTypeRef extends VarTypeRef {
  public int dim;
  public List<Exp> dimBound;

  public void SetBound(int i, Exp bound) {
    dimBound.set(i, bound);
  }

  public ArrayTypeRef(String arrayTypeName, int dim) {
    super(arrayTypeName);
    this.dim = dim;
    this.dimBound = new LinkedList<>();
    for(int i = 0; i < dim; ++i)
      dimBound.add(null);
  }

  public ArrayTypeRef() {
    dim = 0;
    dimBound = new LinkedList<>();
  }

  @Override
  public String toString() {
    String selfie = typeName;
    for (int i = 0; i < dim; ++i) {
      if (dimBound.get(i) != null)
        selfie += "[" + dimBound.get(i).toString() + "]";
      else
        selfie += "[]";
    }
    return selfie;
  }

  public boolean Equal (VarTypeRef varTypeRef) {
    return varTypeRef.getClass() == ArrayTypeRef.class && typeName.equals(varTypeRef.typeName) && dim == ((ArrayTypeRef) varTypeRef).dim;
  }

  @Override
  public void Accept(AstBaseVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public VarTypeRef GetAccessedTypeRef() throws RuntimeException {
    if (dim == 1) return new VarTypeRef(typeName);
    else return new ArrayTypeRef(typeName, dim-1);
  }
}

