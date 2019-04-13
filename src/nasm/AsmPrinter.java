package nasm;

import ir.structure.StringLiteral;
import nasm.asm.*;
import nasm.reg.GlobalMem;

import java.io.*;
import java.util.Map;
import java.util.Set;

import static ir.Utility.unescape;
import static nasm.AsmTranslateVisitor.StringLiteralRenamer;

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
			// print label id. as its label. _S_0:
			fout.println(StringLiteralRenamer(str.name) + ":");
			// print string constant's length 8 bytes in advance.
			PrintLine("dq", Integer.toString(literal.length()));
			for (int i = 0; i < literal.length(); ++i)
				db.append(String.format("%02XH, ", (int) literal.charAt(i)));
			db.append("00H");
			// string label name should be renamed.
			PrintLine("db", db.toString());
		}
	}
	
	/************************* Extern print ******************************/
	public void PrintExtern () {
		fout.println("extern strcmp");
		fout.println("extern snprintf");
		fout.println("extern __stack_chk_fail");
		fout.println("extern strcpy");
		fout.println("extern malloc");
		fout.println("extern strlen");
		fout.println("extern __isoc99_scanf");
		fout.println("extern puts");
		fout.println("extern strcmp");
		fout.println("extern printf");
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
		if (asm.extend) {
			assert asm.dst.GetText().equals("rax");
			PrintLine("mov", "rax", "al");
		}
		else
			PrintLine("mov", asm.dst.GetText(), asm.src.GetText());
	}
	
	public void Print (Oprt asm) {
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
	
	public void Print (Special asm) {
		PrintLine(asm.gossip);
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
	
	
	public void pasteLibFunction() throws IOException {
		File file = new File("lib/lib.asm");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while ((line = br.readLine()) != null) {
			fout.println(line);
		}
	}
}
