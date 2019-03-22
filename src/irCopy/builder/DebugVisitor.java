package ir.builder;

import ast.node.exp.lvalue.ArrayAccessExp;
import ast.node.exp.lvalue.FieldAccessExp;
import ast.node.exp.unary.PrefixExp;
import ast.node.exp.unary.SuffixExp;
import ast.node.prog.Prog;
import ast.typeref.VarTypeRef;
import semantic.AstTraverseVisitor;

public class DebugVisitor extends AstTraverseVisitor<Void> {

  public static boolean checkLValue = false;
  public static boolean checkBaseType = false;

  public void debug(Prog prog) {
    System.out.println("start debug...\n");
    prog.Accept(this);
  }

  @Override
  public Void visit(SuffixExp node) {
    if (checkLValue) {
      if (node.objInstance.lValue)
        ;
      else {
        System.out.println("SUFFIX\n" + node.LocationToString());
        throw new RuntimeException();
      }
    }

    return super.visit(node);
  }

  @Override
  public Void visit(PrefixExp node) {
    if (checkLValue) {
      if (node.op.equals("++") || node.op.equals("--")) {
        if (node.objInstance.lValue && node.lValue) ;
        else {
          System.out.println("PREFIX\n" + node.LocationToString());
          throw new RuntimeException();
        }
      }
    }

    return super.visit(node);
  }

  /**
   * NOTE !!! FIXME !!! FieldAccess's object doesn't have to be a lValue...
   * */
  @Override
  public Void visit(FieldAccessExp node) {
    if (checkLValue) {
      if (node.lValue)
        ;
      else {
        System.out.println("FIELDACCESS\n" + node.LocationToString());
        throw new RuntimeException();
      }
    }

    return super.visit(node);
  }

  /**
   * NOTE !!! FIXME !!! ArrayAccess's object doesn't have to be a lValue...
   * */
  @Override
  public Void visit(ArrayAccessExp node) {
    if (checkLValue) {
      if (node.lValue)
        ;
      else {
        System.out.println("ARRAYACCESS\n" + node.LocationToString());
        throw new RuntimeException();
      }
    }
    return super.visit(node);
  }

  @Override
  public Void visit(VarTypeRef varTypeRef) {
    if (checkBaseType) {
      if (varTypeRef.baseType == null)
        throw new RuntimeException("null baseType...\n" + varTypeRef.LocationToString());
    }
    return null;
  }
}
