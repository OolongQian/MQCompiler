package ir_codegen.usage;

import ir_codegen.quad.*;

public class IRTraverseVisitor<T> extends IRBaseVisitor<T> {

  @Override
  public T visit(Quad quad) {
    return quad.Accept(this);
  }

  @Override
  public T visit(Load load) {
    visit(load.phyVar);
    visit(load.loaded);
    return null;
  }

  @Override
  public T visit(Store store) {
    visit(store.address);
    return null;
  }

  @Override
  public T visit(AllocaQuad allocaQuad) {
    visit(allocaQuad.var);
    return null;
  }

  @Override
  public T visit(Binary binary) {
    visit(binary.virtualReg);
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
