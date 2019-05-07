package ir.quad;


import ir.Printer;
import ir.structure.BasicBlock;
import ir.structure.IrValue;
import ir.structure.Reg;
import nasm.AsmTranslateVisitor;

public class Comment extends Quad {
	public String content;
	
	public Comment(String content) {
		this.content = content;
	}
	
	public Comment(BasicBlock blk, String content) {
		this.blk = blk;
		this.content = content;
	}
	
	@Override
	public void ReplaceUse(Reg v, IrValue val) { }
	
	@Override
	public void AcceptPrint(Printer printer) {
		printer.print(this);
	}
	
	@Override
	public void AcceptTranslator(AsmTranslateVisitor translator) {
		translator.visit(this);
	}
}
