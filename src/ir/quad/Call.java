package ir.quad;

import ir.builder.Printer;
import ir.reg.GlobalReg;
import ir.reg.IrValue;
import ir.reg.Reg;

import java.util.LinkedList;
import java.util.List;

public class Call extends Quad {
  public String funcName;
  public Reg ret;
  public List<IrValue> args = new LinkedList<>();
  

  public Call(String funcName) {
  	ret = new GlobalReg("@null");
    this.funcName = funcName;
  }


  // for constructor.
  public Call(String funcName, Reg ret, Reg instPtr) {
    this.funcName = funcName;
    this.ret = ret;
    args = new LinkedList<>();
    args.add(instPtr);
  }

  // for simple functions.
  public Call(String funcName, Reg ret, List<IrValue> args) {
    this.funcName = funcName;
    this.ret = ret;
    this.args = args;
  }

  // for method function.
  public Call(String funcName, Reg ret, Reg instPtr, List<IrValue> args) {
    this.funcName = funcName;
    this.ret = ret;
    this.args.add(instPtr);
    this.args.addAll(args);
  }

  @Override
  public void AcceptPrint(Printer printer) {
    printer.print(this);
  }
}
