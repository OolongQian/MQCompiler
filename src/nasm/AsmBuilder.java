package nasm;

import com.sun.scenario.effect.impl.state.LinearConvolveKernel;
import ir.IrProg;
import ir.quad.Binary;
import ir.quad.Quad;
import ir.structure.BasicBlock;
import ir.structure.IrFunct;
import ir.structure.Reg;
import ir.structure.StringLiteral;
import nasm.allocate.AsmRegAllocator;
import nasm.inst.*;
import nasm.reg.GlobalMem;
import nasm.reg.Imm;

import java.util.*;

import static config.Config.ALLOCAREGS;
import static config.Config.COMMENTNASM;
import static nasm.Utils.*;
import static nasm.inst.Oprt.Op.ADD;
import static nasm.inst.Oprt.Op.INC;
import static nasm.inst.Oprt.Op.SUB;
import static nasm.reg.PhysicalReg.PhyRegType.rsp;

/** Translate IR to NASM assembly with infinite registers.
 * This AsmBuilder will contain comments, delete them later. */
public class AsmBuilder {
	public Map<String, AsmFunct> asmFuncts = new HashMap<>();
	
	// GlobalMem consists of globals and string literals.
	// map @a to _G_a.
	public Map<String, GlobalMem> globalMems = new HashMap<>();
	
	public Set<StringLiteral> strings = new HashSet<>();
	
	public AsmTranslateVisitor translator = new AsmTranslateVisitor();
	public AsmRegAllocator allocator = new AsmRegAllocator();
	
	
	public void TranslateIr(IrProg ir) {
		// config
		translator.ConfigTranslateContext(globalMems);
		strings.addAll(ir.stringPool.values());
		for (Reg g : ir.globals.values())
			globalMems.put(g.name, new GlobalMem(GlobalRenamer(g.name)));
		
		// build nasm. All regs are virtual by now, except global, string, allocated.
		// allocated has already been stackMem, but offset hasn't been set.
		for (IrFunct irFunct : ir.functs.values()) {
			
			// create and config asmfunct
			AsmFunct asmfunct = new AsmFunct(FunctRenamer(irFunct.name), irFunct.regArgs);
			asmFuncts.put(asmfunct.name, asmfunct);
			translator.ConfigAsmFunct(asmfunct);
			
			// create and translate each basic block.
			for (BasicBlock cur = irFunct.bbs.list.Head(); cur != null; cur = cur.next) {
				AsmBB asmCur = new AsmBB(BasicBlockRenamer(cur), asmfunct, cur.loopLevel);
				asmfunct.bbs.add(asmCur);
				translator.ConfigAsmBB(asmCur);
				translator.TranslateQuadList(cur.quads);
			}
			
			// maintain cfg.
			asmfunct.InitCFG(irFunct.bbs.cfg);
			
			// do technical function routine.
			// if asmFunct is main, call _init_ first.
			if (asmfunct.name.equals("main")) {
				int cnt = 0;
				AsmBB cur = asmfunct.bbs.get(0);
				cur.insts.add(cnt++, new Oprt(GetPReg(rsp), new Imm(0), cur, SUB));
				cur.insts.add(cnt++, new Msg(cur, "BEGIN args pass\n"));
				cur.insts.add(cnt++, new Msg(cur, "END args pass\n"));
				cur.insts.add(cnt++, new Call(cur, "_init_", Boolean.FALSE));
				cur.insts.add(cnt++, new Oprt(GetPReg(rsp), new Imm(0), cur, ADD));
			}

			translator.ArgsVirtualize();
			translator.x86_FormCheck();
			translator.CalleeSave();
			if (ALLOCAREGS)
				allocator.AllocateRegister(asmfunct);
			asmfunct.CalcStackOffset();
			translator.AddPrologue();
			translator.AddEpilogue();
			if (!COMMENTNASM)
				asmFuncts.values().forEach(Utils::DelMsg);
		}
	}
	
	public void Print (AsmPrinter printer) throws Exception {
		printer.PrintExtern();
		printer.PrintHeaders(asmFuncts, globalMems);
		printer.PrintSection(AsmPrinter.SECTION.text);
		for (AsmFunct asmFunct : asmFuncts.values()) {
			printer.Print(asmFunct);
		}
		printer.PrintSection(AsmPrinter.SECTION.data);
		printer.PrintStringLabels(strings);
		printer.PrintSection(AsmPrinter.SECTION.bss);
		printer.PrintGVar(globalMems);
		printer.pasteLibFunction();
	}
	
	public void FallAndSweep() {
		// clear all unnecessary bb.
		for (AsmFunct asmFunct : asmFuncts.values()) {
			for (int i = 0; i < asmFunct.bbs.size(); ++i) {
				AsmBB bb = asmFunct.bbs.get(i);
				
				if (bb.insts.size() == 1 && bb.insts.get(0) instanceof Jmp) {
					Jmp last = (Jmp) bb.insts.get(0);
					asmFunct.bbs.remove(i--);
					ReplaceLabel(asmFunct, bb.hintName, last.label);
				}
			}
		}
		
		// add fall through.
		for (AsmFunct asmFunct : asmFuncts.values()) {
			for (int i = 0; i < asmFunct.bbs.size(); ++i) {
				AsmBB bb = asmFunct.bbs.get(i);
				Inst last = bb.insts.get(bb.insts.size() - 1);
				assert last instanceof Jmp || last instanceof Ret;
				if (last instanceof Jmp) {
					// clear fall through jump
					Jmp jmp = (Jmp) last;
					if (i + 1 < asmFunct.bbs.size() &&
									asmFunct.bbs.get(i + 1).hintName.equals(jmp.label)) {
						bb.insts.remove(last);
					}
				}
			}
		}
		IncNotAdd();
	}
	
	private void ReplaceLabel(AsmFunct asmFunct, String oldLabel, String newLabel) {
		for (AsmBB asmBB : asmFunct.bbs) {
			List<Jmp> jmps = new LinkedList<>();
			
			int j = asmBB.insts.size() - 1;
			while (j >= 0 && asmBB.insts.get(j) instanceof Jmp)
				jmps.add((Jmp) asmBB.insts.get(j--));
			
			for (Jmp jp : jmps) {
				if (jp.label.equals(oldLabel))
					jp.label = newLabel;
			}
		}
	}
	
	// use inc instead of add.
	private void IncNotAdd() {
		for (AsmFunct asmFunct : asmFuncts.values()) {
			for (int i = 0; i < asmFunct.bbs.size(); ++i) {
				AsmBB bb = asmFunct.bbs.get(i);
				for (int j = 0; j < bb.insts.size(); ++j) {
					Inst inst = bb.insts.get(j);
					if (inst instanceof Oprt && ((Oprt) inst).op == ADD && inst.src instanceof Imm && ((Imm) inst.src).imm == 1) {
						bb.insts.set(j, new Oprt(inst.dst, bb, INC));
					}
				}
			}
		}
	}
}
