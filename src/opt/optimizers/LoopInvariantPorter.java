package opt.optimizers;

import ir.IrProg;
import ir.structure.BasicBlock;
import ir.structure.IrFunct;

import java.util.HashMap;
import java.util.Map;

public class LoopInvariantPorter {


  public void MoveLoopInvariant () {

  }

  // I suppose each basic block could have at most one backedge.
  private Map<BasicBlock, BasicBlock> backedges = new HashMap<>();
  private enum DfsColor { BLACK, GREY, WHITE }

  private void BackedgeDetection (IrFunct funct) {
    // perform dfs to funct's bbs.
    funct.bbs.BuildCFG();
    // initialize dfs color.
    Map<BasicBlock, DfsColor> dfsColors = new HashMap<>();
    for (BasicBlock cur = funct.bbs.list.Head(); cur != null; cur = cur.next)
      // haven't been visited.
      dfsColors.put(cur, DfsColor.WHITE);
    // start from entry.
    BackedgeDfsUtil(funct.bbs.list.Head(), dfsColors);
  }

  private void BackedgeDfsUtil (BasicBlock cur, Map<BasicBlock, DfsColor> dfsColors) {

  }
}
