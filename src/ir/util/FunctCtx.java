package ir.util;

import ir.reg.LocalReg;
import ir.reg.Reg;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class FunctCtx {
  public String name;
  public List<Reg> args;
  public List<BasicBlock> bbs = new LinkedList<>();
  private int tmpRegCnt;
	private HashMap<String, Integer> locVar = new HashMap<>();
  
  public FunctCtx(String name, List<Reg> args) {
    this.name = name;
    this.args = (args == null) ? new LinkedList<>() : args;
    tmpRegCnt = 0;
  }

	public BasicBlock GetNewBB(String hintName) {
  	if (hintName == null)
  		hintName = "";
		BasicBlock nb = new BasicBlock("&" + Integer.toString(bbs.size()) + "_" + hintName);
		bbs.add(nb);
		return nb;
	}

  /**
   * Insert a BB after the input BB, and return it.
   * */
  public BasicBlock NewBBAfter(BasicBlock bb, String hintName) {
	  if (hintName == null)
		  hintName = "";
    BasicBlock nb = new BasicBlock("&" + Integer.toString(bbs.size()) + "_" + hintName);
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

  public Reg GetTmpReg() {
    return new Reg('%' + Integer.toString(tmpRegCnt++));
  }
  
  public Reg GetLocReg(String name) {
  	if (!locVar.containsKey(name))
  		locVar.put(name, 0);
  	int id = locVar.get(name);
  	locVar.put(name, id + 1);
	  return new LocalReg('$' + name + "_" + id);
  }
}
