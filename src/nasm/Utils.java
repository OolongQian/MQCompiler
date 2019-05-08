package nasm;

import ir.structure.BasicBlock;
import nasm.inst.*;
import nasm.reg.AsmReg;
import nasm.reg.PhysicalReg;
import nasm.reg.Reg;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static nasm.reg.PhysicalReg.PhyRegType.rax;
import static nasm.reg.PhysicalReg.PhyRegType.rdx;

public class Utils {
	public static void DelMsg (AsmFunct asmFunct) {
		for (AsmBB bb : asmFunct.bbs) {
			bb.insts.removeIf(x -> x instanceof Msg);
		}
	}
	
	public static void Say (AsmBB bb, int idx, String something) {
		bb.insts.add(idx, new Msg (bb, something));
	}
	
	public static void Say (AsmBB bb, String something) {
		bb.insts.add(new Msg (bb, something));
	}
	
	// turn *0 into __0
	public static String StringLiteralRenamer (String strId) {
		return "_S_" + strId.substring(1);
	}
	
	// rename basic block's name to be AsmBB, attach parentFunction to it.
	// and again, rename Basic block name in jump.
	public static String BasicBlockRenamer (BasicBlock bb) {
		return String.format("_B_%s_%s", bb.name.substring(1), FunctRenamer(bb.parentFunct.name));
	}
	
	// change @a to _G_a.
	public static String GlobalRenamer (String gName) {
		return String.format("_G_%s", gName.substring(1));
	}
	
	private static Map<String, String> builtin2Extern = new HashMap<>();
	static {
		builtin2Extern.put("~print", "print");
		builtin2Extern.put("~memset", "memset");
		builtin2Extern.put("~println", "println");
		builtin2Extern.put("~getString", "getString");
		builtin2Extern.put("~getInt", "getInt");
		builtin2Extern.put("~toString", "toString");
		builtin2Extern.put("~-string#length", "_stringLength");
		builtin2Extern.put("~-string#substring", "_stringSubstring");
		builtin2Extern.put("~-string#parseInt", "_stringParseInt");
		builtin2Extern.put("~-string#ord", "_stringOrd");
		builtin2Extern.put("~-string#add", "_stringAdd");
		builtin2Extern.put("~--array#size", "_arraySize");
		builtin2Extern.put("~-string#lt", "_stringLt");
		builtin2Extern.put("~-string#gt", "_stringGt");
		builtin2Extern.put("~-string#le", "_stringLe");
		builtin2Extern.put("~-string#ge", "_stringGe");
		builtin2Extern.put("~-string#eq", "_stringEq");
		builtin2Extern.put("~-string#neq", "_stringNeq");
	}
	public static String FunctRenamer (String fName) {
		if (fName.startsWith("~")) {
			assert builtin2Extern.containsKey(fName);
			return builtin2Extern.get(fName);
		}
		else {
			fName = fName.replace('-', '_');
			fName = fName.replace('#', '_');
			return fName;
		}
	}
	
	// when getting a physical register, we are not directly creating a physical register. But create
	// a pre-colored temp virtual register.
	public static AsmReg GetPReg (PhysicalReg.PhyRegType phyReg) {
		Reg tmpVReg = new Reg ("v" + phyReg.name());
		tmpVReg.AllocReg(new PhysicalReg(phyReg));
		return tmpVReg;
	}
	
	public static AsmReg GetPReg (String hintName, PhysicalReg.PhyRegType phyReg) {
		Reg tmpVReg = new Reg ("v" + phyReg.name());
		tmpVReg.AllocReg(new PhysicalReg(hintName, phyReg));
		return tmpVReg;
	}
	
	
	public static List<Reg> GetUses (Inst inst) {
		List<Reg> uses = new LinkedList<>();
		
		if (inst instanceof Call) { }
		else if (inst instanceof Cmp) {
			if (inst.dst instanceof Reg) uses.add((Reg) inst.dst);
			if (inst.src instanceof Reg) uses.add((Reg) inst.src);
		}
		else if (inst instanceof Jmp) { }
		else if (inst instanceof Lea) {
//			if (inst.dst instanceof Reg) uses.add((Reg) inst.dst);
			if (inst.src instanceof Reg) uses.add((Reg) inst.src);
		}
		else if (inst instanceof Mov) {
			if (inst.src instanceof Reg) uses.add((Reg) inst.src);
		}
		else if (inst instanceof Oprt) {
			if (((Oprt) inst).op == Oprt.Op.IDIV) {
				uses.add((Reg) GetPReg(rax));
				uses.add((Reg) GetPReg(rdx));
			}
			if (inst.dst instanceof Reg) uses.add((Reg) inst.dst);
			if (inst.src instanceof Reg) uses.add((Reg) inst.src);
		}
		else if (inst instanceof Msg) { }
		else if (inst instanceof Pop) { }
		else if (inst instanceof Push) {
			if (inst.src instanceof Reg) uses.add((Reg) inst.src);
		}
		else if (inst instanceof Load) {
			if (inst.src instanceof Reg) uses.add((Reg) inst.src);
		}
		else if (inst instanceof Store) {
			// store actually uses both dst and src.
			if (inst.dst instanceof Reg) uses.add((Reg) inst.dst);
			if (inst.src instanceof Reg) uses.add((Reg) inst.src);
		}
		else if (inst instanceof Ret) {
			for (PhysicalReg.PhyRegType lee : PhysicalReg.calleeSave)
				uses.add((Reg) GetPReg(lee));
		}
		else {
			assert false;
		}
		
		return uses;
	}
	
	public static List<Reg> GetDefs (Inst inst) {
		List<Reg> defs = new LinkedList<>();
		
		if (inst instanceof Call) {
			// add a virtual rax register.
//			if (((Call) inst).ret) defs.add((Reg) GetPReg(rax));
			PhysicalReg.callerSave.forEach(x -> defs.add((Reg) GetPReg(x)));
		}
		else if (inst instanceof Cmp) {
			if (((Cmp) inst).flagReg instanceof Reg) defs.add(((Cmp) inst).flagReg);
		}
		else if (inst instanceof Jmp) { }
		else if (inst instanceof Lea) {
			if (inst.dst instanceof Reg) defs.add((Reg) inst.dst);
		}
		else if (inst instanceof Mov) {
			if (inst.dst instanceof Reg) defs.add((Reg) inst.dst);
		}
		else if (inst instanceof Oprt) {
			// idiv is strange.
			if (((Oprt) inst).op == Oprt.Op.IDIV) {
				defs.add((Reg) GetPReg(rax));
				defs.add((Reg) GetPReg(rdx));
			}
			else if (inst.dst instanceof Reg) defs.add((Reg) inst.dst);
			
		}
		else if (inst instanceof Msg) { }
		else if (inst instanceof Pop) {
			if (inst.dst instanceof Reg) defs.add((Reg) inst.dst);
		}
		else if (inst instanceof Push) { }
		else if (inst instanceof Load) {
			if (inst.dst instanceof Reg) defs.add((Reg) inst.dst);
		}
		else if (inst instanceof Store) {
			// store doesn't actually define anything new.
//			if (inst.dst instanceof Reg) defs.add((Reg) inst.dst);
		}
		else if (inst instanceof Ret) { }
		else {
			assert false;
		}
		
		return defs;
	}
	
	public static void ReplaceUses (Inst inst, String old, String new_) {
		boolean changed = false;
		
		if (inst instanceof Call) { }
		else if (inst instanceof Cmp) {
			if (inst.dst instanceof Reg && ((Reg) inst.dst).hintName.equals(old)) {
				assert !((Reg) inst.dst).isColored();
				((Reg) inst.dst).hintName = new_;
				changed = true;
			}
			if (inst.src instanceof Reg && ((Reg) inst.src).hintName.equals(old)) {
				assert !((Reg) inst.src).isColored();
				((Reg) inst.src).hintName = new_;
				changed = true;
			}
		}
		else if (inst instanceof Jmp) { }
		else if (inst instanceof Lea) {
//			if (inst.dst instanceof Reg && ((Reg) inst.dst).hintName.equals(old)) {
//				assert !((Reg) inst.dst).isColored();
//				((Reg) inst.dst).hintName = new_;
//				changed = true;
//			}
			if (inst.src instanceof Reg && ((Reg) inst.src).hintName.equals(old)) {
				assert !((Reg) inst.src).isColored();
				((Reg) inst.src).hintName = new_;
				changed = true;
			}
		}
		else if (inst instanceof Mov) {
			if (inst.src instanceof Reg && ((Reg) inst.src).hintName.equals(old)) {
				assert !((Reg) inst.src).isColored();
				((Reg) inst.src).hintName = new_;
				changed = true;
			}
		}
		else if (inst instanceof Oprt) {
			if (inst.dst instanceof Reg && ((Reg) inst.dst).hintName.equals(old)) {
				assert !((Reg) inst.dst).isColored();
				((Reg) inst.dst).hintName = new_;
				changed = true;
			}
			if (inst.src instanceof Reg && ((Reg) inst.src).hintName.equals(old)) {
				assert !((Reg) inst.src).isColored();
				((Reg) inst.src).hintName = new_;
				changed = true;
			}
		}
		else if (inst instanceof Msg) { }
		else if (inst instanceof Pop) { }
		else if (inst instanceof Push) {
			if (inst.src instanceof Reg && ((Reg) inst.src).hintName.equals(old)) {
				assert !((Reg) inst.src).isColored();
				((Reg) inst.src).hintName = new_;
				changed = true;
			}
		}
		else if (inst instanceof Load) {
			if (inst.src instanceof Reg && ((Reg) inst.src).hintName.equals(old)) {
				assert !((Reg) inst.src).isColored();
				((Reg) inst.src).hintName = new_;
				changed = true;
			}
		}
		else if (inst instanceof Store) {
			if (inst.dst instanceof Reg && ((Reg) inst.dst).hintName.equals(old)) {
				assert !((Reg) inst.dst).isColored();
				((Reg) inst.dst).hintName = new_;
				changed = true;
			}
			if (inst.src instanceof Reg && ((Reg) inst.src).hintName.equals(old)) {
				assert !((Reg) inst.src).isColored();
				((Reg) inst.src).hintName = new_;
				changed = true;
			}
		}
		else if (inst instanceof Ret) { }
		else {
			assert false;
		}
		assert changed;
	}
	
	// get all vregs used in the nasm instruction.
	public static List<Reg> GetVregs (Inst inst) {
		List<Reg> tmp = GetUses(inst);
		tmp.addAll(GetDefs(inst));
		return tmp;
	}
	
	public static String GraphvizRegRenamer(String regName) {
		regName = regName.replace("!", "");
		regName = regName.replace("@", "");
		regName = regName.replace("`", "");
		regName = regName.replace("%", "");
		regName = regName.replace("$", "");
		regName = regName.replace("(", "");
		regName = regName.replace(")", "");
		regName = regName.replace("[", "");
		regName = regName.replace("]", "");
		return regName;
	}
}

