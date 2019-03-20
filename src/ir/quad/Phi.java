package ir.quad;

import ir.builder.Printer;
import ir.reg.IrValue;
import ir.reg.Reg;
import ir.util.BasicBlock;

import java.util.HashMap;
import java.util.Map;

public class Phi extends Quad {
	public Reg phiVal;
	public Map<BasicBlock, IrValue> options = new HashMap<>();
	
	public Phi(Reg phiVal, Map<BasicBlock, IrValue> options) {
		this.phiVal = phiVal;
		this.options = options;
	}
	
	@Override
	public void AcceptPrint(Printer printer) {
		printer.print(this);
	}
}
