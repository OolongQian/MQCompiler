package ir_codegen;

import ast.typeref.FunctTypeRef;
import ir_codegen.util.Function;

import java.util.Hashtable;

public class IR {
  public Hashtable<FunctTypeRef, Function> functionTable = new Hashtable<>();
}
