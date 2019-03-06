package ir_codegen.quad;

import ir_codegen.util.HintName;

public class Virtual implements Value {

  public HintName hintNameIR;
  public boolean named = false; // used by name allocator.

  public Virtual(HintName hintNameIR) {
    this.hintNameIR = hintNameIR;
  }

  @Override
  public HintName GetValueName() {
    return hintNameIR;
  }
}
