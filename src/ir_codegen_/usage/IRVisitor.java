package ir_codegen.usage;

import ir_codegen.quad.*;

public interface IRVisitor<T> {

  T visit (Quad quad);

  T visit (AllocaQuad allocaQuad);

  T visit (Binary binary);

  T visit (Load load);

  T visit (Store store);

  T visit (Var var);

  T visit (Virtual virtual);

  T visit (Return_ return_);
}
