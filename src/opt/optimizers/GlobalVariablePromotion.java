package opt.optimizers;

import ir.IrProg;
import ir.quad.*;
import ir.structure.BasicBlock;
import ir.structure.IrFunct;
import ir.structure.Reg;

import java.util.HashMap;
import java.util.Map;

// alloca a virtual register for each global variables.
// this should be applied after SSA destruction.
public class GlobalVariablePromotion {

  public void PromoteGlobalVariables(IrProg ir) {
    for (IrFunct funct : ir.functs.values()) {
      // scan used global variables. create corresponding local version for each of them.
      Map<Reg, Reg> promoteMap = new HashMap();
      for (BasicBlock cur = funct.bbs.list.Head(); cur != null; cur = cur.next) {
        for (Quad quad : cur.quads) {
          if (quad instanceof Load && ((Load) quad).addr.name.startsWith("@")) {
            Reg global = ((Load) quad).addr;
            Reg local = new Reg("$" + global.name);
            promoteMap.put(global, local);
          }
        }
      }

      // promote used global variables.
      // alloca a local reg for each of them, replace the use ->
      // replace store and load by mov.
      // add store and load in each ambiguous place -> call, ret.
      // add is necessary, store may be redundant, since the value
      // may not be dirty. we could use later pass to handle them.

      // alloca locals.
      BasicBlock entry = funct.bbs.list.Head();
      for (Reg promLocal : promoteMap.values()) {
        Alloca alloca = new Alloca(promLocal); alloca.blk = entry;
        entry.quads.add(0, alloca);
      }

      // replace global use -> load & store
      // add store before call and ret.
      for (BasicBlock cur = funct.bbs.list.Head(); cur != null; cur = cur.next) {
        for (Quad quad : cur.quads) {
          if (quad instanceof Load && ((Load) quad).addr.name.startsWith("@")) {
          }
          else if (quad instanceof Store && ((Store) quad).dst.name.startsWith("@")) {

          }
          // ambiguous quads.
          else if ((quad instanceof Call) ||
              (quad instanceof Ret)) {


          }
        }
      }
    }
  }
}
