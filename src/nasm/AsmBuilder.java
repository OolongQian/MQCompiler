package nasm;

import ir.IrProg;
import ir.structure.BasicBlock;
import ir.structure.IrFunct;
import ir.structure.Reg;
import ir.structure.StringLiteral;
import nasm.reg.GlobalMem;

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
		
		for (IrFunct irFunct : ir.functs.values()) {
			AsmFunct asmfunct = new AsmFunct(irFunct.name, irFunct.regArgs);
			asmFuncts.put(asmfunct.name, asmfunct);
			for (BasicBlock cur = irFunct.bbs.list.Head(); cur != null; cur = cur.next) {
				AsmBB asmCur = new AsmBB(BasicBlockRenamer(cur), asmfunct);
				asmfunct.bbs.add(asmCur);
				translator.ConfigAsmBB(asmCur);
				translator.TranslateQuadList(cur.quads);
			}
			asmfunct.HandleArgs();
		}
		AllocateRegister();
		for (AsmFunct funct : asmFuncts.values()) {
			funct.RewriteBackfill();
			funct.AddPrologue();
			funct.AddEpilogue();
		}
	}
	
	private void AllocateRegister () {
		asmFuncts.values().forEach(allocator::AllocateRegister);
	}
	
	public void Print (AsmPrinter printer) {
		printer.PrintExtern();
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
