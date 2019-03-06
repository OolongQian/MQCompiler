package ir_codegen.util;

import ir_codegen.quad.AllocaQuad;
import ir_codegen.quad.Quad;
import ir_codegen.quad.Virtual;

import static ir_codegen.builder.IRGenVisitor.GetHintName;

public class BasicBlock {
  public Quad headQuad = null;
  public BasicBlock prev = null, next = null;
  // label for this basic block, but Label class is a quad. It is essentially a Virtual Register.
  public Virtual physicalAddr = null;

  public BasicBlock() {
    physicalAddr = new Virtual(GetHintName());
  }

  public BasicBlock(BasicBlock prev, BasicBlock next) {
    this.prev = prev;
    this.next = next;
    this.physicalAddr = new Virtual(GetHintName());
  }

  /**
   * insert alloca quad to the headQuad part of BB
   */
  public void InsertAlloca(AllocaQuad allocaQuad) {
    if (headQuad == null) {
      headQuad = allocaQuad;
      allocaQuad.prev = allocaQuad.next = null;
    } else {
      Quad curQuad = headQuad;
      while (curQuad.next != null && curQuad.next instanceof AllocaQuad)
        curQuad = curQuad.next;

      if (curQuad.next != null)
        curQuad.next.prev = allocaQuad;
      allocaQuad.next = curQuad.next;
      curQuad.next = allocaQuad;
      allocaQuad.prev = curQuad;
    }
  }

  public void Append(Quad node) {
    if (headQuad == null)
      headQuad = node;
    else {
      Quad curNode = headQuad;
      while (curNode.next != null)
        curNode = curNode.next;
      curNode.next = node;
    }
  }

  public void AppendBB(BasicBlock nextBB) {
    nextBB.prev = this;
    nextBB.next = next;
    next = nextBB;
  }

  @Override
  public String toString() {
    String ret = "";
    Quad nowQuad = headQuad;
    while (nowQuad != null) {
      ret += nowQuad.toString();
      nowQuad = nowQuad.next;
    }
    return ret;
  }
}
