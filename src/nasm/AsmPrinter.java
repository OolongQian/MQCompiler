package nasm;

import ir.structure.StringLiteral;
import nasm.inst.*;
import nasm.reg.GlobalMem;
import nasm.reg.PhysicalReg;
import nasm.reg.Reg;
import nasm.reg.StackMem;

import java.io.*;
import java.util.Map;
import java.util.Set;

import static ir.Utility.unescape;
import static nasm.Utils.FunctRenamer;
import static nasm.Utils.StringLiteralRenamer;

public class AsmPrinter {

	protected PrintStream fout = System.out;
	
	/************************** Config ************************/
	public void ConfigOutput (String filename) throws Exception {
		if (filename != null)
			fout = new PrintStream(new FileOutputStream(filename));
	}
	
	/************************ Header print *********************************/
	public void PrintHeaders (Map<String, AsmFunct> functs, Map<String, GlobalMem> globals) {
		for (String s : functs.keySet())
			if (!s.equals("_init_"))
				fout.println("global " + FunctRenamer(s));
		
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
	
	public void PrintGVar (Map<String, GlobalMem> globals) {
		for (GlobalMem g : globals.values()) {
			if (!g.isString) {
				fout.println(g.hintName + ":");
				PrintLine("resq", "1");
			}
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
		text, data, bss
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
		asmbb.insts.forEach(this::Print);
		fout.println();
	}
	
	public void Print (Inst inst) {
		inst.AcceptPrint(this);
	}
	
	public void Print (Mov asm) {
		if (asm.extend) {
			assert asm.dst.GetText().equals("rax");
			PrintLine("movzx", "rax", "al");
		}
		else
			PrintLine("mov", asm.dst.GetText(), asm.src.GetText());
	}
	
	public void Print (Oprt asm) {
		if (asm.dst == null)
			PrintLine(asm.op.name(), asm.src.GetText());
		else if (asm.src == null)
			PrintLine(asm.op.name(), asm.dst.GetText());
		else
			PrintLine(asm.op.name(), asm.dst.GetText(), asm.src.GetText());
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
		// if jump directly, no need to do this routine.
		if (!asm.jmp) {
			String lowerReg = PhysicalReg.wide2lower.get(asm.flagReg.color.phyReg);
			String phyReg = asm.flagReg.color.phyReg.name();
			PrintLine(asm.Cmp2Set(), lowerReg);
			PrintLine("movzx", phyReg, lowerReg);
		}
	}
	
	public void Print (Call asm) {
		PrintLine("call", asm.functName);
	}
	
	// can load from regs or globals
	public void Print (Load asm) {
		if (asm.src instanceof Reg)
			PrintLine("mov", asm.dst.GetText(), String.format("[%s]", asm.src.GetText()));
		else
			PrintLine("mov", asm.dst.GetText(), asm.src.GetText());
	}
	
	// can store into usual reg or globals.
	public void Print (Store asm) {
		if (asm.dst instanceof Reg)
			PrintLine("mov", String.format("qword [%s]", asm.dst.GetText()), asm.src.GetText());
		else
			PrintLine("mov", asm.dst.GetText(), asm.src.GetText());
	}
	
	public void Print (Lea asm) {
		PrintLine("lea", asm.dst.GetText(), ((StackMem) asm.src).GetLeaText());
	}
	
	public void Print (Msg asm) {
		fout.print("MSG\t " + asm.msg);
	}
	
	/******************* Utility ***************************/
	protected void PrintLine(String inst, String... args) {
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
