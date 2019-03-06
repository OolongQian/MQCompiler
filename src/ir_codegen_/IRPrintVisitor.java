package ir_codegen;

import ir_codegen.quad.*;
import ir_codegen.usage.IRTraverseVisitor;
import ir_codegen.util.Function;
import ir_codegen.util.HintName;

import java.util.Enumeration;

/**
 * IRPrintVisitor is for register name allocation.
 * */
public class IRPrintVisitor extends IRTraverseVisitor<Void> {

  private IR IR;

  private static int hintIndex = 1;
  private static HintName GetHintName() {
    return new HintName("%" + Integer.toString(hintIndex++));
  }

  public IRPrintVisitor(ir_codegen.IR IR) {
    this.IR = IR;
  }

  public String VisitAndPrintIR() {
    String ret = "";
    Enumeration<Function> e = IR.functionTable.elements();
    while (e.hasMoreElements()) {
      Function temp = e.nextElement();
      Quad nowQuad = temp.headBB.headQuad;
      while (nowQuad != null) {
        visit(nowQuad);
        nowQuad = nowQuad.next;
      }
      ret += temp.toString();
    }
    return ret;
  }

  @Override
  public Void visit(Var var) {
    if (!var.named) {
      var.hintNameIR = GetHintName();
      var.named = true;
    }
    return null;
  }

  @Override
  public Void visit(Virtual virtual) {
    if (!virtual.named) {
      virtual.hintNameIR = GetHintName();
      virtual.named = true;
    }
    return null;
  }
}
