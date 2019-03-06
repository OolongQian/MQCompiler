package ir_codegen.usage;

import ir_codegen.quad.*;

public class IRBaseVisitor<T> implements IRVisitor<T> {

  @Override
  public T visit(Quad quad) {
    return null;
  }

  @Override
  public T visit(Load load) {
    return null;
  }

  @Override
  public T visit(Store store) {
    return null;
  }

  @Override
  public T visit(AllocaQuad allocaQuad) {
    return null;
  }

  @Override
  public T visit(Binary binary) {
    return null;
  }


  @Override
  public T visit(Var var) {
    return null;
  }

  @Override
  public T visit(Virtual virtual) {
    return null;
  }

  @Override
  public T visit(Return_ return_) {
    return null;
  }
}
