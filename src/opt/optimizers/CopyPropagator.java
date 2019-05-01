package opt.optimizers;

import ir.IrProg;
import ir.quad.Mov;
import ir.quad.Phi;
import ir.quad.Quad;
import ir.structure.BasicBlock;
import ir.structure.IrFunct;
import ir.structure.IrValue;
import ir.structure.Reg;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CopyPropagator {

	public void PropagateCopy(IrProg ir) {
		for (IrFunct funct : ir.functs.values()) {
			// first pass collect mov.
			Map<Reg, IrValue> valMap = new HashMap<>();

			for (BasicBlock cur = funct.bbs.list.Head(); cur != null; cur = cur.next) {
				for (Quad inst : cur.quads) {
					if (inst instanceof Mov) {
						Mov mov = (Mov) inst;
						if (mov.src instanceof Reg && valMap.containsKey(mov.src))
							valMap.put(mov.dst, valMap.get(mov.src));
						else
							valMap.put(mov.dst, mov.src);
					}
				}
			}

			// second pass replace uses of keys in valMap.
			for (BasicBlock cur = funct.bbs.list.Head(); cur != null; cur = cur.next) {
				for (Quad inst : cur.quads) {
					List<Reg> uses = new LinkedList<>();
					inst.GetUseRegs(uses);
					for (Reg use : uses) {
						if (valMap.containsKey(use)) {
							inst.ReplaceUse(use, valMap.get(use));
						}
					}
				}
			}
		}
	}
}
