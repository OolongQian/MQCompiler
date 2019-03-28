package ir.interpreter;

import ir.interpreter.execute.MemModel;
import ir.interpreter.execute.Reg;
import ir.interpreter.execute.RunCtx;
import ir.interpreter.parse.Funct;
import ir.interpreter.parse.Inst;
import ir.structure.StringLiteral;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.io.*;
import java.util.*;

import static ir.Config.LINENO;
import static ir.Config.LOG;
import static ir.Utility.unescape;

public class Interpreter {

	public static void main(String args[]) {
		// use input file name as input stream
		Interpreter interpreter = new Interpreter();
		interpreter.ConfigIO(args[0], null);
		
		interpreter.Parse();
		interpreter.Execute();
	}
	
	/**
	 * Used for read in IR code
	 * */
	private InputStream fin;
	private Scanner scanner;
	
	/**
	 * Used for getInt and print
	 * */
	private Scanner stdScanner;
	private PrintStream stdout;
	
	/**
	 * printStream for log
	 * */
	private PrintStream logger;
	
	
	/**
	 * record line number during parsing.
	 * */
	private int lineTrace = 0;
	
	/**
	 * hold all parsed functs' literal content.
	 * */
	private Map<String, Funct> functs = new HashMap<>();
	
	/**
	 * bind stringLiteral to addr.
	 * */
	private Map<Integer, String> stringPool = new HashMap<>();
	private List<String> globalPool = new LinkedList<>();
	
	public Interpreter() { }
	
	/**
	 * Config input filename as inputstream
	 * */
	public void ConfigIO(String inputFilename, String logFilename) {
		// input file
		if (inputFilename != null) {
			try {
				fin = new FileInputStream(inputFilename);
			} catch (FileNotFoundException e) {
				throw new RuntimeException("IO");
			}
		} else {
			fin = System.in;
		}
		
		// output
		stdout = System.out;
		
		// logger
		if (logFilename != null) {
			try {
				logger = new PrintStream(new FileOutputStream(logFilename));
			} catch (FileNotFoundException e) {
				throw new RuntimeException("IO");
			}
		} else {
			logger = System.out;
		}
		
		// scanner
		scanner = new Scanner(fin);
		stdScanner = new Scanner(System.in);
	}
	public void ConfigString(Map<String, StringLiteral> irStringRecord) {
		irStringRecord.values().forEach(x -> stringPool.put(x.id, x.val));
	}
	/**
	 * parse the IR code to construct a literal IR data structure.
	 * */
	public void Parse() {
		String line;
		List<String> tokens;
		Funct parseCtx = null;
		
		while (scanner.hasNextLine()) {
			line = scanner.nextLine();  lineTrace++;
			if (line.equals("")) continue;
			
			tokens = SplitBySpaces(line);
			
			if (line.startsWith("<Function>")) {
				String name = tokens.get(1);
				Funct funct = new Funct(name);
				
				for (int i = 2; i < tokens.size(); ++i) {
					funct.AddFormalArg(tokens.get(i));
				}
				
				functs.put(name, funct);
				parseCtx = funct;
			}
			else if (line.startsWith("<BasicBlock>")) {
				assert parseCtx != null;
				assert tokens.size() == 2;
				// generate a BB label inst to be a marker.
				String label = tokens.get(1);
				Inst inst = new Inst(lineTrace);
				inst.operator = tokens.get(0);
				inst.dst = label;
				
				parseCtx.AddInst(inst);
				parseCtx.label2Index.put(label, parseCtx.GetNextIndex());
			}
			else if (line.startsWith("<*>")) {
				// TODO : collect global strings.
				int id = Integer.parseInt(tokens.get(1));
				String gStr;
				int left = line.indexOf('"');
				int right = line.lastIndexOf('"');
				if (left != right)
					gStr = line.substring(left + 1, right);
				else {
					gStr = line.substring(left + 1) + '\n';
					// check whether we have \n.
					String tmp;
					tmp = scanner.nextLine();
					while (tmp.indexOf('"') == -1) {
						gStr += tmp + '\n';
						tmp = scanner.nextLine();
					}
					gStr += tmp.substring(0, tmp.indexOf('"'));
				}
				stringPool.put(id, gStr);
			}
			else if (line.startsWith("<@>")) {
				assert tokens.size() == 2;
				globalPool.add(tokens.get(1));
			}
			else {
				// this line is an instruction
				assert parseCtx != null;
				Inst inst = ParseInst(tokens, line);
				parseCtx.AddInst(inst);
			}
		}
	}
	
	/**
	 * Parse a line of instruction
	 * */
	private Inst ParseInst(List<String> tokens, String line) {
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
			case "concat":
				// sub %4 = %3 - 1
				assert tokens.size() == 6;
				inst.dst = tokens.get(1);
				inst.src1 = tokens.get(3);
				inst.src2 = tokens.get(5);
				break;
			
			case "neg": case "bitnot":
				assert tokens.size() == 5;
				inst.dst = tokens.get(1);
				inst.src1 = tokens.get(4);
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
			
			case "move":
				assert tokens.size() == 4;
				inst.dst = tokens.get(1);
				inst.src1 = tokens.get(3);
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
	private MemModel mem = new MemModel();
	
	private Map<String, Reg> global = new HashMap<>();
	
	private Map<Reg, Reg> phiParalCache = new HashMap<>();
	private boolean phiCached = false;
	
	public void Execute() {
		InitString();
		InitGlobal();
		RunMain();
	}
	
	/**
	 * Init global is executing a pseudo init function.
	 * The corresponding Reg is automatically assigned because
	 * GetReg function returns global reg or run-time reg respectively.
	 * s : @gName
	 * */
	private void InitGlobal() {
		for (String s : globalPool) {
			Reg gReg = new Reg(s);
			gReg.SetAllocd();
			gReg.SetValue(mem.AllocMem(4));
			global.put(s, gReg);
		}
		RunCtx gInitCtx = new RunCtx(functs.get("_init_"));
		while (!gInitCtx.terminate) {
			ExecuteInst(gInitCtx);
		}
	}
	
	/**
	 * Use string's id to lookup these strings.
	 * */
	private void InitString() {
		for (int id : stringPool.keySet()) {
			String s = stringPool.get(id);
			Reg strReg = new Reg("*" + Integer.toString(id));
			strReg.SetAllocd();
			int strAddr = mem.MallocMem(s.length() + 4);
			mem.StoreInt(strAddr, s.length());
			mem.StoreStr(strAddr + 4, s);
			
			strReg.SetValue(strAddr);
			global.put(strReg.name, strReg);
		}
	}
	
	private void RunMain() {
		RunCtx mainCtx = new RunCtx(functs.get("main"));
		while (!mainCtx.terminate) {
			ExecuteInst(mainCtx);
		}
		
		System.exit(mainCtx.GetRetVal().GetValue());
	}
	
	/**
	 * Key method, execution of a thread, execute one instruction
	 * at a time.
	 * */
	private void ExecuteInst(RunCtx ctx) {
		if (ctx.curInst >= ctx.func.insts.size()) {
			ctx.terminate = true;
			return;
		}
		
		Inst inst = ctx.func.insts.get(ctx.curInst++);
		if(LINENO) System.err.println(inst.lineNo);
		if (LOG) {
			System.err.println(inst.lineNo);
			ctx.PrintDefUse(mem);
		}
		
		// if phi nodes have been evaluated already, assign cached phi nodes results in parallel.
		if (!inst.operator.equals("phi") && phiCached) {
			for (Reg phiReg : phiParalCache.keySet()) {
				phiReg.SetValue(phiParalCache.get(phiReg).GetValue());
			}
			phiParalCache.clear();
			phiCached = false;
		}
		switch (inst.operator) {
			case "alloca":
				// alloc in memModel, bind a new local reg to the addr.
				String name = inst.dst;
				Reg local = GetReg(ctx, name);
				if (local.alloc_d) break;
				int addr = mem.AllocMem(4);
				local.SetValue(addr);
				local.SetAllocd();
				if(LOG) System.err.println("alloc, space 4, addr " + addr + ", name " + name);
				break;
			
			case "malloc":
				// malloc register value from MemoryModel.
				Reg mallocAddr = GetReg(ctx, inst.dst);
				Reg mallocByte = GetReg(ctx, inst.src1);
				int hpAddr = mem.MallocMem(mallocByte.GetValue());
				mallocAddr.SetValue(hpAddr);
				if(LOG) System.err.println("malloc, " +
								"space " + mallocByte.GetValue() +
								", addr " + hpAddr +
								", name " + inst.dst);
				break;
			
			case "store":
				// NOTE : assume store an integer value by default.
				// NOTE : actually, this is the only condition needed to take care of.
				Reg storeAddr = GetReg(ctx, inst.dst);
				Reg storeVal = GetReg(ctx, inst.src1);
				if (!storeVal.IsNull()) mem.StoreInt(storeAddr.GetValue(), storeVal.GetValue());
				if(LOG) logger.println("store content: " + Integer.toString(storeVal.GetValue()) +
								" to " + Integer.toString(storeAddr.GetValue()));
				if(LOG) System.err.println("store, " +
								"space 4" +
								", addr " + storeAddr.GetValue() +
								", name " + inst.dst +
								", content " + storeVal.GetValue() +
								", name " + inst.src1);
				break;
				
			case "move":
				Reg dstVar = GetReg(ctx, inst.dst);
				Reg srcVal = GetReg(ctx, inst.src1);
				if (!srcVal.IsNull())
					dstVar.SetValue(srcVal.GetValue());
				else dstVar.SetValue(null);
				break;
				
			case "load":
				
				Reg loadContent = GetReg(ctx, inst.dst);
				Reg loadAddr = GetReg(ctx, inst.src1);
				// FIXME : things become nasty when a string is to be loaded, because what we want to load is the string's address, while if
				// FIXME : we get the string from the globalRegs, the address of the head of strings doesn't actually resides in memory. This
				// FIXME : shows my IR has structural problems, and how to fix it depends on how the assembly's realization.
				// NOTE : always assume load 4 bytes, and cast it to int.
				int headAddr = loadAddr.GetValue();
				int loaded;
				
				if(LOG) logger.println("load content: " + loadContent.name);
				if(LOG) System.err.println("load, " +
								"space 4" +
								", addr " + headAddr +
								", name " + inst.src1 +
								", content...missing " +
								", name " + inst.dst);
				
				if (inst.src1.startsWith("*")) {
					// if load string
					loaded = headAddr;
				} else {
					loaded = mem.LoadInt(headAddr);
				}
				
				loadContent.SetValue(loaded);
				if(LOG) logger.println("load content: " + Integer.toString(loadContent.GetValue()));
				if(LOG) System.err.println("load, " +
								"space 4" +
								", addr " + headAddr +
								", name " + inst.src1 +
								", content " + loaded +
								", name " + inst.dst);
				break;
				
			/**
			 * If the funct's return type is void, the reg value is null.
			 * if ret isn't void, the return value shouldn't be null.
			 * */
			case "call":
				// create function info
				String funcName = inst.funcName;
				Reg ret = GetReg(ctx, inst.dst);
				List<Reg> args = new LinkedList<>();
				for (String arg : inst.args) {
					args.add(GetReg(ctx, arg));
				}
				
				if (LOG) {
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
					// create another thread
					RunCtx calleeCtx = new RunCtx(functs.get(funcName));
					calleeCtx.PassArgs(args);
					// stack it to the stack.
					ctx.Invoke(calleeCtx);
					
					// NOTE : this structure is cool: the interpreter's stack structure coincides with the actual program structure.
					while (!calleeCtx.terminate) {
						ExecuteInst(calleeCtx);
					}
					// get return value.
					// when ret.IsNull(), it means the function signature says its return value is null, no need to return.
					if (!ret.IsNull()) {
						if (calleeCtx.GetRetVal() == null) {
							ret.SetValue(null);
						} else {
							ret.SetValue(calleeCtx.GetRetVal().GetValue());
						}
					}
				}
				break;
			
			// binary operations are reserved for default
			// every function: runContext must end with a return.
			case "return":
				Reg retVal = GetReg(ctx, inst.dst);
				ctx.SetRetVal(retVal);
				ctx.terminate = true;
				if (LOG) logger.println("return " + ctx.func.name +
								" ret: " + retVal.GetValue() +
								", name " + retVal.name);
				break;
			
			case "jump":
				String jLabel = inst.dst;
				JumpToBlock(ctx, jLabel);
				if(LOG) System.err.println("jump, " + jLabel);
				break;
			
			case "branch":
				Reg brCond = GetReg(ctx, inst.dst);
				String ifTrue = inst.src1;
				String ifFalse = inst.src2;
				
				if(LOG) System.err.println("branch, cond " + brCond.GetValue() +
								", name " + inst.dst +
								", ifTrue " + inst.src1 +
								", ifFalse " + inst.src2);
				
				if (brCond.GetValue() == 1) {
					if(LOG) System.err.println("branch to " + ifTrue);
					JumpToBlock(ctx, ifTrue);
				} else {
					if(LOG) System.err.println("branch to " + ifFalse);
					JumpToBlock(ctx, ifFalse);
				}
				
				break;
			
			case "phi":
				Reg phiReg = GetReg(ctx, inst.dst);
				Map<String, String> opts = inst.options;
				Reg phiChoice = GetReg(ctx, opts.get(ctx.GetJumpFrom()));
				
				phiCached = true;
				phiParalCache.put(phiReg, phiChoice);
				
				if(LOG) System.err.println("phi, choose " + ctx.GetJumpFrom() + " val " + phiChoice.GetValue() + " name " + opts.get(ctx.GetJumpFrom()));
				
				break;
			
			case "<BasicBlock>":
				ctx.JumpToBB(inst.dst);
				break;
			
			case "neg": case "bitnot":
				Reg uniAns = GetReg(ctx, inst.dst);
				int uniSrc = GetReg(ctx, inst.src1).GetValue();
				if (inst.operator.equals("neg")) {
					uniAns.SetValue(-uniSrc);
				} else {
					uniAns.SetValue(~uniSrc);
				}
				if (LOG)
					System.err.println(inst.operator + ", src " + uniSrc + ", ans " + uniAns.GetValue());
				break;
			case "concat":
				int str1Addr = GetReg(ctx, inst.src1).GetValue();
				int str2Addr = GetReg(ctx, inst.src2).GetValue();
				String str1 = mem.LoadStr(str1Addr);
				String str2 = mem.LoadStr(str2Addr);
				String catStr = str1 + str2;
				int catStrAddr = mem.MallocMem(catStr.length() + 4);
				Reg catStrAns = GetReg(ctx, inst.dst);
				catStrAns.SetAllocd();
				catStrAns.SetValue(catStrAddr);
				mem.StoreInt(catStrAddr, catStr.length());
				mem.StoreStr(catStrAddr + 4, catStr);
				break;
			default:
				Reg binAns = GetReg(ctx, inst.dst);
				Integer binSrc1 = GetReg(ctx, inst.src1).GetValue();
				Integer binSrc2 = GetReg(ctx, inst.src2).GetValue();
				Integer ans = null;
				if (binSrc1 != null && binSrc2 != null) {
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
						case "eq": ans = (binSrc1.equals(binSrc2)) ? 1 : 0; break;
						case "ne": ans = (!binSrc1.equals(binSrc2)) ? 1 : 0; break;
					}
				} else {
					switch (inst.operator) {
						case "eq":
							ans = (binSrc1 == null) == (binSrc2 == null) ? 1 : 0;
							break;
						case "ne":
							ans = (binSrc1 == null) == (binSrc2 == null) ? 0 : 1;
							break;
					}
				}
				assert ans != null;
				binAns.SetValue(ans);
		}
	}
	
	/**
	 * Return value is an integer, if a function has no return value, it returns null.
	 * */
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
					stdout.print(unescape(printStr));
				else
					stdout.println(unescape(printStr));
				return null;
				
			case "getString":
				// read a line of string from stdin, malloc mem space, store it, and return headAddr.
				assert args.size() == 0;
				if(LOG) System.err.println("getString()...");
				/**
				 * stdScanner.nextLine() may read in '\n'.
				 * */
				String getStr = stdScanner.next();
				int getStrAddr = mem.MallocMem(getStr.length() + 4);
				mem.StoreInt(getStrAddr, getStr.length());
				mem.StoreStr(getStrAddr + 4, getStr);
				return getStrAddr;
			
			case "getInt":
				// getInt and return.
				assert args.size() == 0;
				if(LOG) System.err.println("getInt()...");
				int getInt = stdScanner.nextInt();
				// skip \n, not necessary.
//				stdScanner.nextLine();
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
	 * A key method
	 * Only interface to get register.
	 * Create new register and get existed register.
	 * */
	private Reg GetReg(RunCtx ctx, String name) {
		/**
		 * Check if it is a global variable or a string literal.
		 * */
		if (name.startsWith("@") || name.startsWith("*")) {
			if (!global.containsKey(name))
				global.put(name, new Reg(name));
			return global.get(name);
		}
		if (!ctx.defuse.containsKey(name))
			ctx.defuse.put(name, new Reg(name));
		return ctx.defuse.get(name);
	}
	
	/**
	 * Change RunTime context, and record the basic block that jumps from.
	 * */
	private void JumpToBlock(RunCtx ctx, String jLabel) {
		ctx.JumpToBB(jLabel);
		int jIndex = ctx.func.label2Index.get(jLabel);
		ctx.curInst = jIndex;
	}
	/**
	 * utilities
	 * */
	private List<String> SplitBySpaces(String line) {
		return Arrays.asList(line.trim().split(" +"));
	}
	
	/**
	 * A trick played in AST
	 * */
	private boolean IsBuiltIn(String functName) {
		return functName.startsWith("~");
	}
}