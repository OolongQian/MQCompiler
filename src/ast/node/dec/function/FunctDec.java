package ast.node.dec.function;

import ast.node.dec.Dec;
import ast.node.stm.Stm;
import ast.typeref.FunctTypeRef;
import ast.typeref.VarTypeRef;
import ast.usage.AstBaseVisitor;
import ast.util.DecArgs;

import java.util.LinkedList;
import java.util.List;

public class FunctDec extends Dec {
  public FunctTypeRef functType = new FunctTypeRef();
  public VarTypeRef returnType = new VarTypeRef();
  public DecArgs arguments = new DecArgs();
  public List<Stm> functBody = new LinkedList<>();

  public FunctDec() {}


  public FunctDec(FunctDec other) {
    beginRow = other.beginRow;
    endRow = other.endRow;
    beginCol = other.beginCol;
    endCol = other.endCol;
    functType = other.functType;
    returnType = other.returnType;
    arguments = other.arguments;
    functBody = other.functBody;
  }

  @Override
  protected String SelfDeclare() {
    return "FunctDec: \n";
  }

  @Override
  public String PrettyPrint() {
    String selfie = returnType.toString() + " " + functType.toString() + arguments.toString() + "\n";
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
  public void Accept(AstBaseVisitor visitor) {
    visitor.visit(this);
  }
}
