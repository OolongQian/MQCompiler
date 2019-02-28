package ast.node;

import org.antlr.v4.runtime.ParserRuleContext;

public interface ParserPosition {
  void SetPosition(ParserRuleContext ctx);
  int getBeginRow();
  int getEndRow();
  int getBeginCol();
  int getEndCol();
  String LocationToString();
}
