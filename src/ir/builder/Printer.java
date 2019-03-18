package ir.builder;

import ir.quad.*;
import ir.reg.GlobalReg;
import ir.util.BasicBlock;
import ir.util.FunctCtx;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static ir.quad.Binary.Op.*;
import static ir.quad.Binary.Op.LAND;
import static ir.quad.Unary.Op.BITNOT;
import static ir.quad.Unary.Op.NEG;

public class Printer {

	private PrintStream fout;
	
	
	
	public Printer(String path) {
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
  }
	
	private static Map<Unary.Op, String> uniOpInterpMap = new HashMap<>();
	static {
		uniOpInterpMap.put(NEG, "-");
		uniOpInterpMap.put(BITNOT, "^");
	}
	
	
	public void print(GlobalReg reg) {
    fout.println("@reg");
  }

  public void print(FunctCtx functs) {
    fout.print("<Function> " + functs.name + " ");

    for (String s : functs.args) {
      fout.print(s + " ");
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
    				quad.ans.getText() + " = " + quad.src1.getText() + " " +
								    binOpInterpMap.get(quad.op) + " " + quad.src2.getText());
  }
  
  public void print(Branch quad) {
  	fout.println("branch " + quad.cond.getText() + " " +
					  quad.ifTrue.getName() + ", " + quad.ifFalse.getName());
  }
  
  public void print(Call quad) {
    fout.print("call " + quad.funcName + " " + quad.ret.getText() + " ");
    quad.args.forEach(x -> fout.print(x.getText()));
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
		fout.println("unary " + uniOpInterpMap.get(quad.op) + " " +
						quad.ans.getText() + " " + quad.src.getText());
	}
	
	public void print(Comment quad) {
		fout.println("# " + quad.content);
	}
}
