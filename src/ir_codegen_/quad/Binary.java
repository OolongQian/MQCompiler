package ir_codegen.quad;

import com.sun.org.apache.bcel.internal.generic.GETFIELD;
import ir_codegen.usage.IRBaseVisitor;
import ir_codegen.util.HintName;

import static ir_codegen.quad.Binary.Op.ADD;
import static ir_codegen.util.Info.tempTypeName;

public class Binary extends Quad implements Value {
  public enum Op {
    ADD
  }

  private Op op;
  private Value lhs;
  private Value rhs;
  public Virtual virtualReg;

  public Binary(Op op, Value lhs, Value rhs, Virtual virtualReg) {
    this.op = op;
    this.lhs = lhs;
    this.rhs = rhs;
    this.virtualReg = virtualReg;
  }

  @Override
  public HintName GetValueName() {
    return virtualReg.hintNameIR;
  }

  @Override
  public String toString() {
    String opStr = (op == ADD) ? "add" : "not implemented";
    return GetValueName() + " = " + opStr + " " + tempTypeName + " " + lhs.GetValueName() + ", " + rhs.GetValueName() + "\n";
  }

  @Override
  public <T> T Accept(IRBaseVisitor<T> irBaseVisitor) {
    return irBaseVisitor.visit(this);
  }
}
