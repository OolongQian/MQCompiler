package ir.reg;

import ir.quad.Quad;

import java.util.LinkedList;
import java.util.List;

public class GlobalReg extends Reg {
  /**
   * Global variable won't have duplicated names
   * */
  private List<Quad> initLst = new LinkedList<>();
  
  public GlobalReg(String name) {
    super(name);
  }

  public void EmplaceInit(Quad q) {
    initLst.add(q);
  }
  
  public List<Quad> GetInit() {
    return initLst;
  }
}
