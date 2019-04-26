package ir.quad;


import ir.Printer;
import ir.structure.BasicBlock;
import ir.structure.IrValue;
import ir.structure.Reg;
import nasm.AsmTranslateVisitor;

import java.util.Map;

import static ir.Utility.inlinePrefix;

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
	
	@Override
	public Quad Copy(Map<String, BasicBlock> BBMap) {
		Comment tmp = new Comment(content);
		tmp.blk = BBMap.get( blk.name + inlinePrefix);
		return tmp;
	}
}
