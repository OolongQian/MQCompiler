package ir.reg;

/**
 * Only have integer type in IR.
 * */
public class IntLiteral extends IrValue {
  public int val;

  public IntLiteral(int val) {
    this.val = val;
  }

  @Override
  public String getText() {
    return Integer.toString(val);
  }
}
