package ast.typeref;

import ast.node.Ast;

/**
 * The following question is how to design TypeRef class.
 * Declaration can actually serve as a kind of type, but that's too concrete.
 *
 * ClassDec is pure kind of Type.
 * */
abstract public class TypeRef extends Ast {
  public String typeName = "";
}
