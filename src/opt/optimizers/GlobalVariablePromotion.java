package opt.optimizers;

import ir.IrProg;
import ir.quad.*;
import ir.structure.BasicBlock;
import ir.structure.IrFunct;
import ir.structure.Reg;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GlobalVariablePromotion {
	
	private enum CacheState {
		COLD, DIRTY, VALID
	}
	
	private Map<Reg, CacheState> gvarStates = new HashMap<>();
	private Map<Reg, Reg> gvar2greg = new HashMap<>();
	
	// load gvar to a tmp register, and then store the tmp register to greg.
	private int LoadGvar2Greg(Reg gvar, Reg greg, BasicBlock bb, int idx) {
		IrFunct funct = bb.parentFunct;
		Reg tmp = funct.GetTmpReg();
		Load load = new Load(tmp, gvar);
		Store store = new Store(greg, tmp);
		load.blk = store.blk = bb;
		bb.quads.add(idx++, load);
		bb.quads.add(idx++, store);
		return idx;
	}
	
	private int StoreGreg2Gvar(Reg gvar, Reg greg, BasicBlock bb, int idx) {
		IrFunct funct = bb.parentFunct;
		Reg tmp = funct.GetTmpReg();
		Load load = new Load(tmp, greg);
		Store store = new Store(gvar, tmp);
		load.blk = store.blk = bb;
		bb.quads.add(idx++, load);
		bb.quads.add(idx++, store);
		return idx;
	}
	
	private Set<Reg> FindUEgvars(IrFunct funct) {
		Set<Reg> ue = new HashSet<>();
		Set<Reg> defed = new HashSet<>();
		for (BasicBlock cur = funct.bbs.list.Head(); cur != null; cur = cur.next) {
			for (int i = 0; i < cur.quads.size(); ++i) {
				Quad quad = cur.quads.get(i);
				if (IsGstore(quad)) {
					defed.add(((Store) quad).dst);
				}
				else if (IsGload(quad)) {
					Load load = (Load) quad;
					if (!defed.contains(load.addr)) {
						ue.add(load.addr);
					}
				}
				else if (quad instanceof Call) {
					Call call = (Call) quad;
					if (funct2guse.containsKey(call.funcName)) {
						for (Reg use : funct2guse.get(call.funcName)) {
							if (!defed.contains(use))
								ue.add(use);
						}
					}
				}
			}
		}
		return ue;
	}
	
	public void PromoteGlobalVariables(IrProg ir) {
		CollectFunctGdefuse(ir);
		
		for (IrFunct funct : ir.functs.values()) {
			// initial setup.
			Set<Reg> gvars = new HashSet<>();
			gvars.addAll(funct2gdef.get(funct.name));
			gvars.addAll(funct2guse.get(funct.name));
			gvarStates.clear();
			gvar2greg.clear();
			
			// gregs input.
			for (Reg gvar : gvars) {
				gvarStates.put(gvar, CacheState.VALID);
				gvar2greg.put(gvar, new Reg("$" + gvar.name.substring(1) + "che"));
			}
			
			// do alloca
			int idx = 0;
			for (Reg greg : gvar2greg.values()) {
				Alloca allocaPromote = new Alloca(greg);
				allocaPromote.blk = funct.bbs.list.Head();
				funct.bbs.list.Head().quads.add(idx++, allocaPromote);
			}
			
			// find UE gvars, initialize them.
			Set<Reg> ue = FindUEgvars(funct);
			for (Reg init : ue) {
				idx = LoadGvar2Greg(init, gvar2greg.get(init), funct.bbs.list.Head(), idx);
			}
			
			// do promotion.
			for (BasicBlock cur = funct.bbs.list.Head(); cur != null; cur = cur.next) {
				for (int i = 0; i < cur.quads.size(); ++i) {
					if (cur == funct.bbs.list.Head() && i < idx)
						continue;
					Quad quad = cur.quads.get(i);
					if (IsGstore(quad)) {
						Store store = (Store) quad;
						Reg gvar = store.dst;
						assert gvars.contains(gvar);
						assert gvarStates.containsKey(gvar);
						// store greg, set dirty.
						store.dst = gvar2greg.get(gvar);
						gvarStates.put(gvar, CacheState.DIRTY);
					}
					else if (IsGload(quad)) {
						Load load = (Load) quad;
						Reg gvar = load.addr;
						assert gvars.contains(gvar);
						assert gvarStates.containsKey(gvar);
						// load greg, load if cold.
						if (gvarStates.get(gvar) == CacheState.COLD) {
							i = LoadGvar2Greg(gvar, gvar2greg.get(gvar), cur, i);
							gvarStates.put(gvar, CacheState.VALID);
						}
						load.addr = gvar2greg.get(gvar);
					}
					else if (quad instanceof Call) {
						Call call = (Call) quad;
						if (funct2gdef.containsKey(call.funcName)) {
							for (Reg guse : funct2guse.get(call.funcName)) {
								// store before call if dirty.
								if (gvarStates.get(guse) == CacheState.DIRTY) {
									i = StoreGreg2Gvar(guse, gvar2greg.get(guse), cur, i);
									gvarStates.put(guse, CacheState.VALID);
								}
							}
							for (Reg gdef : funct2gdef.get(call.funcName)) {
								gvarStates.put(gdef, CacheState.COLD);
							}
						}
					}
				}
			}
			// at the end of the function.
			if (!funct.name.equals("main")) {
				BasicBlock tail = funct.bbs.list.Tail();
				int appendPos = tail.quads.size() - 1;
				for (Reg gvar : gvarStates.keySet()) {
					if (gvarStates.get(gvar) == CacheState.DIRTY) {
						appendPos = StoreGreg2Gvar(gvar, gvar2greg.get(gvar), tail, appendPos);
					}
				}
			}
		}
	}
	
	private Map<String, Set<Reg>> funct2guse = new HashMap<>();
	private Map<String, Set<Reg>> funct2gdef = new HashMap<>();
	private Map<String, Set<String>> funct2calls = new HashMap<>();
	
	private void CollectFunctGdefuse(IrProg ir) {
		ir.functs.keySet().forEach(x -> funct2gdef.put(x, new HashSet<>()));
		ir.functs.keySet().forEach(x -> funct2guse.put(x, new HashSet<>()));
		ir.functs.keySet().forEach(x -> funct2calls.put(x, new HashSet<>()));
		
		for (IrFunct funct : ir.functs.values()) {
			for (BasicBlock cur = funct.bbs.list.Head(); cur != null; cur = cur.next) {
				for (Quad quad : cur.quads) {
					if (IsGstore(quad)) {
						funct2gdef.get(funct.name).add(((Store) quad).dst);
					}
					else if (IsGload(quad)) {
						funct2guse.get(funct.name).add(((Load) quad).addr);
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
					assert funct2guse.containsKey(callee) == funct2gdef.containsKey(callee);
					if (funct2guse.containsKey(callee)) {
						Set<Reg> calleeUse = funct2guse.get(callee);
						Set<Reg> calleeDef = funct2gdef.get(callee);
						for (Reg greg : calleeUse) {
							Set<Reg> callerUse = funct2guse.get(caller);
							if (!callerUse.contains(greg)) {
								callerUse.add(greg);
								change = true;
							}
						}
						for (Reg greg : calleeDef) {
							Set<Reg> callerDef = funct2gdef.get(caller);
							if (!callerDef.contains(greg)) {
								callerDef.add(greg);
								change = true;
							}
						}
					}
				}
			}
		} while (change);
	}
	
	private boolean IsGstore(Quad quad) {
		return quad instanceof Store &&
						((Store) quad).dst.name.startsWith("@") &&
						!((Store) quad).dst.name.equals("@null");
	}
	
	private boolean IsGload(Quad quad) {
		return quad instanceof Load &&
						((Load) quad).addr.name.startsWith("@") &&
						!((Load) quad).addr.name.equals("@null");
	}
}
