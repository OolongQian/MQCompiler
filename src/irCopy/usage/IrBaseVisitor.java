package ir.usage;

import ir.quad.*;
import ir.reg.IrValue;
import ir.util.BasicBlock;
import ir.util.FunctCtx;

public class IrBaseVisitor<T> implements IrVisitor<T> {
  @Override
  public T visit(Quad quad) {
//    return quad.Accept(this);
    return null;
  }

  @Override
  public T visit(Alloca quad) {
    return null;
  }

  @Override
  public T visit(Binary quad) {
    return null;
  }

  @Override
  public T visit(Load quad) {
    return null;
  }

  @Override
  public T visit(Store quad) {
    return null;
  }

  @Override
  public T visit(Unary quad) {
    return null;
  }

  @Override
  public T visit(IrValue var) {
//    return var.Accept(this);
    return null;
  }

  @Override
  public T visit(BasicBlock bb) {
    return null;
  }

  @Override
  public T visit(FunctCtx f) {
    return null;
  }
}
