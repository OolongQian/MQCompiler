package ir.quad;


import ir.Printer;
import ir.structure.Constant;
import ir.structure.IrValue;
import ir.structure.Reg;

import java.util.List;

public class Store extends Quad {
  public Reg dst;
  public IrValue src;

  public Store(Reg dst, IrValue src) {
    this.dst = dst;
    this.src = src;
  }
  
  @Override
  public Reg GetDefReg() {
    // store doesn't define.
//    return dst;
    return null;
  }
  
  @Override
  public void GetUseRegs(List<Reg> list_) {
    list_.add(dst);
    if (src instanceof Reg)
      list_.add((Reg) src);
  }
	@Override
	public void ReplaceUse(Reg v, IrValue c) {
  	boolean replace = false;
  	if (dst == v) {
  		if (c instanceof Constant)
  		  throw new RuntimeException("I think programmer cannot store to a constant address.");
  		dst = (Reg) c;
  		replace = true;
	  }
	  if (src == v) {
  		replace = true;
  		src = c;
	  }
	  assert replace;
	}
	
	@Override
  public void AcceptPrint(Printer printer) {
    printer.print(this);
  }
}
