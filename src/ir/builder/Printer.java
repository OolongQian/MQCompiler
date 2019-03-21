package ir.builder;

import ir.quad.*;
import ir.reg.GlobalReg;
import ir.reg.Reg;
import ir.reg.StringLiteral;
import ir.util.BasicBlock;
import ir.util.FunctCtx;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static ir.builder.Config.TEST;
import static ir.quad.Binary.Op.*;
import static ir.quad.Binary.Op.LAND;
import static ir.quad.Unary.Op.BITNOT;
import static ir.quad.Unary.Op.NEG;

public class Printer {

	private PrintStream fout;
	
	public Printer(String path) {
		if (TEST) {
			fout = System.out;
			return;
		}
		if (path == null) {
			this.fout = System.out;
			return;
		}
		try {
			this.fout = new PrintStream(new FileOutputStream(path));
		}
		catch (FileNotFoundException e) {
			throw new RuntimeException("IR printer output file not found");
		}
	}
	
	public PrintStream getFout() {
		return fout;
	}
	
	private static Map<Binary.Op, String> binOpInterpMap = new HashMap<>();
  static {
    binOpInterpMap.put(ADD, "+");
    binOpInterpMap.put(SUB, "-");
    binOpInterpMap.put(MUL, "*");
    binOpInterpMap.put(DIV, "/");
    binOpInterpMap.put(MOD, "%");
    binOpInterpMap.put(SHL, "<<");
    binOpInterpMap.put(SHR, ">>");
    binOpInterpMap.put(AND, "&");
    binOpInterpMap.put(XOR, "^");
    binOpInterpMap.put(OR,  "|");
    binOpInterpMap.put(LAND, "&&");
    binOpInterpMap.put(GT, ">");
	  binOpInterpMap.put(GE, ">=");
	  binOpInterpMap.put(LT, "<");
	  binOpInterpMap.put(LE, "<=");
	  binOpInterpMap.put(EQ, "==");
	  binOpInterpMap.put(NE, "!=");
	  binOpInterpMap.put(CONCAT, "+");
  }
  
	private static Map<Binary.Op, String> binOpStrMap = new HashMap<>();
	static {
		binOpStrMap.put(ADD, "add");
		binOpStrMap.put(SUB, "sub");
		binOpStrMap.put(MUL, "mul");
		binOpStrMap.put(DIV, "div");
		binOpStrMap.put(MOD, "mod");
		binOpStrMap.put(SHL, "shl");
		binOpStrMap.put(SHR, "shr");
		binOpStrMap.put(AND, "and");
		binOpStrMap.put(XOR, "xor");
		binOpStrMap.put(OR,  "or");
		binOpStrMap.put(LAND, "land");
		binOpStrMap.put(LOR, "lor");
		binOpStrMap.put(GT, "gt");
		binOpStrMap.put(GE, "ge");
		binOpStrMap.put(LT, "lt");
		binOpStrMap.put(LE, "le");
		binOpStrMap.put(EQ, "eq");
		binOpStrMap.put(NE, "ne");
		binOpStrMap.put(CONCAT, "concat");
	}
	
	private static Map<Unary.Op, String> uniOpInterpMap = new HashMap<>();
	static {
		uniOpInterpMap.put(NEG, "-");
		uniOpInterpMap.put(BITNOT, "^");
	}
	
	private static Map<Unary.Op, String> uniOpStrMap = new HashMap<>();
	static {
		uniOpStrMap.put(NEG, "neg");
		uniOpStrMap.put(BITNOT, "bitnot");
	}
	
	public void print(StringLiteral str) {
		fout.println("<*> " + str.id + " " + str.val);
	}
	
	public void print(GlobalReg reg) {
    fout.println("TODO ! @reg");
  }

  public void print(FunctCtx functs) {
    fout.print("<Function> " + functs.name + " ");
    
    for (Reg reg : functs.args) {
      fout.print(reg.getText() + " ");
    }
    fout.println();

    functs.bbs.forEach(this::print);
  }

  public void print(BasicBlock bb) {
    fout.println("<BasicBlock> " + bb.getName());
    bb.quads.forEach(x -> x.AcceptPrint(this));
  }

  public void print(Alloca quad) {
    fout.println("alloca " + quad.dst.getText());
  }

  public void print(Binary quad) {
    fout.println(
    				binOpStrMap.get(quad.op) + " " + quad.ans.getText() + " = " +
								    quad.src1.getText() + " " + binOpInterpMap.get(quad.op) + " " +
								    quad.src2.getText());
  }
  
  public void print(Branch quad) {
  	fout.println("branch " + quad.cond.getText() + " " +
					  quad.ifTrue.getName() + " " + quad.ifFalse.getName());
  }
  
  public void print(Call quad) {
    fout.print("call " + quad.funcName + " " + quad.ret.getText() + " ");
    quad.args.forEach(x -> fout.print(x.getText() + " "));
    fout.println();
  }
  
  public void print(GetElemPtr quad) {
  	fout.println("getElemPtr " + quad.elem.getText() + " " +
					  quad.base.getText() + " " + quad.offset.getText());
  }
  
  public void print(Jump quad) {
	  fout.println("jump " + quad.target.getName());
  }
  
  public void print(Load quad) {
	  fout.println("load " + quad.val.getText() + " " + quad.addr.getText());
  }
  
  public void print(Malloc quad) {
	  fout.println("malloc " + quad.memAddr.getText() + " " + quad.size_.getText());
  }
  
  public void print(Ret quad) {
	  fout.println("return " + quad.val.getText());
  }
  
  public void print(Store quad) {
	  fout.println("store " + quad.dst.getText() + " " + quad.src.getText());
  }
	
	public void print(Unary quad) {
		fout.println(uniOpStrMap.get(quad.op) + " " +
						quad.ans.getText() + " = " + uniOpInterpMap.get(quad.op) + " " + quad.src.getText());
	}
	
	public void print(Comment quad) {
		fout.println("# " + quad.content);
	}
	
	public void print(Phi quad) {
		fout.print("phi " + quad.phiVal.getText());
		
		for (BasicBlock blk : quad.options.keySet()) {
			fout.print(" " + blk.getName() + ":" + quad.options.get(blk).getText());
		}
		fout.println();
	}
}
