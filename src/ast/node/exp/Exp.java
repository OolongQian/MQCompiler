package ast.node.exp;

import ast.node.Ast;
import ast.typeref.VarTypeRef;
import ir.reg.IrValue;
import ir.reg.Reg;
import ir.util.BasicBlock;

abstract public class Exp extends Ast {

  public boolean lValue = false;

  public VarTypeRef varTypeRef;

  // NOTE : for IR logic short evaluation
  public BasicBlock ifTrue;
  public BasicBlock ifFalse;

  /**
   * value is the register containing value.
   * ptr is the pointer register pointing to the value.
   * only lvalue has it.
   *
   *   // If the expression is a bool literal or int literal, then the return value
   *   // is just a IntLiteral containing the same value.
   *   // If the expression is of type bool or int but is not a literal, then the
   *   // return value is the register containing the value.
   *   // If the expression is of type array or string or some user-defined type,
   *   // then the return value is the register containing the pointer which points
   *   // to the actual instance.
   *
   *  NOTE : this is used primarily for arithmetic operation.
   * */
  private Reg irAddr;
  private IrValue irValue;

  public Reg getIrAddr() {
    return irAddr;
  }

  public IrValue getIrValue() {
    // if not set, return null
    return irValue;
  }

  public void setIrAddr(Reg irAddr) {
    this.irAddr = irAddr;
  }

  public void setIrValue(IrValue irValue) {
    this.irValue = irValue;
  }
}
