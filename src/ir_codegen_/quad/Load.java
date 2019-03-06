package ir_codegen.quad;

import ir_codegen.usage.IRBaseVisitor;
import ir_codegen.util.HintName;

import static ir_codegen.util.Info.tempAddressTypeName;
import static ir_codegen.util.Info.tempTypeName;

public class Load extends Quad implements Value {
  public Virtual loaded;
  public Var phyVar;

  public Load(Virtual loaded, Var phyVar) {
    this.loaded = loaded;
    this.phyVar = phyVar;
  }

  @Override
  public HintName GetValueName() {
    return loaded.GetValueName();
  }

  @Override
  public String toString() {
    return loaded.hintNameIR.hintName + " = " + "load " + tempTypeName + ", " +
        tempAddressTypeName + " " + phyVar.hintNameIR.hintName + ", " +
        "align " + phyVar.getMemoerySpace() + "\n";
  }

  @Override
  public <T> T Accept(IRBaseVisitor<T> irBaseVisitor) {
    return irBaseVisitor.visit(this);
  }
}
