package ir.quad;

import ir.Printer;
import ir.structure.BasicBlock;
import ir.structure.IrValue;
import ir.structure.Reg;
import nasm.AsmTranslateVisitor;

import java.util.List;

public class Unary extends Quad {
  public enum Op {
    BITNOT, NEG
  }

  // ans is val instead of addr.
  public Reg ans;
  public Op op;
  public IrValue src;


  public Unary(Reg ans, Op op, IrValue src) {
    this.ans = ans;
    this.op = op;
    this.src = src;
  }
  
  public Unary(BasicBlock blk, Reg ans, Op op, IrValue src) {
    this.blk = blk;
    this.ans = ans;
    this.op = op;
    this.src = src;
  }
  
  @Override
  public Reg GetDefReg() {
    return ans;
  }
  @Override
  public void GetUseRegs(List<Reg> list_) {
    if (src instanceof Reg)
      list_.add((Reg) src);
  }
  @Override
  public void ReplaceUse(Reg v, IrValue c) {
  	assert src == v;
  	src = c;
  }
  
  @Override
  public void AcceptPrint(Printer printer) {
    printer.print(this);
  }
  
  @Override
  public void AcceptTranslator(AsmTranslateVisitor translator) {
    translator.visit(this);
  }
}
