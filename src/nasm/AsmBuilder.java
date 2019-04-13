package nasm;

import ir.IrProg;
import ir.structure.BasicBlock;
import ir.structure.IrFunct;
import ir.structure.Reg;
import ir.structure.StringLiteral;
import nasm.reg.GlobalMem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static nasm.AsmTranslateVisitor.BasicBlockRenamer;
import static nasm.AsmTranslateVisitor.GlobalRenamer;

/**
 * Translate IR to NASM assembly with infinite registers.
 * */
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
			AsmFunct asmfunct = new AsmFunct(irFunct.name, irFunct.regArgs);
			asmFuncts.put(asmfunct.name, asmfunct);
			for (BasicBlock cur = irFunct.bbs.list.Head(); cur != null; cur = cur.next) {
				AsmBB asmCur = new AsmBB(BasicBlockRenamer(cur), asmfunct);
				asmfunct.bbs.add(asmCur);
				translator.ConfigAsmBB(asmCur);
				translator.TranslateQuadList(cur.quads);
			}
			// move args to temp registers. put things to register allocation.
			asmfunct.MovAllocateArgs();
			
			allocator.AllocateRegister(asmfunct);
			asmfunct.Backfill();
			asmfunct.AddPrologue();
		}
	}
	
	public void Print (AsmPrinter printer) throws Exception {
		printer.PrintExtern();
		printer.pasteLibFunction();
		printer.PrintHeaders(asmFuncts, globalMems);
		printer.PrintSection(AsmPrinter.SECTION.TEXT);
		for (AsmFunct asmFunct : asmFuncts.values()) {
			if (asmFunct.name.equals("_init_")) continue;
			printer.Print(asmFunct);
		}
		printer.PrintSection(AsmPrinter.SECTION.DATA);
		printer.PrintStringLabels(strings);
	}

}
