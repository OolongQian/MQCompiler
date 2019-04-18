package ir.quad;


import ir.Printer;
import ir.structure.BasicBlock;
import ir.structure.IrValue;
import ir.structure.Reg;
import nasm.AsmTranslateVisitor;

public class Jump extends Quad {
  public BasicBlock target;

  public Jump(BasicBlock target) {
    this.target = target;
  }
  
  @Override
  public void ReplaceUse(Reg v, IrValue val) {
    ;
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
