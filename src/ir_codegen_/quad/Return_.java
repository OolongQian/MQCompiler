package ir_codegen.quad;

import ir_codegen.usage.IRBaseVisitor;

import static ir_codegen.util.Info.tempTypeName;

public class Return_ extends Quad {

  public Value retVal;

  public Return_(Value retVal) {
    this.retVal = retVal;
  }

  @Override
  public String toString() {
    if (retVal != null)
      return "ret " + tempTypeName + " " + retVal.GetValueName() + "\n";
    else
      return "ret\n";
  }

  @Override
  public <T> T Accept(IRBaseVisitor<T> irBaseVisitor) {
    return irBaseVisitor.visit(this);
  }
}
