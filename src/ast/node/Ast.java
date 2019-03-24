package ast.node;

import ast.usage.AstBaseVisitor;
import org.antlr.v4.runtime.ParserRuleContext;

abstract public class Ast implements ParserPosition {
	private int beginCol, endCol;
	private int beginRow, endRow;
	
	@Override
	public void SetPosition(ParserRuleContext ctx) {
		beginCol = ctx.start.getStartIndex();
		endCol = ctx.stop.getStopIndex();
		beginRow = ctx.start.getLine();
		endRow = ctx.stop.getLine();
	}
	
	@Override
	public int getBeginRow() {
		return beginRow;
	}
	
	@Override
	public int getEndRow() {
		return endRow;
	}
	
	@Override
	public int getEndCol() {
		return endCol;
	}
	
	@Override
	public int getBeginCol() {
		return beginCol;
	}
	
	@Override
	public String LocationToString() {
		return "Location -- Row: " + getBeginRow() + "-" + getEndRow() + " Col: " + getBeginCol() + "-" + getEndCol() + "\n";
	}
	
	abstract public <T> T Accept (AstBaseVisitor<T> visitor);
}
