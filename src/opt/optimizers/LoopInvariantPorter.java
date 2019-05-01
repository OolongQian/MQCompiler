package opt.optimizers;

import ir.IrProg;
import ir.quad.*;
import ir.structure.BasicBlock;
import ir.structure.CFG;
import ir.structure.IrFunct;
import ir.structure.Reg;
import opt.GraphInfo;

import java.util.*;

public class LoopInvariantPorter {

  private Map<String, Set<Reg>> gvarDefsMap = new HashMap<>(); 
  public void ConfigGvarCallDef(IrProg ir) {
    ir.functs.keySet().forEach(x -> gvarDefsMap.put(x, new HashSet<>()));
    for (IrFunct funct : ir.functs.values()) {
      for (BasicBlock cur = funct.bbs.list.Head(); cur != null; cur = cur.next) {
        for (Quad quad : cur.quads) {
          if (quad instanceof Store && ((Store) quad).dst.name.startsWith("@"))
            gvarDefsMap.get(funct.name).add(((Store) quad).dst);
        }
      }
    }
  }

  public void MoveLoopInvariant (IrFunct funct,  Map<BasicBlock, GraphInfo> dts) {
    Map<BasicBlock, BasicBlock> backedges = DetectBackedges(funct);
    Map<BasicBlock, List<BasicBlock>> cond2rejump = new HashMap<>();
    backedges.values().forEach(x -> cond2rejump.put(x, new LinkedList<>()));
    backedges.keySet().forEach(x -> cond2rejump.get(backedges.get(x)).add(x));

    // target of backedges are conds.
    for (BasicBlock cond : cond2rejump.keySet()) {
      // build cfg for loop detection.
      funct.bbs.BuildCFG();
      Set<BasicBlock> loop = FindLoop(cond, cond2rejump.get(cond));
      List<Quad> invariantDefs = ComputeInvariant(loop);

      // find preheader placed in irBuilder phase, insert invariant defs into it.
      // this is a very ugly way to do this.
      List<BasicBlock> outsideLoop = new LinkedList<>();
      for (BasicBlock pre : cond.parentFunct.bbs.cfg.predesessors.get(cond)) {
        if (!loop.contains(pre))
          outsideLoop.add(pre);
      }
//      System.out.println(outsideLoop.size());
//      System.out.println(cond.name);
//      loop.forEach(x -> System.out.println(x.name));
//      System.out.println();
//      outsideLoop.forEach(x -> System.out.println(x.name));
      assert outsideLoop.size() == 1;
      BasicBlock preheader = outsideLoop.get(0);

      int ipos = preheader.quads.size() - 1;
      for (Quad idef : invariantDefs)
        preheader.quads.add(ipos++, idef);
    }
  }

  // compute invariant definition in the loop.
  // a fixed-point algorithm.
  // initial invariant : global variable, constant, string, variables defined outside the loop.
  // note : invariant quads have been deleted during invariant code detection.
  private List<Quad> ComputeInvariant(Set<BasicBlock> loop) {
    List<Quad> invarDefs = new LinkedList<>();

    // first, find all variables defined outside the loop.
    Set<Reg> invarRegs = new HashSet<>();

    // -- add all uses.
    List<Reg> uses = new ArrayList<>();
    for (BasicBlock loopBB : loop) {
      for (Quad quad : loopBB.quads) {
        quad.GetUseRegs(uses);
        invarRegs.addAll(uses);
      }
    }
    // -- remove uses defined in loop.
    // -- -- remove defined global variable, by call.
    for (BasicBlock loopBB : loop) {
      for (Quad quad : loopBB.quads) {
        if (quad instanceof Store)
          invarRegs.remove(((Store) quad).dst);
        else if (quad instanceof Call && gvarDefsMap.containsKey(((Call) quad).funcName)) {
          invarRegs.removeAll(gvarDefsMap.get(((Call) quad).funcName));
          if (!((Call) quad).ret.name.equals("@null"))
            invarRegs.remove(((Call) quad).ret);
        }
        else if (quad.GetDefReg() != null)
          invarRegs.remove(quad.GetDefReg());
      }
    }

    // fixed-point while(change)
    boolean change;
    do {
      change = false;
      for (BasicBlock loopBB : loop) {
        for (int i = 0; i < loopBB.quads.size(); ++i) {
          Quad quad = loopBB.quads.get(i);
          // skip defs already had.
          if (invarDefs.contains(quad))
            continue;
          // for possible instruction movement :
          if ((quad instanceof Load) ||
              (quad instanceof Store) ||
              (quad instanceof Mov) ||
              (quad instanceof Unary) ||
              (quad instanceof Binary)) {
            uses.clear();
            quad.GetUseRegs(uses);
            boolean invar = true;
            for (Reg use : uses) {
              if (!invarRegs.contains(use))
                invar = false;
            }
            if (invar) {
              assert !invarDefs.contains(quad);
              invarDefs.add(quad);
              change = true;
              // we need i-- to stay here, in order to access the next quad in for loop.
              loopBB.quads.remove(i--);
            }
          }
        }
      }
    } while (change);

    return invarDefs;
  }

  // a loop is a set of basic blocks.
  // a set of basic blocks that is the sub-dominant tree rooted with loop condition.
  // reset for each IrFunct.
  private Set<BasicBlock> FindLoop (BasicBlock cond,
                                    List<BasicBlock> reJumps) {
    Set<BasicBlock> loop = new HashSet<>();

    IrFunct funct = cond.parentFunct;
    Deque<BasicBlock> worklist = new ArrayDeque<>(reJumps);
    while (!worklist.isEmpty()) {
      BasicBlock cur = worklist.removeFirst();
      loop.add(cur);
//      System.out.println(cur.name);
//      funct.bbs.cfg.predesessors.get(cur).forEach(x -> System.out.println(x.name));
      if (cur != cond) {
        for (BasicBlock pred : funct.bbs.cfg.predesessors.get(cur)) {
          if (!loop.contains(pred))
            worklist.add(pred);
        }
      }
    }

    return loop;
  }

  // I suppose each basic block could have at most one backedge.
  private enum DfsColor { BLACK, GREY, WHITE }
  // find loops by detecting backedges.
  private Map<BasicBlock, BasicBlock> DetectBackedges (IrFunct funct) {
    Map<BasicBlock, BasicBlock> backedges = new HashMap<>();

    // perform dfs to funct's bbs.
    funct.bbs.BuildCFG();
    // initialize dfs color.
    Map<BasicBlock, DfsColor> dfsColors = new HashMap<>();
    for (BasicBlock cur = funct.bbs.list.Head(); cur != null; cur = cur.next)
      // haven't been visited.
      dfsColors.put(cur, DfsColor.WHITE);
    // start from entry.
    BackedgeDfsUtil(funct.bbs.list.Head(), funct.bbs.cfg, dfsColors, backedges);

    return backedges;
  }

  private void BackedgeDfsUtil (BasicBlock cur, CFG cfg,
                                Map<BasicBlock, DfsColor> dfsColors,
                                Map<BasicBlock, BasicBlock> backedges) {

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
        BackedgeDfsUtil(scs, cfg, dfsColors, backedges);
    }
    // blacken myself.
    dfsColors.put(cur, DfsColor.BLACK);
  }
}
