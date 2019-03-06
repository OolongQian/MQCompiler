package ir_codegen.quad;

import ir_codegen.util.HintName;

public class IntImmediate implements Value {

  private int value;

  public IntImmediate(int value) {
    this.value = value;
  }

  @Override
  public HintName GetValueName() {
    return new HintName(Integer.toString(value));
  }

}
