package ast.node.dec.function;

import ast.node.dec.Dec;
import ast.node.stm.Stm;
import ast.typeref.VarTypeRef;
import ast.usage.AstBaseVisitor;
import ast.util.DecArgs;

import java.util.LinkedList;
import java.util.List;

public class FunctDec extends Dec {
  public String functName;
  public VarTypeRef returnType;
  public DecArgs arguments = new DecArgs();
  public List<Stm> functBody = new LinkedList<>();
  public boolean builtIn = false;

  public FunctDec() {}

  public FunctDec(FunctDec other) {
    beginRow = other.beginRow;
    endRow = other.endRow;
    beginCol = other.beginCol;
    endCol = other.endCol;
    functName = other.functName;
    returnType = other.returnType;
    arguments = other.arguments;
    functBody = other.functBody;
  }

  public String GetFunctName() {
    return functName;
  }

  public void MarkBuiltIn() {
    builtIn = true;
  }
  @Override
  protected String SelfDeclare() {
    return "FunctDec: \n";
  }

  @Override
  public String PrettyPrint() {
    String selfie = returnType.toString() + " " + functName + arguments.toString() + "\n";
    selfie += TabShift("{\n");
    ++tab;
    for (int i = 0; i < functBody.size(); ++i) {
      selfie += functBody.get(i).PrettyPrint();
    }
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
