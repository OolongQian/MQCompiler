package ir.reg;

import ir.quad.Quad;

import java.util.LinkedList;
import java.util.List;

public class GlobalReg extends Reg {
  /**
   * Global variable won't have duplicated names
   * */
  private List<Quad> initLst;
  private int memAddr;
  
  public GlobalReg(String name) {
    super(name);
    initLst = new LinkedList<>();
  }

  public void EmplaceInit(Quad q) {
    initLst.add(q);
  }
	
	public int GetMemAddr() {
		return memAddr;
	}
	
	public void SetMemAddr(int memAddr) {
		this.memAddr = memAddr;
	}
}
