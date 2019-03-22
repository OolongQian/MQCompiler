package ir.quad;

import ir.builder.Printer;
import ir.reg.IntLiteral;
import ir.reg.IrValue;
import ir.reg.Reg;

public class Alloca extends Quad {
  public Reg dst;
  public IrValue size_;

  public Alloca(Reg dst, IrValue size_) {
    this.dst = dst;
    this.size_ = size_;
  }
	
	public Alloca(Reg dst, int size_) {
		this.dst = dst;
		this.size_ = new IntLiteral(size_);
	}
	
	@Override
  public void AcceptPrint(Printer printer) {
    printer.print(this);
  }
}
