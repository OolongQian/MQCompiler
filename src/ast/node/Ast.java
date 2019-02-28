package ast.node;

import ast.usage.AstBaseVisitor;
import org.antlr.v4.runtime.ParserRuleContext;

abstract public class Ast implements ParserPosition {
  protected static int tab = 0;

  protected static String TabShift(String inStr) {
    String ret = "";
    for (int i = 0; i < tab; ++i) {
      ret += "\t";
    }
    return ret + inStr;
  }

  protected String Formatter(Ast self, String raw) {
    return self.SelfDeclare() + TabShift(raw);
  }

  abstract public void Accept (AstBaseVisitor visitor);


  /**
   * Declare self-identity without indent, taking an independent row. */
  protected String SelfDeclare() {
    return "";
  }

  public String PrettyPrint() {
    return "Default PrettyPrint\n";
  }

  protected int beginCol, endCol;
  protected int beginRow, endRow;

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
}
