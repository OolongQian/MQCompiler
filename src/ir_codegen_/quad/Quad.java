package ir_codegen.quad;

import ir_codegen.usage.IRBaseVisitor;

abstract public class Quad {

  public Quad prev, next;

  abstract public <T> T Accept (IRBaseVisitor<T> irBaseVisitor);
}
