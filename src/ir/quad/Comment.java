package ir.quad;


import ir.Printer;

public class Comment extends Quad {
	public String content;
	
	public Comment(String content) {
		this.content = content;
	}
	
	@Override
	public void AcceptPrint(Printer printer) {
		printer.print(this);
	}
	
}
