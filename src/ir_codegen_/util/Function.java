package ir_codegen.util;

import ast.node.dec.function.FunctDec;
import ir_codegen.quad.Virtual;

import static ir_codegen.builder.IRGenVisitor.GetHintName;
import static ir_codegen.util.Info.tempTypeName;

public class Function {
  // TODO : now directly pass info from AST, ugly.
  public FunctDec functDec;

  public Virtual physicalAddr;
  public BasicBlock headBB;
  public BasicBlock tailBB;

  public Function(FunctDec functDec) {
    this.functDec = functDec;
    physicalAddr = new Virtual(GetHintName());
    headBB = new BasicBlock(null, null);
    tailBB = headBB;
  }

  public void AppendNewBB() {
    tailBB = new BasicBlock(tailBB, tailBB.next);
    tailBB.prev.next = tailBB;
  }

  @Override
  public String toString() {
    String ret = "";
    ret += "define " + tempTypeName + " @" + functDec.functType.typeName + functDec.arguments.toString() + "\n";
    BasicBlock nowBB = headBB;
    while (nowBB != null) {
      ret += nowBB.toString();
      nowBB = nowBB.next;
    }
    ret += "end\n";
    return ret;
  }
}
