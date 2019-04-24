package ir.quad;


import ir.Printer;
import ir.structure.IrValue;
import ir.structure.Reg;
import nasm.AsmTranslateVisitor;

public class Comment extends Quad {
	public String content;
	
	public Comment(String content) {
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
