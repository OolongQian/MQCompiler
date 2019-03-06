package ir_codegen.quad;

import ir_codegen.util.HintName;

public class Var implements Value {

  public HintName hintNameIR;
  public String decNameAST;
  private int physicalAddr;
  private int memoerySpace;
  public boolean named = false; // used by name allocator.

  public Var(HintName hintNameIR, String decNameAST, int physicalAddr, int memoerySpace) {
    this.hintNameIR = hintNameIR;
    this.decNameAST = decNameAST;
    this.physicalAddr = physicalAddr;
    this.memoerySpace = memoerySpace;
  }

  public int getPhysicalAddr() {
    return physicalAddr;
  }

  public int getMemoerySpace() {
    return memoerySpace;
  }

  @Override
  public HintName GetValueName() {
    return hintNameIR;
  }
}
