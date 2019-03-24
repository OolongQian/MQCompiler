package ast;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;

public class ParserErrorHandlerStrategy extends BailErrorStrategy {

  @Override
  public void reportError(Parser parser, RecognitionException e) {
    throw new RuntimeException("antlr parse error...\n");
  }
}
