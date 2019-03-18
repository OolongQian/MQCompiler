package ast.node.stm;

import ast.node.Ast;
import ir.util.BasicBlock;
import org.antlr.v4.runtime.ParserRuleContext;

abstract public class Stm extends Ast {
  private int beginCol, endCol;
  private int beginRow, endRow;

  @Override
  public void SetPosition(ParserRuleContext ctx) {
    beginCol = ctx.start.getStartIndex();
    endCol = ctx.stop.getStopIndex();
    beginRow = ctx.start.getLine();
    endRow = ctx.stop.getLine();
  }

  public BasicBlock GetLoopAfter() {
    return null;
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
}
