package ir.Interpreter;

import ir.reg.GlobalReg;
import ir.reg.LocalReg;
import ir.reg.StringLiteral;

import java.io.*;
import java.util.*;

public class Interpreter {
	private InputStream fin;
	Scanner scanner;
	private PrintStream logger;
	public boolean log = true;
	
	private List<GlobalReg> globalRegs = new LinkedList<>();
	private Map<String, StringLiteral> globalStringPool = new HashMap<>();
	private Map<String, Funct> functs = new HashMap<>();
	private Funct ctx;  // current function context.
	private MemModel mem = new MemModel();
	/**
	 * Use filepath or in-outStream to construct Interpreter.
	 * */
	public Interpreter(String pathIn, String pathOut) {
		if (pathIn == null) {
			this.fin = System.in;
			return;
		}
		try {
			this.fin = new FileInputStream(pathIn);
		}
		catch (FileNotFoundException e) {
			throw new RuntimeException("interpreter input file not found");
		}
		
		if (pathOut == null) {
			this.logger = System.out;
			return;
		}
		try {
			this.logger = new PrintStream(new FileOutputStream(pathOut));
		}
		catch (FileNotFoundException e) {
			throw new RuntimeException("interpreter output file not found");
		}
	}
	
	// record line number during parsing.
	private int lineTrace = 0;
	public void Parse() {
		// create scanner.
		scanner = new Scanner(fin);
		
		String line;
		List<String> tokens;
		
		while (scanner.hasNextLine()) {
			line = scanner.nextLine();  lineTrace++;
			tokens = SplitBySpaces(line);
			
			if (line.startsWith("<Function>")) {
				// create Funct instance and parse it.
				assert tokens.size() == 2;
				String name = tokens.get(1);
				Funct funct = new Funct(name);
				functs.put(name, funct);
				ctx = funct;
				// log
				if (log) logger.println("parse function " + name);
			}
			else if (line.startsWith("<BasicBlock>")) {
				// record basic blocks label by instruction index.
				assert ctx != null;
				assert tokens.size() == 2;
				String label = tokens.get(1);
				ctx.label2Index.put(label, ctx.GetIndex());
				// log
				if (log) logger.println("parse basic block " + label);
			}
			else {
				// this IR line is an instruction.
				assert ctx != null;
				Inst inst = ParseInst(tokens);
				ctx.AddInst(inst);
				// log
				if (log) logger.println("parse inst " + inst.operator);
			}
		}
	}
	
	public void Execute() {
		InitGlobal();
		PlaceString();
		Funct main = functs.get("main");
		Inst curInst = main.insts.get(0);
		while (curInst != null) {
			curInst = ExecuteInst(curInst);
		}
	}
	
	private void InitGlobal() {
		;
	}
	
	private void PlaceString() {
		;
	}
	
	private Inst ExecuteInst(Inst inst) {
		switch (inst.operator) {
			case "alloca":
				// alloc in memModel, bind a new local reg to the addr.
				int addr = mem.AllocMem(4);
				String name = inst.dst;
				Reg local = new Reg(name, addr, true);
				ctx.regs.put(name, local);
				break;
			case "store":
				Reg storeAddr = ctx.regs.get(inst.dst);
				Reg storeVal = ctx.regs.get(inst.src1);
				mem.StoreMem(storeAddr.val, (byte) storeVal.val);
				break;
		}
		return new Inst(10);
	}
	
	
	
	private Inst ParseInst(List<String> tokens) {
		Inst inst = new Inst(lineTrace);
		inst.operator = tokens.get(0);
		
		switch (inst.operator) {
			case "alloca":
				assert tokens.size() == 2;
				inst.dst = tokens.get(1);
				break;
			case "store":
				assert tokens.size() == 3;
				inst.dst = tokens.get(1);   // addr
				inst.src1 = tokens.get(2);  // val
				break;
			case "load":
				assert tokens.size() == 3;
				inst.dst = tokens.get(1);   // val loaded
				inst.src1 = tokens.get(2);  // addr to load
				break;
			case "call":
				inst.funcName = tokens.get(1);
				inst.dst = tokens.get(2);   // funct return value.
				for (int i = 3; i < tokens.size(); ++i) {
					inst.args.add(tokens.get(i));
				}
				break;
			case "return":
				assert tokens.size() == 2;
				inst.dst = tokens.get(1);   // value to be returned.
				break;
			default:
				throw new RuntimeException("undefined operator");
		}
		return inst;
	}
	
	/**
	 * utilities
	 * */
	private List<String> SplitBySpaces(String line) {
		return Arrays.asList(line.trim().split(" +"));
	}
	
	/**
	 * These classes contain literal information used solely by IR interpreter.
	 * They are inference information.
	 * */
	public class Inst {
		String operator;
		String dst;  // used as `cond` for `br`
		String src1;
		String src2;
		
		List<String> args = new LinkedList<>();
		int lineNo;
		String funcName;
		
		public Inst(int lineNo) {
			this.lineNo = lineNo;
		}
	}
	
	private class Funct {
		String name;
		List<Inst> insts = new ArrayList<>();
		// trace basicBlock instruction via label.
		Map<String, Integer> label2Index = new HashMap<>();
		// trace tmp register's content value.
//		Map<String, TmpReg> tmpRegs = new HashMap<>();
		// trace bound local registers' memory address.
		// NOTE : above are wrong, tmp regs can also contain addr.
		Map<String, Reg> regs = new HashMap<>();
		
		Funct(String name) {
			this.name = name;
		}
		
		int GetIndex() {
			return insts.size();
		}
		
		void AddInst(Inst inst) {
			insts.add(inst);
		}
	}
	
	private class Reg {
		String name;
		int val;
		boolean alloc_d;
		
		public Reg(String name, int val, boolean alloc_d) {
			this.name = name;
			this.val = val;
			this.alloc_d = alloc_d;
		}
	}
	
	private static class MemModel {
		static int sp = 0;
		List<Byte> mem = new ArrayList<>();
		
		Byte LoadMem(int addr) {
			return mem.get(addr);
		}
		
		void StoreMem(int addr, Byte val) {
			mem.set(addr, val);
		}
		
		int AllocMem(int size_) {
			int addr = sp;
			sp += size_;
			return addr;
		}
	}
}
