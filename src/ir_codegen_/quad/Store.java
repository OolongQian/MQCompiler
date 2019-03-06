package ir_codegen.quad;

import ir_codegen.usage.IRBaseVisitor;

import static ir_codegen.util.Info.tempAddressTypeName;
import static ir_codegen.util.Info.tempTypeName;

public class Store extends Quad {
  public Value value;
  public Var address;

  public Store(Value value, Var address) {
    this.value = value;
    this.address = address;
  }

  @Override
  public String toString() {
    return "store " + tempTypeName + " " + value.GetValueName() + ", "
        + tempAddressTypeName + " " + address.GetValueName() + ", "
        + "align " + address.getMemoerySpace() + "\n";
  }

  @Override
  public <T> T Accept(IRBaseVisitor<T> irBaseVisitor) {
    return irBaseVisitor.visit(this);
  }
}
