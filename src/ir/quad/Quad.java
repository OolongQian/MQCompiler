package ir.quad;

import ir.Printer;
import ir.structure.BasicBlock;

abstract public class Quad {
	abstract public void AcceptPrint(Printer printer);
	public BasicBlock blk;
}
