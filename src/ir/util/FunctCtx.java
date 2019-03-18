package ir.util;

import ir.reg.Reg;

import java.util.LinkedList;
import java.util.List;

public class FunctCtx {
  public String name;
  public List<String> args;
  public List<BasicBlock> bbs = new LinkedList<>();
  private int tmpRegCnt;

  public FunctCtx(String name, List<String> args) {
    this.name = name;
    this.args = args;
    tmpRegCnt = 0;
  }

	public BasicBlock GetNewBB(String hintName) {
  	if (hintName == null)
  		hintName = "";
		BasicBlock nb = new BasicBlock("&" + Integer.toString(bbs.size()) + " " + hintName);
		bbs.add(nb);
		return nb;
	}

  /**
   * Insert a BB after the input BB, and return it.
   * */
  public BasicBlock NewBBAfter(BasicBlock bb, String hintName) {
	  if (hintName == null)
		  hintName = "";
    BasicBlock nb = new BasicBlock("&" + Integer.toString(bbs.size()) + " " + hintName);
    // find the position of input bb.
    int index = bbs.indexOf(bb);
    // insert new bb after that.
    bbs.add(index + 1, nb);
    return nb;
  }

  public BasicBlock GetLastBB() {
    assert bbs.size() != 0;
    return bbs.get(bbs.size()-1);
  }

  public BasicBlock GetHeadBB() {
    assert bbs.size() != 0;
    return bbs.get(0);
  }

  public Reg GetTmpLocalReg() {
    return new Reg('%' + Integer.toString(tmpRegCnt++));
  }
}
