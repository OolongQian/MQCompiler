package opt.optimizers;

import ir.IrProg;
import ir.Printer;
import ir.quad.*;
import ir.structure.BasicBlock;
import ir.structure.IrFunct;
import ir.structure.Reg;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GlobalVariablePromotion {

	// load gvar to a tmp register, and then store the tmp register to greg.
	private int LoadGvar2Greg(Reg gvar, Reg greg, BasicBlock bb, int idx) {
		IrFunct funct = bb.parentFunct;
		Reg tmp = funct.GetTmpReg();
		bb.quads.add(idx++, new Load(bb, tmp, gvar));
		bb.quads.add(idx++, new Store(bb, greg, tmp));
		return idx;
	}
	
	private int StoreGreg2Gvar(Reg gvar, Reg greg, BasicBlock bb, int idx) {
		IrFunct funct = bb.parentFunct;
		Reg tmp = funct.GetTmpReg();
		bb.quads.add(idx++, new Load(bb, tmp, greg));
		bb.quads.add(idx++, new Store(bb, gvar, tmp));
		return idx;
	}
	
	private Map<String, Set<Reg>> directRegMap = new HashMap<>();
	private Map<String, Set<Reg>> closureRegMap = new HashMap<>();
	private Map<String, Set<Reg>> directDirtyMap = new HashMap<>();
	private Map<String, Set<Reg>> closureDirtyMap = new HashMap<>();
	private Map<String, Set<String>> callMap = new HashMap<>();
	
	private void PrintMap(Map<String, Set<Reg>> map, String msg) {
		System.out.println(msg);
		for (String key : map.keySet()) {
			System.out.print(key);
			map.get(key).forEach(x -> System.out.print(" " + x.name));
			System.out.println();
		}
		System.out.println();
	}
	
	public void PromoteGlobalVariables(IrProg ir) {
		CollectFunctGdefuse(ir);
		
//		PrintMap(directRegMap, "directRegMap");
//		PrintMap(closureRegMap, "closureRegMap");
//		PrintMap(directDirtyMap, "directDirtyMap");
//		PrintMap(closureDirtyMap, "closureDirtyMap");
		
		
		// create alloca for all directRegs.
		for (IrFunct funct : ir.functs.values()) {
			Map<Reg, Integer> directRegNo = GetDirectRegNo(funct);
			Set<Reg> validDirectRegSet = new HashSet<>();
			for (Reg reg : directRegMap.get(funct.name)) {
				assert directRegNo.containsKey(reg);
				if (directRegNo.get(reg) < 30) {
					validDirectRegSet.add(reg);
				}
			}
			
			Map<Reg, Reg> global2local = new HashMap<>();
			int idx = 0;
			BasicBlock entry = funct.bbs.list.Head();
			for (Reg gvar : validDirectRegSet) {
				Reg glocal = new Reg("$" + gvar.name.substring(1) + "che");
				global2local.put(gvar, glocal);
				entry.quads.add(idx++, new Alloca(entry, glocal));
			}
			for (Reg gvar : validDirectRegSet) {
				idx = LoadGvar2Greg(gvar, global2local.get(gvar), entry, idx);
			}
			
			Printer printer = new Printer(null);
			for (BasicBlock cur = funct.bbs.list.Head(); cur != null; cur = cur.next) {
				for (int i = 0; i < cur.quads.size(); ++i) {
					if (cur == funct.bbs.list.Head() && i < idx)
						continue;
					Quad quad = cur.quads.get(i);
					if (IsGload(quad)) {
						Load load = (Load) quad;
						assert global2local.keySet().contains(load.addr);
						load.addr = global2local.get(load.addr);
					}
					else if (IsGstore(quad)) {
						Store store = (Store) quad;
						assert global2local.keySet().contains(store.dst);
						store.dst = global2local.get(store.dst);
					}
					else if (quad instanceof Call) {
						Call call = (Call) quad;
						
						if (call.funcName.equals("dfs") && funct.name.equals("main")) {
							printer.print(cur);
						}
						if (callMap.keySet().contains(call.funcName)) {
							for (Reg gvar : global2local.keySet()) {
								Reg greg = global2local.get(gvar);
								// store before call.
								if (directDirtyMap.get(funct.name).contains(gvar) &&
												closureRegMap.get(call.funcName).contains(gvar)) {
									if (call.funcName.equals("dfs") && funct.name.equals("main")) {
										System.out.println("store reg : " + gvar.name);
									}
									i = StoreGreg2Gvar(gvar, greg, cur, i);
								}
								
							}
							++i;
							for (Reg gvar : global2local.keySet()) {
								Reg greg = global2local.get(gvar);
								if (closureDirtyMap.get(call.funcName).contains(gvar)) {
									// add after.
									i = LoadGvar2Greg(gvar, greg, cur, i);
								}
							}
							--i;
							
						}
						if (call.funcName.equals("dfs") && funct.name.equals("main")) {
							printer.print(cur);
						}
					}
					else if (quad instanceof Ret && !funct.name.equals("main")) {
						for (Reg gvar : global2local.keySet()) {
							if (directDirtyMap.get(funct.name).contains(gvar)) {
								i = StoreGreg2Gvar(gvar, global2local.get(gvar), cur, i);
							}
						}
					}
				}
			}
		}
	}
	
	// record global variable usage in this function, if function is used so frequently, refuse to do promotion
	// to reduce the burden on register allocator.
	private Map<Reg, Integer> GetDirectRegNo(IrFunct funct) {
		Map<Reg, Integer> directRegNo = new HashMap<>();
		directRegMap.get(funct.name).forEach(x -> directRegNo.put(x, 0));
		
		for (BasicBlock cur = funct.bbs.list.Head(); cur != null; cur = cur.next) {
			for (int i = 0; i < cur.quads.size(); ++i) {
				Quad quad = cur.quads.get(i);
				if (IsGstore(quad)) {
					Reg greg = ((Store) quad).dst;
					assert directRegNo.containsKey(greg);
					directRegNo.put(greg, directRegNo.get(greg) + 1);
				}
				else if (IsGload(quad)) {
					Reg greg = ((Load) quad).addr;
					assert directRegNo.containsKey(greg);
					directRegNo.put(greg, directRegNo.get(greg) + 1);
				}
			}
		}
		return directRegNo;
	}
	
	private void CollectFunctGdefuse(IrProg ir) {
		for (String funct : ir.functs.keySet()) {
			directRegMap.put(funct, new HashSet<>());
			closureRegMap.put(funct, new HashSet<>());
			directDirtyMap.put(funct, new HashSet<>());
			closureDirtyMap.put(funct, new HashSet<>());
			callMap.put(funct, new HashSet<>());
		}
		
		for (IrFunct funct : ir.functs.values()) {
			for (BasicBlock cur = funct.bbs.list.Head(); cur != null; cur = cur.next) {
				for (Quad quad : cur.quads) {
					if (IsGload(quad)) {
						directRegMap.get(funct.name).add(((Load) quad).addr);
					}
					else if (IsGstore(quad)) {
						directRegMap.get(funct.name).add(((Store) quad).dst);
						directDirtyMap.get(funct.name).add(((Store) quad).dst);
					}
					else if (quad instanceof Call) {
						Call call = (Call) quad;
						if (callMap.keySet().contains(call.funcName))
							callMap.get(funct.name).add(call.funcName); 
					}
				}
			}
			closureRegMap.get(funct.name).addAll(directRegMap.get(funct.name));
			closureDirtyMap.get(funct.name).addAll(directDirtyMap.get(funct.name));
		}
		
		boolean change;
		do {
			change = false;
			for (String caller : callMap.keySet()) {
				int closureRegSize = closureRegMap.get(caller).size();
				int closureDirtySize = closureDirtyMap.get(caller).size();
				for (String callee : callMap.get(caller)) {
					closureRegMap.get(caller).addAll(closureRegMap.get(callee));
					closureDirtyMap.get(caller).addAll(closureDirtyMap.get(callee));
				}
				if (closureRegMap.get(caller).size() != closureRegSize ||
								closureDirtyMap.get(caller).size() != closureDirtySize)
					change = true;
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
