package ir.quad;


import ir.Printer;
import ir.structure.BasicBlock;
import ir.structure.IrValue;
import ir.structure.Reg;
import nasm.AsmTranslateVisitor;

import java.util.Map;

import static ir.Utility.inlinePrefix;

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
  
  @Override
  public Quad Copy(Map<String, BasicBlock> BBMap) {
  	try {
		  Jump tmp = new Jump(BBMap.get( target.name + inlinePrefix));
		  tmp.blk = BBMap.get( blk.name + inlinePrefix);
		  return tmp;
	  } catch (NullPointerException e) {
  		int a = 1;
	  }
	  return null;
  }
}
