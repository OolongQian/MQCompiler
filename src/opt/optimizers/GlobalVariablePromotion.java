package opt.optimizers;

import ir.IrProg;
import ir.quad.*;
import ir.structure.BasicBlock;
import ir.structure.IrFunct;
import ir.structure.Reg;

import java.util.*;

// do global variable promotion within a basic block.
// if in a basic block, a global variable is used more than once, then create a virtual register to hold it.
// since basic block executes linearly, no need to worry about being interrupted before saving global var's copy.
// except 'call'. But if we know a call won't redefine the global variable in our interest, we don't have to
// store and reload it.
// if the global virtual register is redefined, then a store should be added at the end of the basic block.
public class GlobalVariablePromotion {
	
	private Map<Reg, Reg> gCache = new HashMap<>();
	private Map<Reg, Boolean> dirty = new HashMap<>();
	
	public void PromoteGlobalVariable(IrProg ir) {
		CollectFunctDefGvar(ir);
		for (IrFunct funct : ir.functs.values()) {
			for (BasicBlock cur = funct.bbs.list.Head(); cur != null; cur = cur.next) {
				gCache.clear();
				dirty.clear();
				for (int i = 0; i < cur.quads.size(); ++i) {
					Quad quad = cur.quads.get(i);
					if (quad instanceof Load && ((Load) quad).addr.name.startsWith("@")) {
						Load gload = (Load) quad;
						if (!gCache.containsKey(gload.addr)) {
							gCache.put(gload.addr, gload.val);
							dirty.put(gload.addr, Boolean.FALSE);
						}
						else {
							// replace load with a move
							Mov movLoad = new Mov (gload.val, gCache.get(gload.addr));
							movLoad.blk = cur;
							cur.quads.remove(i);
							cur.quads.add(i, movLoad);
						}
					}
					else if (quad instanceof Store && ((Store) quad).dst.name.startsWith("@")) {
						Store gStore = (Store) quad;
						if (gCache.containsKey(gStore.dst)) {
							assert dirty.containsKey(gStore.dst);
							dirty.put(gStore.dst, Boolean.TRUE);
							// replace store with a move
							Reg cacheTmp = funct.GetTmpReg();
							gCache.put(gStore.dst, cacheTmp);
							Mov movStore = new Mov (cacheTmp, gStore.src);
							movStore.blk = cur;
							cur.quads.remove(i);
							cur.quads.add(i, movStore);
						}
					}
					else if (quad instanceof Call) {
						Call call = (Call) quad;
						if (funct2gvar.containsKey(call.funcName)) {
							Set<Reg> callGdefs = funct2gvar.get(call.funcName);
							for (Reg callDef : callGdefs) {
								if (gCache.containsKey(callDef) && dirty.get(callDef)) {
									// if the greg to be defined is in cache, and it is dirty, write back.
									Store wb = new Store (callDef, gCache.get(callDef));
									wb.blk = cur;
									// add store before call.
									cur.quads.add(i++, wb);
									// remove them from cache.
									dirty.remove(callDef);
									gCache.remove(callDef);
								}
							}
						}
					}
				}
				int wbPos = cur.quads.size() - 1;
				for (Reg greg : gCache.keySet()) {
					assert dirty.containsKey(greg);
					if (dirty.get(greg)) {
						Store wbStore = new Store (greg, gCache.get(greg));
						wbStore.blk = cur;
						cur.quads.add(wbPos++, wbStore);
					}
				}
			}
		}
		
	}
	
	private Map<String, Set<Reg>> funct2gvar = new HashMap<>();
	private Map<String, Set<String>> funct2calls = new HashMap<>();
	private void CollectFunctDefGvar(IrProg ir) {
		ir.functs.keySet().forEach(x -> funct2gvar.put(x, new HashSet<>()));
		ir.functs.keySet().forEach(x -> funct2calls.put(x, new HashSet<>()));
		
		for (IrFunct funct : ir.functs.values()) {
			for(BasicBlock cur = funct.bbs.list.Head(); cur != null; cur = cur.next) {
				for (Quad quad : cur.quads) {
					if (quad instanceof Store && ((Store) quad).dst.name.startsWith("@")) {
						funct2gvar.get(funct.name).add(((Store) quad).dst);
					}
					else if (quad instanceof Call) {
						funct2calls.get(funct.name).add(((Call) quad).funcName);
					}
				}
			}
		}
		
		boolean change;
		do {
			change = false;
			for (String caller : funct2calls.keySet()) {
				for (String callee : funct2calls.get(caller)) {
					if (funct2gvar.containsKey(callee)) {
						Set<Reg> calleegReg = funct2gvar.get(callee);
						for (Reg greg : calleegReg) {
							Set<Reg> callergReg = funct2gvar.get(caller);
							if (!callergReg.contains(greg)) {
								callergReg.add(greg);
								change = true;
							}
						}
					}
				}
			}
		} while (change);
	}
}
