package nasm;

import ir.structure.StringLiteral;
import nasm.asm.*;
import nasm.reg.GlobalMem;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.Set;

import static ir.Utility.unescape;
import static nasm.AsmTranslateVisitor.StringRenamer;
import static nasm.AsmTranslateVisitor.builtin2Extern;

public class AsmPrinter {

	private PrintStream fout = System.out;
	
	/************************** Config ************************/
	public void ConfigOutput (String filename) throws Exception {
		if (filename != null)
			fout = new PrintStream(new FileOutputStream(filename));
	}
	
	/************************ Header print *********************************/
	public void PrintHeaders (Map<String, AsmFunct> functs, Map<String, GlobalMem> globals) {
		for (String s : functs.keySet())
			if (!s.equals("_init_"))
				fout.println("global " + s);
		
		for (GlobalMem gMem : globals.values())
			fout.println("global " + gMem.hintName);
		
		fout.println();
	}
	public void PrintStringLabels (Set<StringLiteral> globals) {
		for (StringLiteral str : globals) {
			StringBuilder db = new StringBuilder();
			String literal = unescape(str.val);
			// eliminate "" in head and tail.
			literal = literal.substring(1, literal.length() - 1);
			for (int i = 0; i < literal.length(); ++i)
				db.append(String.format("%02XH, ", (int) literal.charAt(i)));
			db.append("00H");
			fout.println(StringRenamer(str.name) + ":");
			PrintLine("db", db.toString());
		}
	}
	
	/************************* Extern print ******************************/
	public void PrintExtern () {
		builtin2Extern.values().forEach(x -> fout.println("extern " + x));
		fout.println();
	}
	
	/************************** Section print **********************/
	public enum SECTION {
		TEXT, DATA
	}
	public void PrintSection (SECTION sec) {
		fout.println("SECTION ." + sec.name());
		fout.println();
	}
	
	/******************* Body print *************************/
	public void Print (AsmFunct asmFunct) {
		fout.println(asmFunct.name + ":");
		asmFunct.bbs.forEach(this::Print);
	}
	
	public void Print (AsmBB asmbb) {
		// comment basic block name.
		fout.println(asmbb.hintName + ":");
		asmbb.asms.forEach(this::Print);
		fout.println();
	}
	
	public void Print (Asm asm) {
		asm.AcceptPrint(this);
	}
	
	public void Print (Mov asm) {
		PrintLine("mov", asm.dst.GetText(), asm.src.GetText());
	}
	
	public void Print (Binary asm) {
		if (asm.dst != null)
			PrintLine(asm.op.name(), asm.dst.GetText(), asm.src.GetText());
		else
			PrintLine(asm.op.name(), asm.src.GetText());
	}
	
	public void Print (Jmp asm) {
		PrintLine(asm.jpOp.toString(), asm.label);
	}
	
	public void Print (Push asm) {
		PrintLine("push", asm.src.GetText());
	}
	
	public void Print (Pop asm) {
		PrintLine("pop", asm.dst.GetText());
	}
	
	public void Print (Ret asm) {
		PrintLine("ret");
	}
	
	public void Print (Cmp asm) {
		PrintLine("cmp", asm.dst.GetText(), asm.src.GetText());
	}
	
	public void Print (Call asm) {
		PrintLine("call", asm.functName);
	}
	
	/******************* Utility ***************************/
	private void PrintLine(String inst, String... args) {
		StringBuilder line = new StringBuilder(String.format("\t\t%-8s", inst));
		for (int i = 0; i < args.length; ++i) {
			if (i != 0) line.append(", ");
			line.append(args[i]);
		}
		fout.println(line.toString());
	}
	
}
