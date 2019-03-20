package ir.reg;

public class Reg extends IrValue {
  protected String name;

  public Reg(String name) {
	  this.name = name;
  }

  @Override
  public String getText() {
    return name;
  }
}

