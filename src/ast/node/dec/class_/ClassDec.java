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

import static ast.typeref.VarTypeRef.CreatePrimitiveType;

public class ClassDec extends Dec {
  public VarTypeRef classType;
  // NOTE : fieldList maintains format info, fields smash them off.
  public List<FieldDec> fieldList = new LinkedList<>();
  public List<VarDec> fields = new LinkedList<>();
  public List<MethodDec> method = new LinkedList<>();
  public ConstructDec constructor = null;
  public boolean builtIn = false;

  /**
   * Constructor
   * */
  public ClassDec(String className) {
    classType = CreatePrimitiveType(className);
    classType.SetBaseType(this);
  }

  /**
   * Getter and setter.
   *********************************************************************/
  public String GetClassName () {
    return classType.typeName;
  }

  public void MarkBuiltIn() {
    builtIn = true;
    method.forEach(x -> x.builtIn = true);
  }

  public void AddFieldList(FieldDec fList) {
    fieldList.add(fList);
    fields.addAll(fList.varDecs);
  }

  /**
   * Find method
   * Search for Field Declaration from a literal variable name string.
   ***********************************************************************/
  public VarDec FindField(String fieldName) {
    VarDec tmp;
    for(int i = 0; i < fields.size(); ++i) {
      tmp = fields.get(i);
      if (tmp.varName.equals(fieldName))
        return tmp;
    }
    return null;
  }

  public MethodDec FindMethod(String methodName) {
    for (int i = 0; i < method.size(); ++i) {
      if (method.get(i).functName.equals(methodName))
        return method.get(i);
    }
    return null;
  }


  /** utility
   * *****************************************************************/
  @Override
  protected String SelfDeclare() {
    return "ClassDec: \n";
  }

  @Override
  public String PrettyPrint() {
    return null;
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
