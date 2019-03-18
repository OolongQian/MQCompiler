package semantic;

import ast.node.dec.class_.ClassDec;
import ast.node.dec.function.FunctDec;
import ast.node.exp.CreationExp;
import ast.node.exp.unary.PrefixExp;
import ast.node.exp.unary.SuffixExp;
import ast.node.prog.Prog;
import ast.node.stm.BreakStm;
import ast.node.stm.ContinueStm;
import ast.node.stm.ForStm;
import ast.node.stm.WhileStm;
import ast.typeref.VarTypeRef;

import java.util.Hashtable;

/**
 * When type-checking is finished, this format visitor does final check
 * for various language specification and usage tricks.
 *
 * Language Specification:
 * 1. main function needs to be checked for its fixed signature.
 * 2. continue and break statement must be inside a loop scope.
 * 3. deprecated: returnStm can be omitted.
 * 4. array creation index must be continuous from start, and couldn't be all null.
 * 5. for ++a and a++, a needs to be a lvalue. But note that ++a produces a lvalue while a++ doesn't.
 * */
public class FormatCheckVisitor extends AstTraverseVisitor<Void> {

  /**
   * Create association from class's literal name to its actual classDec.
   * */
  public Hashtable<String, ClassDec> classTable;

  /**
   * Create association from function's literal name to its actual functionDec.
   * */
  public Hashtable<String, FunctDec> functTable;

  private int loopNest = 0;

  /**
   * it seems that returnStm could be skipped.
   * */
//  private boolean hasReturn = false;

  public FormatCheckVisitor(TypeCheckVisitor typeCheckVisitor) {
    classTable = typeCheckVisitor.classTable;
    functTable = typeCheckVisitor.functTable;
  }

  /**
   * Go!
   * */
  public void FormatCheck(Prog prog) {
    CheckMain();
    visit(prog);
  }

  /**
   * First check main function
   * */
  private void CheckMain() {
    if (!functTable.containsKey("main"))
      throw new RuntimeException("no main function...\n");
    FunctDec mainDec = functTable.get("main");
    if (!mainDec.returnType.isInt())
      throw new RuntimeException("main function has wrong return type...\n");
    if (mainDec.arguments.varDecs.size() != 0)
      throw new RuntimeException("main function has arguments...\n");
  }

  /**
   * Then check continue and break inside loops
   * */
  @Override
  public Void visit(ForStm forStm) {
    ++loopNest;
    super.visit(forStm);
    --loopNest;
    return null;
  }

  @Override
  public Void visit(WhileStm whileStm) {
    ++loopNest;
    super.visit(whileStm);
    --loopNest;
    return null;
  }

  @Override
  public Void visit(BreakStm breakStm) {
    if (loopNest <= 0)
      throw new RuntimeException("cannot break outside a loop...\n" + breakStm.LocationToString());
    return null;
  }

  @Override
  public Void visit(ContinueStm continueStm) {
    if (loopNest <= 0)
      throw new RuntimeException("cannot break outside a loop...\n" + continueStm.LocationToString());
    return null;
  }

  /**
   * array creation index must be continuous from start,
   * and couldn't be all null
   * */
  @Override
  public Void visit(CreationExp node) {
    super.visit(node);

    if (node.varTypeRef.isArray()) {
      VarTypeRef tmp = node.varTypeRef;
      boolean indexed = true;
      boolean allNull = true;

      while (tmp != null) {
        if (!indexed && tmp.dim != null) {
          throw new RuntimeException("array creation dimension isn't continuous...\n" + node.LocationToString());
        }
        if (indexed && tmp.dim == null) {
          indexed = false;
        }
        if (tmp.dim != null) {
          allNull = false;
        }
        tmp = tmp.innerType;
      }
      if (allNull)
        throw new RuntimeException("array dimension initializer expected...\n" + node.LocationToString());
    }
    return null;
  }


  @Override
  public Void visit(SuffixExp suffixExp) {
    super.visit(suffixExp);
    if (!suffixExp.objInstance.lValue)
      throw new RuntimeException("suffixExp " + suffixExp.op + " need to reference lvalue...\n" + suffixExp.LocationToString());
    return null;
  }

  @Override
  public Void visit(PrefixExp prefixExp) {
    super.visit(prefixExp);

    if ((prefixExp.op.equals("++") || prefixExp.op.equals("--")) && !(prefixExp.objInstance.lValue))
      throw new RuntimeException("prefixExp " + prefixExp.op + " need to reference lvalue...\n" + prefixExp.LocationToString());
    return null;
  }

//  /** It seems that missing return doesn't need a check. */
//  /**
//   * Check missing return.
//   * */
//  @Override
//  public Void visit(FunctDec functDec) {
//    hasReturn = false;
//    // don't consider completeness.
//    for (int i = 0; i < functDec.functBody.size(); ++i) {
//      visit(functDec.functBody.get(i));
//    }
//    if (!hasReturn && !functDec.returnType.isVoid() && !functDec.functType.typeName.equals("main") && !(functDec.getClass() == ConstructDec.class))
//      throw new RuntimeException("functDec missing return...\n" + functDec.LocationToString());
//    return null;
//  }
//
//  /**
//   * Check for whether return.
//   * If already has return, don't care about it.
//   * if this Ifstm is complete, set hasReturn to true.
//   * */
//  @Override
//  public Void visit(IfStm ifStm) {
//    // don't consider completeness
//    // already have return, go routine.
//    if (hasReturn) {
//      super.visit(ifStm);
//      return null;
//    } else {
//      visit(ifStm.thenBody);
//      if (!hasReturn) {
//        // then has no return, no longer could have return.
//        if (ifStm.elseBody != null)
//          visit(ifStm.elseBody);
//        hasReturn = false;
//        return null;
//      } else {
//        // then has return, count on else.
//        if (ifStm.elseBody == null) {
//          hasReturn = false;
//          return null;
//        } else {
//          visit(ifStm.elseBody);
//          if (hasReturn)
//            hasReturn = true;
//          else
//            hasReturn = false;
//          return null;
//        }
//      }
//    }
//  }
//
//  @Override
//  public Void visit(ReturnStm returnStm) {
//    hasReturn = true;
//    return null;
//  }
}
