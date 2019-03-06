package ast.node.dec.class_;

import ast.node.dec.Dec;
import ast.node.dec.function.ConstructDec;
import ast.node.dec.function.MethodDec;
import ast.node.dec.variable.FieldDec;
import ast.node.dec.variable.VarDec;
import ast.typeref.VarTypeRef;
import ast.usage.AstBaseVisitor;

import java.util.LinkedList;
import java.util.List;

public class ClassDec extends Dec {
  public VarTypeRef classTypeName = new VarTypeRef();
  public List<FieldDec> field = new LinkedList<>();
  public List<MethodDec> method = new LinkedList<>();
  public ConstructDec constructor = null;

  public ClassDec(String className) {
    classTypeName.typeName = className;
  }

  /**
   * Search for Field Declaration from a literal variable name string.
   * */
  public VarDec FindField(String fieldName) {
    VarDec temp;
    for (int i = 0; i < field.size(); ++i) {
      if ( (temp = field.get(i).GetVarDec(fieldName)) != null)
        return temp;
    }
    return null;
  }

  public MethodDec FindMethod(String methodName) {
    for (int i = 0; i < method.size(); ++i) {
      if (method.get(i).functType.typeName.equals(methodName))
        return method.get(i);
    }
    return null;
  }

  @Override
  protected String SelfDeclare() {
    return "ClassDec: \n";
  }

  @Override
  public String PrettyPrint() {
    String selfie = "class " + classTypeName.toString() + "\n" + TabShift("{\n");
    ++tab;
    for (int i = 0; i < field.size(); ++i)
      selfie += field.get(i).PrettyPrint();
    for (int i = 0; i < method.size(); ++i)
      selfie += method.get(i).PrettyPrint();
    if (constructor != null)
      constructor.PrettyPrint();

    --tab;
    selfie += TabShift("}\n");
    return Formatter(this, selfie);
  }

  @Override
  public String toString() {
    return this.PrettyPrint();
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
