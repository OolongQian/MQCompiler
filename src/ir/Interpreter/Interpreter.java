package ir.Interpreter;

import ir.reg.StringLiteral;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ir.builder.Config.TEST;


/**
 * We have a alias problem here. The inner reg type is irrelevant to reg.Reg type for IrBuilder.
 *
 * Using *stringContent to indicate its a string global static literal.
 *
 * If the function's return type is void, it appears to be a global @null in IR.
 *
 * string should be parsed more neatly in printer or builder.
 * */
public class Interpreter {
	
	public static void main(String args[]) {
		Interpreter interpreter = new Interpreter(args[0], null);
		interpreter.Parse();
		interpreter.Execute();
	}
	
	private InputStream fin;
	private Scanner scanner;
	private Scanner stdScanner;
	private PrintStream logger;
	// for debug
	public boolean log = false;
	public boolean err = true;
	public boolean printLineNo = false;
	
	// info get from builder
	private Map<String, StringLiteral> globalStringPool = new HashMap<>();
	
	// global variable and stringLiteral has to be transformed into interpreter compatible type to function here.
	// global contains global variable and string. Have runTime information of gRegs and string.
	private Map<String, Reg> global = new HashMap<>();
	
	private Map<String, Funct> functs = new HashMap<>();
	
	// this is used for parsing, indicating current function context.
	// context used in runTime is called RunCtx.
	private Funct parseCtx;
	private MemModel mem = new MemModel();
	
	/**
	 * Use filepath or in-outStream to construct Interpreter.
	 * */
	public Interpreter(String pathIn, String pathOut) {
		
		if (TEST) {
			try {
				this.fin = new FileInputStream(pathIn);
			} catch (FileNotFoundException e) {
				throw new RuntimeException("interpreter input file not found");
			}
			this.log = false;
			// no logger
			return;
		}
		// check code IO.
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
		stdScanner = new Scanner(System.in);
		
		String line;
		List<String> tokens;
		
		while (scanner.hasNextLine()) {
			line = scanner.nextLine();  lineTrace++;
			tokens = SplitBySpaces(line);
			
			if (line.startsWith("<Function>")) {
				// create Funct instance and parse it.
//				assert tokens.size() == 2;
				String name = tokens.get(1);
				Funct funct = new Funct(name);
				// record args literal info, for matching.
				for (int i = 2; i < tokens.size(); ++i) {
					funct.AddArg(tokens.get(i));
				}
				functs.put(name, funct);
				parseCtx = funct;
				// log
				if (log) logger.println("parse function " + name);
			}
			else if (line.startsWith("<BasicBlock>")) {
				// record basic blocks label by instruction index.
				assert parseCtx != null;
				assert tokens.size() == 2;
				// FIXME : add basic block as inst to be the mark of where I am.
				Inst inst = ParseInst(tokens);
				parseCtx.AddInst(inst);
				String label = tokens.get(1);
				parseCtx.label2Index.put(label, parseCtx.GetIndex());
				// log
				if (log) logger.println("parse basic block " + label);
			}
			else if (line.startsWith("<*>")) {
				StringLiteral gStr;
				if (tokens.size() == 1)
					gStr = new StringLiteral("");
				else
					gStr = new StringLiteral(tokens.get(1));
				globalStringPool.put(gStr.val, gStr);
			}
			else {
				// this IR line is an instruction.
				assert parseCtx != null;
				Inst inst = ParseInst(tokens);
				parseCtx.AddInst(inst);
				// log
				if (log) logger.println("parse inst " + inst.operator);
			}
		}
	}
	
	
	private Inst ParseInst(List<String> tokens) {
		Inst inst = new Inst(lineTrace);
		inst.operator = tokens.get(0);
		
		switch (inst.operator) {
			case "alloca":
				assert tokens.size() == 2;
				inst.dst = tokens.get(1);
				break;
			
			case "malloc":
				assert tokens.size() == 3;
				inst.dst = tokens.get(1);
				inst.src1 = tokens.get(2);
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
			
			case "add": case "sub": case "mul": case "div":
			case "mod": case "shl": case "shr":
			case "and": case "xor": case "or":
			case "land": case "lor":
			case "gt": case "ge": case "lt": case "le": case "eq": case "ne":
			// sub %4 = %3 - 1
				assert tokens.size() == 6;
				inst.dst = tokens.get(1);
				inst.src1 = tokens.get(3);
				inst.src2 = tokens.get(5);
				break;
			
			case "jump":
				// jump &6_after
				assert tokens.size() == 2;
				inst.dst = tokens.get(1);
				break;
				
			case "branch":
				// branch %1 &1_merge, &2_||rhs
				assert tokens.size() == 4;
				inst.dst = tokens.get(1);
				inst.src1 = tokens.get(2);
				inst.src2 = tokens.get(3);
				break;
				
			case "phi":
				inst.dst = tokens.get(1);
				for (int i = 2; i < tokens.size(); ++i) {
					// split by comma
					List<String> optionPair = Arrays.asList(tokens.get(i).trim().split(":"));
					assert optionPair.size() == 2;
					inst.options.put(optionPair.get(0), optionPair.get(1));
				}
				break;
				
			case "<BasicBlock>":
				assert tokens.size() == 2;
				// set dst to be BB's label
				inst.dst = tokens.get(1);
				break;
				
			default:
				throw new RuntimeException("undefined operator");
		}
		return inst;
	}
	
	/************************* Execution ***********************************/
	
	public void Execute() {
		InitGlobal();
		InitString();
		Funct main = functs.get("main");
		RunCtx runCtx = new RunCtx(main, 0);
		
		while (!runCtx.finish) {
			ExecuteInst(runCtx);
		}
		try {
			System.exit(main.retVal.GetValue());
		} catch (NullPointerException e) {
			System.exit(0);
		}
	}
	
	private void InitGlobal() {
		Funct gInit = functs.get("-globalInit");
		RunCtx gInitCtx = new RunCtx(gInit, 0);
		
		while (!gInitCtx.finish) {
			ExecuteInst(gInitCtx);
		}
	}
	
	private void InitString() {
		for (StringLiteral str : globalStringPool.values()) {
			// create global register, storing static global string address.
			Reg strReg = new Reg("*" + str.val);
			// this strReg is bound with memory address, and is immutable.
			strReg.SetAllocd();
			int strAddr = mem.MallocMem(str.val.length() + 4);
			// set strReg's value as its begin address.
			strReg.SetValue(strAddr);
			// put it into globalRegPool, setting * to indicate its a string.
			global.put(strReg.name, strReg);
			
			// memory operation.
			// store string length to the first memory address.
			mem.StoreInt(strAddr, str.val.length());
			// store the subsequent string content to next memory address.
			mem.StoreStr(strAddr + 4, str.val);
		}
	}
	
	
	
	/**
	 * Only interface to get register.
	 * Create new register and get existed register.
	 * */
	private Reg GetReg(RunCtx run, String name) {
		/**
		 * Check if it is a global variable or a string literal.
		 * */
		if (name.startsWith("@") || name.startsWith("*")) {
			if (!global.containsKey(name))
				global.put(name, new Reg(name));
			return global.get(name);
		}
		if (!run.curFunc.regs.containsKey(name))
			run.curFunc.regs.put(name, new Reg(name));
		return run.curFunc.regs.get(name);
	}
	
	/**
	 * Change RunTime context, and record the basic block that jumps from.
	 * */
	private void JumpToBlock(RunCtx run, String jLabel) {
		run.JumpToBB(jLabel);
		int jIndex = run.curFunc.label2Index.get(jLabel);
		run.instIdx = jIndex;
	}
	/**
	 * Set the register to be allocated, bind a memory address to it.
	 *
	 * During execution, don't use ctx but run.
	 * */
	private void ExecuteInst(RunCtx run) {
		// get current index, point to the next index by default.
		// TODO : this is buding
		if (run.instIdx >= run.curFunc.insts.size()) {
			run.finish = true;
			return;
		}
		Inst inst = run.curFunc.insts.get(run.instIdx++);
		if(printLineNo) System.err.println(inst.lineNo);
		switch (inst.operator) {
			case "alloca":
				// alloc in memModel, bind a new local reg to the addr.
				String name = inst.dst;
				Reg local = GetReg(run, name);
				if (local.alloc_d) break;
				int addr = mem.AllocMem(4);
				local.SetValue(addr);
				local.SetAllocd();
				if(err) System.err.println("alloc, space 4, addr " + addr + ", name " + name);
				break;
			
			case "malloc":
				// malloc register value from MemoryModel.
				Reg mallocAddr = GetReg(run, inst.dst);
				Reg mallocByte = GetReg(run, inst.src1);
				int hpAddr = mem.MallocMem(mallocByte.GetValue());
				mallocAddr.SetValue(hpAddr);
				if(err) System.err.println("malloc, " +
								"space " + mallocByte.GetValue() +
								", addr " + hpAddr +
								", name " + inst.dst);
				break;
			
			case "store":
				// NOTE : assume store an integer value by default.
				// NOTE : actually, this is the only condition needed to take care of.
				Reg storeAddr = GetReg(run, inst.dst);
				Reg storeVal = GetReg(run, inst.src1);
				mem.StoreInt(storeAddr.GetValue(), storeVal.GetValue());
				if (log) logger.println("store content: " + Integer.toString(storeVal.GetValue()) +
								" to " + Integer.toString(storeAddr.GetValue()));
				if(err) System.err.println("store, " +
								"space 4" +
								", addr " + storeAddr.GetValue() +
								", name " + inst.dst +
								", content " + storeVal.GetValue() +
								", name " + inst.src1);
				break;
			case "load":
				Reg loadContent = GetReg(run, inst.dst);
				Reg loadAddr = GetReg(run, inst.src1);
				// FIXME : things become nasty when a string is to be loaded, because what we want to load is the string's address, while if
				// FIXME : we get the string from the globalRegs, the address of the head of strings doesn't actually resides in memory. This
				// FIXME : shows my IR has structural problems, and how to fix it depends on how the assembly's realization.
				// NOTE : always assume load 4 bytes, and cast it to int.
				int headAddr = loadAddr.GetValue();
				int loaded;
				if (inst.src1.startsWith("*")) {
					// if load string
					loaded = headAddr;
				} else {
					loaded = mem.LoadInt(headAddr);
				}
				
				loadContent.SetValue(loaded);
				if (log) logger.println("load content: " + Integer.toString(loadContent.GetValue()));
				if(err) System.err.println("load, " +
								"space 4" +
								", addr " + headAddr +
								", name " + inst.src1 +
								", content " + loaded +
								", name " + inst.dst);
				break;
			
			case "call":
				String funcName = inst.funcName;
				Reg ret = GetReg(run, inst.dst);
				List<Reg> args = new LinkedList<>();
				for (String arg : inst.args) {
					args.add(GetReg(run, arg));
				}
				
				if (err) {
					System.err.print("call, name " + funcName + ", args");
					args.forEach(x -> System.err.print(" " + x.name + ":" + x.GetValue()));
					System.err.println(", ret " + ret.name);
				}
				
				if (IsBuiltIn(funcName)) {
					// obtain the return value of the built-in function, and set return register's value.
					Integer retVal = BuiltInFuncExec(funcName, args);
					// set the return value if it isn't null.
					if (!ret.IsNull())
						ret.SetValue(retVal);
				}
				else {
					// get the Funct which is invoked.
					Funct callee = functs.get(funcName);
					callee.PassArgs(args);
					// create a runCtx
					RunCtx calleeCtx = new RunCtx(callee, 0);
					// stack it to the stack.
					run.Invoke(calleeCtx);
					run = calleeCtx;
					// NOTE : this structure is cool: the interpreter's stack structure coincides with the actual program structure.
					while (!run.finish) {
						ExecuteInst(run);
					}
					// get return value.
					try {
						ret.SetValue(run.curFunc.GetRetVal().GetValue());
					} catch (NullPointerException e) {
						ret.SetValue(null);
					}
					run = run.Return();
				}
				break;
			// binary operations are reserved for default
			// every function: runContext must end with a return.
			case "return":
				Reg retVal = GetReg(run, inst.dst);
				run.curFunc.SetRetVal(retVal);
				run.finish = true;
				if (log) logger.println("unhandled return statement");
				break;
				
			case "jump":
				String jLabel = inst.dst;
				JumpToBlock(run, jLabel);
				if(err) System.err.println("jump, " + jLabel);
				break;
				
			case "branch":
				Reg brCond = GetReg(run, inst.dst);
				String ifTrue = inst.src1;
				String ifFalse = inst.src2;
				
				if(err) System.err.println("branch, cond " + brCond.GetValue() +
								", name " + inst.dst +
								", ifTrue " + inst.src1 +
								", ifFalse " + inst.src2);
				
				if (brCond.GetValue() == 1) {
					if(err) System.err.println("branch to " + ifTrue);
					JumpToBlock(run, ifTrue);
				} else {
					if(err) System.err.println("branch to " + ifFalse);
					JumpToBlock(run, ifFalse);
				}
				
				break;
				
			case "phi":
				Reg phiChoice = GetReg(run, inst.dst);
				Map<String, String> opts = inst.options;
				Reg phiOption = GetReg(run, opts.get(run.GetJumpFrom()));
				phiChoice.SetValue(phiOption.GetValue());
				
				if(err) System.err.println("phi, choose " + run.GetJumpFrom() + " val " + phiOption.GetValue() + " name " + opts.get(run.GetJumpFrom()));
				
				break;
				
			case "<BasicBlock>":
				run.JumpToBB(inst.dst);
				break;
				
			default:
				Reg binAns = GetReg(run, inst.dst);
				int binSrc1 = GetReg(run, inst.src1).GetValue();
				int binSrc2 = GetReg(run, inst.src2).GetValue();
				Integer ans = null;
				switch (inst.operator) {
					case "add": ans = binSrc1 + binSrc2; break;
					case "sub": ans = binSrc1 - binSrc2; break;
					case "mul": ans = binSrc1 * binSrc2; break;
					case "div": ans = binSrc1 / binSrc2; break;
					case "mod": ans = binSrc1 % binSrc2; break;
					case "shl": ans = binSrc1 << binSrc2; break;
					case "shr": ans = binSrc1 >> binSrc2; break;
					case "and":
					case "land": ans = binSrc1 & binSrc2; break;
					case "xor": ans = binSrc1 ^ binSrc2; break;
					case "or":
					case "lor": ans = binSrc1 | binSrc2; break;
					case "gt": ans = (binSrc1 > binSrc2) ? 1 : 0; break;
					case "ge": ans = (binSrc1 >= binSrc2) ? 1 : 0; break;
					case "lt": ans = (binSrc1 < binSrc2) ? 1 : 0; break;
					case "le": ans = (binSrc1 <= binSrc2) ? 1 : 0; break;
					case "eq": ans = (binSrc1 == binSrc2) ? 1 : 0; break;
					case "ne": ans = (binSrc1 != binSrc2) ? 1 : 0; break;
				}
				assert ans != null;
				binAns.SetValue(ans);
		}
	}
	
	class RunCtx {
		Funct curFunc;
		int instIdx;
		List<Reg> args;
		// model function calling stack.
		RunCtx caller;
		// store the basic block label jumped from.
		String curBlk;
		String fromBlk;
		boolean finish = false;
		
		public RunCtx(Funct curFunc, int instIdx) {
			this.curFunc = curFunc;
			this.instIdx = instIdx;
			this.caller = null;
		}
		
		// current RunCtx is calling another function as Running context.
		void Invoke(RunCtx callee) {
			callee.caller = this;
		}
		
		void JumpToBB(String label) {
			fromBlk = curBlk;
			curBlk = label;
		}

		String GetJumpFrom() {
			assert fromBlk != null;
			String tmp = fromBlk;
			// TODO : for debug, yes it's ok, since we require phi can only be queried once in each BB.
//			fromBlk = null;
			return tmp;
		}
		
		// terminate running, and return the previous Running context.
		RunCtx Return() {
			return caller;
		}
	}
	
	/**
	 * utilities
	 * */
	private List<String> SplitBySpaces(String line) {
		return Arrays.asList(line.trim().split(" +"));
	}
	
	private boolean IsBuiltIn(String functName) {
		return functName.startsWith("~");
	}
	
	private Integer BuiltInFuncExec(String markName, List<Reg> args) {
		// markName = ~name
		String funct = markName.substring(1);
		// simulate built-in functions
		switch (funct) {
			// stand alone built-in functions.
			case "print": case "println":
				assert args.size() == 1;
				int printAddr = args.get(0).GetValue();
				String printStr = mem.LoadStr(printAddr);
				if (funct.equals("print"))
					System.out.print(printStr);
				else
					System.out.println(printStr);
				return null;
			case "getString":
				// read a line of string from stdin, malloc mem space, store it, and return headAddr.
				assert args.size() == 0;
				if(err) System.err.println("getString()...");
				String getStr = scanner.nextLine();
				int getStrAddr = mem.MallocMem(getStr.length() + 4);
				mem.StoreInt(getStrAddr, getStr.length());
				mem.StoreStr(getStrAddr + 4, getStr);
				return getStrAddr;
			case "getInt":
				// getInt and return.
				assert args.size() == 0;
				if(err) System.err.println("getInt()...");
				int getInt = stdScanner.nextInt();
				// skip \n
				stdScanner.nextLine();
				return getInt;
			case "toString":
				assert args.size() == 1;
				// parse an integer to string, and store it in heap, return headAddr.
				int srcVal = args.get(0).GetValue();
				String toStr = Integer.toString(srcVal);
				int toStrAddr = mem.MallocMem(toStr.length() + 4);
				mem.StoreInt(toStrAddr, toStr.length());
				mem.StoreStr(toStrAddr + 4, toStr);
				return toStrAddr;
			// method for built-in types.
			case "-string#length":
				assert args.size() == 1;
				// arg is mem addr, access that piece of address and load an int out.
				int strAddr = args.get(0).GetValue();
				int strLen = mem.LoadInt(strAddr);
				return strLen;
			case "-string#substring":
				// cut the original string from left to right, make another space to store it, and return its head addr.
				// NOTE : I've found a brilliant idea to implement string. We need two hop, and then we no long need to
				// NOTE : malloc and copy for subStrings, just record the headAddr and strLength.
				assert args.size() == 3;
				int originStrAddr = args.get(0).GetValue();
				int left = args.get(1).GetValue();
				int right = args.get(2).GetValue();
				String originStr = mem.LoadStr(originStrAddr);
				String substrStr = originStr.substring(left, right);
				int substrAddr = mem.MallocMem(substrStr.length() + 4);
				mem.StoreInt(substrAddr, substrStr.length());
				mem.StoreStr(substrAddr + 4, substrStr);
				return substrAddr;
			case "-string#parseInt":
				assert args.size() == 1;
				int parseAddr = args.get(0).GetValue();
				String parseStr = mem.LoadStr(parseAddr);
				// get the first chunk
				parseStr = SplitBySpaces(parseStr).get(0);
				return Integer.parseInt(parseStr);
			case "-string#ord":
				assert args.size() == 2;
				int ordAddr = args.get(0).GetValue();
				int ordIndex = args.get(1).GetValue();
				// go over string length, and ord chars
				char ch = (char) (byte) mem.LoadMem(ordAddr + 4 + ordIndex);
				int ordAscii = Integer.valueOf(ch);
				return ordAscii;
			case "--array#size":
				assert args.size() == 1;
				int arrAddr = args.get(0).GetValue();
				// load arrayLen from headAddr
				int arrSize = mem.LoadInt(arrAddr);
				return arrSize;
		}
		
		return null;
	}
	
	/**
	 * These classes contain literal information used solely by IR interpreter.
	 * They are inference information.
	 * */
	public class Inst {
		String operator;
		String dst;  // used as `cond` for `br` // return value for ret.
		String src1;
		String src2;
		
		List<String> args = new LinkedList<>();
		String funcName;
		// bbName vs valOption
		Map<String, String> options = new HashMap<>();
		
		int lineNo;
		
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
		Reg retVal = null;
		
		// record arguments' literal values.
		List<Reg> args = new LinkedList<>();
		
		Funct(String name) {
			this.name = name;
		}
		
		int GetIndex() {
			return insts.size();
		}
		
		void AddInst(Inst inst) {
			insts.add(inst);
		}
		
		void SetRetVal(Reg val) {
			retVal = val;
		}
		
		Reg GetRetVal() {
			if (retVal == null)
				throw new RuntimeException("null return value");
			return retVal;
		}
		
		/**
		 * Pass in a list of regs to be the arguments passed in,
		 * feed them into literal regs in suitable positions.
		 * put them into the HashMap.
		 * */
		void PassArgs(List<Reg> invokeArgs) {
			assert this.args.size() == invokeArgs.size();
			for (int i = 0; i < args.size(); ++i) {
				String nameTranslation = args.get(i).name;
				assert regs.containsKey(nameTranslation);
				// assign values to regs, it's args passing.
				Integer argVal = invokeArgs.get(i).GetValue();
				regs.get(nameTranslation).SetValue(argVal);
			}
		}
		
		void AddArg(String argLit) {
			// create regs based on its name (bind value to string), record a argLit list to maintain linear order.
			Reg reg = new Reg(argLit);
			regs.put(argLit, reg);
			args.add(new Reg(argLit));
		}
	}
	
	/**
	 * Used to distinguish immediate value.
	 * */
	public boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if( !isNum.matches() ){
			return false;
		}
		return true;
	}
	
	private class Reg {
		String name;
		private Integer val;
		boolean alloc_d;
		boolean valSet;
		
		Reg(String name) {
			this.name = name;
			valSet = false;
			alloc_d = false;
			// for immediate value.
			if (isNumeric(name)) {
				SetValue(Integer.parseInt(name));
			}
			// set fow null value.
			else if (name.equals("@null")) {
				SetValue(null);
			}
		}
		
		/**
		 * Mark the register to be allocated
		 * */
		void SetAllocd() {
			alloc_d = true;
		}
		
		/**
		 * Null value can be set as value.
		 * */
		void SetValue(Integer val) {
//			assert !valSet || this.val.equals(val);
			valSet = true;
			this.val = val;
		}
		
		int GetValue() {
			assert valSet;
			return val;
		}
		
		boolean IsNull() {
			return valSet && val == null;
		}
	}
	
	private static class MemModel {
		static int sp = 0;
		static int hp = 4095;
		
		Map<Integer, Byte> mem = new HashMap<>();
		
		private Byte LoadMem(int addr) {
			return mem.get(addr);
		}
		
		/**
		 * This method loads a string.
		 * It assumes that an integer indicating string length resides right at
		 * the memory of addr, it reads the length and based on which loads the
		 * number of chars to make up of a string.
		 * */
		String LoadStr(int addr) {
			int strLen = LoadInt(addr);
			byte[] bytes = new byte[strLen];
			for (int i = 0; i < strLen; ++i) {
				bytes[i] = LoadMem(addr + 4 + i);
			}
			return new String(bytes);
		}
		
		int LoadInt(int addr) {
			int ret = 0;
			// note that headAddr is lower address.
			for (int i = 0; i < 4; ++i) {
				byte b = LoadMem(addr + i);
				ret = ret | ((b & 0xFF) << 4 * i);
			}
//			if(err) System.err.println("load int " + ret + " from " + addr);
			
			return ret;
		}
		
		private void StoreMem(int addr, byte byte_) {
			mem.put(addr, byte_);
		}
		
		void StoreInt(int addr, int val) {
//			if(err) System.err.println("store int " + val + " to " + addr);
			// NOTE : identical to LoadInt, headAddr is lower address.
			for (int i = 0; i < 4; ++i) {
				// take the value of 4i bytes, counting from lower.
				byte b = (byte)((val >> (4 * i)) & 0xFF);
				StoreMem(addr + i, b);
			}
		}
		
		/**
		 * Store a string with initial address of storage to be 'addr'.
		 * */
		void StoreStr(int addr, String str) {
			byte[] bytes = str.getBytes();
			for (int i = 0; i < bytes.length; ++i) {
				StoreMem(addr + i, bytes[i]);
			}
		}
		/**
		 * safely initialize stack content to be all 0.
		 * */
		int AllocMem(int size_) {
			int headAddr = sp;
			sp += size_;
			for (int i = headAddr; i < sp; ++i) {
				assert !mem.containsKey(i);
				mem.put(i, (byte) 0);
			}
			assert sp <= hp;
//			if(err) System.err.println("alloca from " + headAddr + " to " + sp);
			return headAddr;
		}
		/**
		 * safely initialize heap content to be all 0.
		 * */
		int MallocMem(int size_) {
			int headAddr = hp;
			// hp points to the first non-allocated address
			hp -= size_;
			assert sp <= hp;
			for (int i = headAddr; i > hp; --i) {
				assert !mem.containsKey(i);
				mem.put(i, (byte) 0);
			}
			// return the first allocated address.
//			if(err) System.err.println("malloc from " + (hp + 1) + " to " + headAddr);
			return hp + 1;
		}
	}
}
