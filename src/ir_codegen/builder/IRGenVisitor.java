package ir_codegen.builder;

import ast.node.dec.function.FunctDec;
import ast.node.dec.variable.GlobalVarDec;
import ast.node.dec.variable.VarDec;
import ast.node.exp.binary.ArithBinaryExp;
import ast.node.exp.binary.LogicBinaryExp;
import ast.node.exp.literal.BoolLiteralExp;
import ast.node.exp.literal.IntLiteralExp;
import ast.node.exp.literal.StringLiteralExp;
import ast.node.exp.lvalue.ArrayAccessExp;
import ast.node.exp.lvalue.VarExp;
import ast.node.exp.unary.PrefixExp;
import ast.node.exp.unary.SuffixExp;
import ast.node.stm.*;
import ir_codegen.quad.*;
import ir_codegen.util.BasicBlock;
import type_check.AstTraverseVisitor;

import java.util.Stack;

import static ir_codegen.util.Namer.GetHintName;

/**
 * Note the virtual register naming here isn't necessary.
 * We name it just for debugging purposes.
 *
 * The translation is attached to AST.
 * */
public class IRGenVisitor extends AstTraverseVisitor<Void> {

  private BasicBlock curBB = null;

  private Stack<Stm> curLoop = new Stack<>();

  private void processStringComparison(ArithBinaryExp node) {
    if (node.ifTrue == null || node.ifFalse == null) {
      System.out.println("something's wrong");
    }
    // TODO : fill this later.
    visit(node.lhs);
    visit(node.rhs);
    curBB.Append(new Binary());
    curBB.Append(new Branch());
  }

  private void processIntComparison(ArithBinaryExp node) {
    if (node.ifTrue == null || node.ifFalse == null) {
      System.out.println("something's wrong");
    }
    // TODO : fill this later.
    visit(node.lhs);
    visit(node.rhs);
    curBB.Append(new Binary());
    curBB.Append(new Branch());
  }

  /**
   * operation has integer or string type.
   * */
  private void processBinaryOperation(ArithBinaryExp node) {
    // TODO : fill this in
    visit(node.lhs);
    visit(node.rhs);
    curBB.Append(new Binary());
  }

  @Override
  public Void visit(FunctDec functDec) {
    curBB = functDec.GetHeadBB();
    // TODO : more info needs to be recorded by functDec.
    for (int i = 0; i < functDec.functBody.size(); ++i)
      visit(functDec.functBody.get(i));
    return null;
  }

  /**
   * Assign irValue to
   * */
  @Override
  public Void visit(GlobalVarDec globalVarDec) {
    // VarTypeRef is a unique reference.
    for (int i = 0; i < globalVarDec.varDecs.size(); ++i) {
      globalVarDec.varDecs.get(i).hintName = GetHintName("g");
    }
    return null;
  }

  @Override
  public Void visit(VarDec varDec) {
    varDec.hintName = GetHintName("v");
    // TODO : fill this later, global has already been overloaded.
    curBB.Append(new Alloca());
    return null;
  }

  @Override
  public Void visit(IfStm ifStm) {
    // TODO : Chen Lequn didn't write the optimal here.
    // Attach BB onto ast node.
    ifStm.ifTrue = new BasicBlock("if_true");
    ifStm.ifFalse = new BasicBlock("if_false");
    ifStm.ifMerge = new BasicBlock("if_merge");

    // prepare for logic expr generation
    // TODO : why not jump to merge directly when else is missing...
    ifStm.condition.ifTrue = ifStm.ifTrue;
    ifStm.condition.ifFalse = ifStm.ifFalse;
    visit(ifStm.condition);

    // generate then block.
    curBB = ifStm.ifTrue;
    visit(ifStm.thenBody);
    // TODO fill this later. jump to merge
    curBB.Append(new Jump());

    if (ifStm.elseBody != null) {
      curBB = ifStm.ifFalse;
      visit(ifStm.elseBody);
    }
    curBB.Append(new Jump());

    curBB = ifStm.ifMerge;
    return null;
  }

  @Override
  public Void visit(IntLiteralExp node) {
    // TODO : have nothing to do.
    System.out.println("IntLiteralExp has nothing to do...\n");
    return null;
  }

  @Override
  public Void visit(BoolLiteralExp node) {
    System.out.println("BoolLiteralExp has nothing to do...\n");
    return null;
  }

  @Override
  public Void visit(StringLiteralExp node) {
    System.out.println("StringLiteralExp has nothing to do...\n");
    return null;
  }

  @Override
  public Void visit(ArithBinaryExp node) {
    switch (node.op) {
      // TODO : fill it later.
      case "*":
      case "/":
      case "-":
      case "%":
      case "<<":
      case ">>":
      case "&":
      case "^":
      case "|":
      case "+":
        processBinaryOperation(node);
        break;
      case ">":
      case ">=":
      case "<":
      case "<=":
      case "==":
      case "!=":
        if (node.varTypeRefDec.isInt()) {
          processIntComparison(node);
        } else {
          processStringComparison(node);
        }
        break;
    }
    return null;
  }

  /**
   * generate code for logic binary expression
   * begin at proper basic block
   * end within its final block, which means
   * curBB isn't suitable after visit() returns.
   * */
  @Override
  public Void visit(LogicBinaryExp node) {
    // TODO : have assumed that logicBinaryExp has already had ifTrue or ifFalse assigned by upper statement.

    if (node.op.equals("||")) {
      node.lhs.ifTrue = node.ifTrue;
      node.lhs.ifFalse = new BasicBlock("lhs_false_fall_through");
      // branch has been appended in visit
      visit(node.lhs);
      // prepare to generate code for the next basic block.
      curBB = node.lhs.ifFalse;
    } else {
      node.lhs.ifTrue = new BasicBlock("lhs_right_fall_through");
      node.lhs.ifFalse = node.ifFalse;
      visit(node.rhs);
      curBB = node.lhs.ifTrue;
    }
    node.rhs.ifTrue = node.ifTrue;
    node.rhs.ifFalse = node.ifFalse;
    visit(node.rhs);
    return null;
  }

  @Override
  public Void visit(PrefixExp prefixExp) {
    // TODO : fill this later.
    curBB.Append(new Unary());
    return null;
  }

  @Override
  public Void visit(SuffixExp suffixExp) {
    // TODO : fill this later.
    curBB.Append(new Unary());
    return null;
  }

  @Override
  public Void visit(VarExp varExp) {
    varExp.AssignIrTmp("tmp");
    // TODO : fill this later. Load IrVar's data into IrTmp.
    curBB.Append(new Load());
    return null;
  }

  @Override
  public Void visit(WhileStm node) {
    node.cond = new BasicBlock("loop_cond");
    node.step = new BasicBlock("loop_step");
    node.after = new BasicBlock("loop_after");

    // while's condition takes a separate bb because it may be checked multiple times.
    curBB = node.cond;
    node.condition.ifTrue = node.step;
    node.condition.ifFalse = node.after;
    visit(node.condition);

    curBB = node.step;
    curLoop.push(node);
    visit(node.whileBody);
    curLoop.pop();

    // set curBB to continue
    curBB = node.after;
    return null;
  }

  @Override
  public Void visit(ForStm node) {
    node.cond = new BasicBlock("for_cond");
    node.step = new BasicBlock("for_step");
    node.after = new BasicBlock("for_after");
    // attach jump info to ast node because they have better structure than BB.
    node.forControl.check.ifTrue = node.step;
    node.forControl.check.ifFalse = node.after;

    // generate initialization before loop checking.
    if (!node.forControl.isInitNull()) {
      if (node.forControl.initIsDec)
        visit(node.forControl.initDec);
      else
        node.forControl.initExps.forEach(x -> visit(x));
    }
    // assign check branch target. push down information
    curBB = node.cond;
    visit(node.forControl.check);
    // go and generate body.
    curBB = node.step;
    curLoop.push(node);
    visit(node.forBody);
    curLoop.pop();
    // continue to other statement.
    curBB = node.after;
    return null;
  }

  @Override
  public Void visit(ContinueStm continueStm) {
    // TODO : fill this later to the 'step' BB when peaking the curLoop stack.
    curBB.Append(new Jump());
    return null;
  }

  @Override
  public Void visit(BreakStm breakStm) {
    // TODO : fill this later to the 'after' BB when peaking the curLoop stack.
    curBB.Append(new Jump());
    return null;
  }

  @Override
  public Void visit(ReturnStm returnStm) {
    // TODO : fill this later.
    curBB.Append(new Return());
    return null;
  }

  @Override
  public Void visit(ArrayAccessExp node) {
    visit(node.arrInstance);
    visit(node.accessor);

    // TODO : mul index shift
    Binary index = new Binary();
    // TODO : add base and shift
    Binary shift = new Binary();
    // TODO : load data
    Load load = new Load();

    curBB.Append(index);
    curBB.Append(shift);
    curBB.Append(load);
    return null;
  }
}
