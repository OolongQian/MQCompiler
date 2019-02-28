package ast.builder;

import org.antlr.v4.runtime.*;

public class ParserErrorHandlerStrategy extends BailErrorStrategy {

  @Override
  public void reportError(Parser parser, RecognitionException e) {
    throw new RuntimeException("antlr parse error...\n");
  }
}
