package ir.usage;

import ir.quad.*;
import ir.reg.IrValue;
import ir.util.BasicBlock;
import ir.util.FunctCtx;

public interface IrVisitor<T> {
  /**
   * Abstract
   * */
  T visit(Quad quad);

  T visit(Alloca quad);

  T visit(Binary quad);

  T visit(Load quad);

  T visit(Store quad);

  T visit(Unary quad);

  T visit(IrValue var);

  T visit(BasicBlock bb);

  T visit(FunctCtx f);
}
