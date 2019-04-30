package opt.optimizers;

import com.sun.imageio.plugins.jpeg.JPEGImageReaderResources;
import ir.IrProg;
import ir.structure.BBS;
import ir.structure.BasicBlock;
import ir.structure.CFG;
import ir.structure.IrFunct;
import opt.GraphInfo;

import java.util.*;

public class LoopInvariantPorter {


  public void MoveLoopInvariant (IrProg ir, Map<BasicBlock, GraphInfo> dts) {
    for (IrFunct funct : ir.functs.values()) {
      List<Set<BasicBlock>> loops = LoopFinder(funct);

    }
  }

  // a loop is a set of basic blocks.
  // a set of basic blocks that is the sub-dominant tree rooted with loop condition.
  private List<Set<BasicBlock>> loops = new LinkedList<>();
  private List<Set<BasicBlock>> LoopFinder (IrFunct funct) {
    BackedgeDetector(funct);

    return null;
  }

  // I suppose each basic block could have at most one backedge.
  private Map<BasicBlock, BasicBlock> backedges = new HashMap<>();
  private enum DfsColor { BLACK, GREY, WHITE }
  // find loops by detecting backedges.
  private void BackedgeDetector (IrFunct funct) {
    // perform dfs to funct's bbs.
    funct.bbs.BuildCFG();
    // initialize dfs color.
    Map<BasicBlock, DfsColor> dfsColors = new HashMap<>();
    for (BasicBlock cur = funct.bbs.list.Head(); cur != null; cur = cur.next)
      // haven't been visited.
      dfsColors.put(cur, DfsColor.WHITE);
    // start from entry.
    BackedgeDfsUtil(funct.bbs.list.Head(), funct.bbs.cfg, dfsColors);
  }

  private void BackedgeDfsUtil (BasicBlock cur, CFG cfg, Map<BasicBlock, DfsColor> dfsColors) {
    assert dfsColors.get(cur) == DfsColor.WHITE;
    dfsColors.put(cur, DfsColor.GREY);
    // dfs to scs.
    for (BasicBlock scs : cfg.successors.get(cur)) {
      if (dfsColors.get(scs) == DfsColor.GREY)
        // encounter back-edges.
        backedges.put(cur, scs);
      else if (dfsColors.get(scs) == DfsColor.BLACK)
        // encounter cross edges.
        ;
      else
        // hasn't visited, recursion down.
        BackedgeDfsUtil(scs, cfg, dfsColors);
    }
    // blacken myself.
    dfsColors.put(cur, DfsColor.BLACK);
  }


}
