package ast.builder;

import antlr_tools.MgBaseVisitor;
import antlr_tools.MgParser;
import ast.node.Ast;
import ast.node.dec.*;
import ast.node.dec.class_.ClassDec;
import ast.node.dec.function.ConstructDec;
import ast.node.dec.function.FunctDec;
import ast.node.dec.function.MethodDec;
import ast.node.dec.variable.*;
import ast.node.exp.*;
import ast.node.exp.binary.ArithBinaryExp;
import ast.node.exp.binary.LogicBinaryExp;
import ast.node.exp.literal.BoolLiteralExp;
import ast.node.exp.literal.IntLiteralExp;
import ast.node.exp.literal.StringLiteralExp;
import ast.node.exp.lvalue.*;
import ast.node.exp.unary.PrefixExp;
import ast.node.exp.unary.SuffixExp;
import ast.node.prog.Prog;
import ast.node.stm.*;
import ast.typeref.VarTypeRef;

/**
 * This is an antlr parse tree visitor intended to builder1 an abstracted syntax tree.
 * This AstBuilder tries to duplicate antlr parse tree, following the original syntactic
 * format honestly. By recording the entire information literally, it provides a
 * playground for later type-checking.
 * */
public class AstBuilder extends MgBaseVisitor<Ast> {

  private int scope = 0;
  private boolean inFunct = false;

  @Override
  public Ast visitProg(MgParser.ProgContext ctx) {
    Prog prog = new Prog();
    prog.SetPosition(ctx);
    for (int i = 0; i < ctx.dec().size(); ++i) {
      prog.AddDec((Dec) visitDec(ctx.dec(i)));
    }
    return prog;
  }


  @Override
  public Ast visitDec(MgParser.DecContext ctx) {
    return visit(ctx.getChild(0));
  }

  @Override
  public Ast visitClassDec(MgParser.ClassDecContext ctx) {
    ClassDec classDec = new ClassDec(ctx.className.getText());
    classDec.SetPosition(ctx);

    ++scope;
    Dec ref;
    for (int i = 0; i < ctx.classBody().classBodyDec().size(); ++i) {
      ref = (Dec) visit(ctx.classBody().classBodyDec(i));

      if (ref instanceof ConstructDec)
        classDec.constructor = (ConstructDec) ref;
      else if (ref instanceof MethodDec)
        classDec.method.add((MethodDec) ref);
      else if (ref instanceof FieldDec) {
        classDec.AddFieldList((FieldDec) ref);
      }
    }
//      classDec.member.add((Dec) visit(ctx.classBody().classBodyDec(i)));
    --scope;

    if (classDec.constructor != null)
      classDec.constructor.returnType = classDec.classType;

    return classDec;
  }
  
  @Override
  public Ast visitClassBodyDec(MgParser.ClassBodyDecContext ctx) {
    return visit(ctx.getChild(0));
  }
  
  @Override
  public Ast visitConstructorDec(MgParser.ConstructorDecContext ctx) {
    ConstructDec constructDec = new ConstructDec();
    constructDec.SetPosition(ctx);
    constructDec.functName = ctx.className.getText();

    ++scope;
    inFunct = true;
    if (ctx.functDecParaList() != null) {
      for (int i = 0; i < ctx.functDecParaList().functDecPara().size(); ++i) {
        constructDec.arguments.varDecs.add((VarDec) visit(ctx.functDecParaList().functDecPara(i)));
      }
    }
    for (int i = 0; i < ctx.blockStm().stm().size(); ++i)
      constructDec.functBody.add((Stm) visit(ctx.blockStm().stm(i)));
    inFunct = false;
    --scope;

    return constructDec;
  }

  @Override
  public Ast visitFunctDec(MgParser.FunctDecContext ctx) {
    FunctDec functDec = new FunctDec();
    functDec.SetPosition(ctx);
    functDec.functName = ctx.functName.getText();
    functDec.returnType = VarTypeRef.CreatePrimitiveType(ctx.simpleType().getText());

    ++scope;
    inFunct = true;
    if (ctx.functDecParaList() != null) {
      for (int i = 0; i < ctx.functDecParaList().functDecPara().size(); ++i) {
        functDec.arguments.varDecs.add((VarDec) visit(ctx.functDecParaList().functDecPara(i)));
      }
    }
    for (int i = 0; i < ctx.blockStm().stm().size(); ++i)
      functDec.functBody.add((Stm) visit(ctx.blockStm().stm(i)));
    inFunct = false;
    --scope;

    if (scope > 0)
      return new MethodDec(functDec);
    else
      return functDec;
  }

  @Override
  public Ast visitFunctDecPara(MgParser.FunctDecParaContext ctx) {
    VarDec varDec = new VarDec((VarTypeRef) visit(ctx.type()), ctx.Identifier().getText());
    varDec.SetPosition(ctx);
    return varDec;
  }

  @Override
  public Ast visitType(MgParser.TypeContext ctx) {
    return visit(ctx.getChild(0));
  }

  @Override
  public Ast visitSimpleType(MgParser.SimpleTypeContext ctx) {
    VarTypeRef varTypeRef = VarTypeRef.CreatePrimitiveType(ctx.getText());
    varTypeRef.SetPosition(ctx);
    return varTypeRef;
  }

  @Override
  public Ast visitArrayType(MgParser.ArrayTypeContext ctx) {
    VarTypeRef type = VarTypeRef.CreatePrimitiveType(ctx.simpleType().getText());
    for (int i = 0; i < ctx.arrayDimDecList().arrayDimDec().size(); ++i) {
      type = VarTypeRef.CreateArrayType(type, null);
    }
    type.SetPosition(ctx);
    return type;
  }

  @Override
  public Ast visitVarDec(MgParser.VarDecContext ctx) {
    VarDecList varDecList = new VarDecList();
    varDecList.SetPosition(ctx);
    VarTypeRef varType = (VarTypeRef) visit(ctx.type());
    for (int i = 0; i < ctx.varDeclaratorList().varDeclarator().size(); ++i) {
      VarDec varDec = new VarDec(varType, ctx.varDeclaratorList().varDeclarator(i).Identifier().getText());
      varDec.SetPosition(ctx.varDeclaratorList().varDeclarator(i));
      if (ctx.varDeclaratorList().varDeclarator(i).exp() != null)
        varDec.inital = (Exp) visit(ctx.varDeclaratorList().varDeclarator(i).exp());
      else
        varDec.inital = null;
      varDecList.varDecs.add(varDec);
    }

    if (inFunct)
      return new LocalVarDec(varDecList);
    else if (scope > 0)
      return new FieldDec(varDecList);
    else
      return new GlobalVarDec(varDecList);
  }

  @Override
  public Ast visitVarDecStm(MgParser.VarDecStmContext ctx) {
    return new LocalVarDecStm((LocalVarDec) visit(ctx.varDec()));
  }

  @Override
  public Ast visitIfStm(MgParser.IfStmContext ctx) {
    IfStm ifStm = new IfStm();
    ifStm.SetPosition(ctx);
    ifStm.condition = (Exp) visit(ctx.exp());

    ++scope;
    ifStm.thenBody = (Stm) visit(ctx.then_);
    ifStm.elseBody = (ctx.else_ != null) ? (Stm) visit(ctx.else_) : null;
    --scope;

    return ifStm;
  }

  @Override
  public Ast visitWhileStm(MgParser.WhileStmContext ctx) {
    WhileStm whileStm = new WhileStm();
    whileStm.SetPosition(ctx);
    whileStm.condition = (Exp) visit(ctx.exp());

    ++scope;
    whileStm.whileBody = (Stm) visit(ctx.stm());
    --scope;

    return whileStm;
  }

  @Override
  public Ast visitForStm(MgParser.ForStmContext ctx) {
    ForStm forStm = new ForStm();
    forStm.SetPosition(ctx);
    forStm.forControl = (ForControl) visit(ctx.forControl());

    ++scope;
    forStm.forBody = (Stm) visit(ctx.stm());
    --scope;

    return forStm;
  }

  @Override
  public Ast visitBreakStm(MgParser.BreakStmContext ctx) {
    BreakStm breakStm = new BreakStm();
    breakStm.SetPosition(ctx);
    return breakStm;
  }

  @Override
  public Ast visitContinueStm(MgParser.ContinueStmContext ctx) {
    ContinueStm continueStm = new ContinueStm();
    continueStm.SetPosition(ctx);
    return continueStm;
  }

  @Override
  public Ast visitReturnStm(MgParser.ReturnStmContext ctx) {
    ReturnStm returnStm = new ReturnStm();
    returnStm.SetPosition(ctx);
    if (ctx.exp() != null)
      returnStm.retVal = (Exp) visit(ctx.exp());
    else
      returnStm.retVal = null;
    return returnStm;
  }

  @Override
  public Ast visitBlockStm(MgParser.BlockStmContext ctx) {
    BlockStm blockStm = new BlockStm();
    blockStm.SetPosition(ctx);

    ++scope;
    if (ctx.stm() != null) {
      for (int i = 0; i < ctx.stm().size(); ++i)
        blockStm.children.add((Stm) visit(ctx.stm(i)));
    }
    --scope;

    return blockStm;
  }

  @Override
  public Ast visitEmptyStm(MgParser.EmptyStmContext ctx) {
    EmptyStm emptyStm = new EmptyStm();
    emptyStm.SetPosition(ctx);
    return emptyStm;
  }

  @Override
  public Ast visitForControl(MgParser.ForControlContext ctx) {
    ForControl forControl = new ForControl();
    forControl.SetPosition(ctx);
    if (ctx.forInit() != null) {
      if (ctx.forInit().varDec() != null) {
        forControl.initIsDec = true;
        forControl.initDec = ((LocalVarDec) visit(ctx.forInit().varDec()));
      } else {
        forControl.initIsDec = false;
        for (int i = 0; i < ctx.forInit().expList().exp().size(); ++i)
          forControl.initExps.add((Exp) visit(ctx.forInit().expList().exp(i)));
      }
    }
    forControl.check = (ctx.exp() != null) ? (Exp) visit(ctx.exp()) : null;
    if (ctx.forUpdate() != null)
      for (int i = 0; i < ctx.forUpdate().expList().exp().size(); ++i)
        forControl.updateExps.add((Exp) visit(ctx.forUpdate().expList().exp(i)));
    return forControl;
  }

  @Override
  public Ast visitSuffixExp(MgParser.SuffixExpContext ctx) {
    SuffixExp suffixExp = new SuffixExp((Exp)visit(ctx.exp()), ctx.op.getText());
    suffixExp.SetPosition(ctx);
    return suffixExp;
  }

  @Override
  public Ast visitPrefixExp(MgParser.PrefixExpContext ctx) {
    PrefixExp prefixExp = new PrefixExp((Exp)visit(ctx.exp()), ctx.op.getText());
    prefixExp.SetPosition(ctx);
    return prefixExp;
  }

  @Override
  public Ast visitArithBinaryExp(MgParser.ArithBinaryExpContext ctx) {
    ArithBinaryExp arithBinaryExp = new ArithBinaryExp((Exp) visit(ctx.lhs), (Exp) visit(ctx.rhs), ctx.op.getText());
    arithBinaryExp.SetPosition(ctx);
    return arithBinaryExp;
  }

  @Override
  public Ast visitLogicBinaryExp(MgParser.LogicBinaryExpContext ctx) {
    LogicBinaryExp logicBinaryExp = new LogicBinaryExp((Exp) visit(ctx.lhs), (Exp) visit(ctx.rhs), ctx.op.getText());
    logicBinaryExp.SetPosition(ctx);
    return logicBinaryExp;
  }

  @Override
  public Ast visitAssignExp(MgParser.AssignExpContext ctx) {
    AssignExp assignExp = new AssignExp((Exp) visit(ctx.lhs), (Exp) visit(ctx.rhs));
    assignExp.SetPosition(ctx);
    return assignExp;
  }

  @Override
  public Ast visitPrimitiveExp(MgParser.PrimitiveExpContext ctx) {
    return visit(ctx.primaryExp());
  }

  @Override
  public Ast visitParenPrimaryExp(MgParser.ParenPrimaryExpContext ctx) {
    return visit(ctx.exp());
  }

  @Override
  public Ast visitSelfPrimaryExp(MgParser.SelfPrimaryExpContext ctx) {
    ThisExp thisExp = new ThisExp();
    thisExp.SetPosition(ctx);
    return thisExp;
  }

  @Override
  public Ast visitLiteral(MgParser.LiteralContext ctx) {
    return visit(ctx.getChild(0));
  }

  @Override
  public Ast visitIntegerLiteral(MgParser.IntegerLiteralContext ctx) {
    IntLiteralExp intLiteralExp = new IntLiteralExp(Integer.parseInt(ctx.PosIntegerConstant().getText()));
    intLiteralExp.SetPosition(ctx);
    return intLiteralExp;
  }

  @Override
  public Ast visitStringLiteral(MgParser.StringLiteralContext ctx) {
    StringLiteralExp stringLiteralExp = new StringLiteralExp(ctx.StringConstant().getText());
    stringLiteralExp.SetPosition(ctx);
    return stringLiteralExp;
  }

  @Override
  public Ast visitLogicLiteral(MgParser.LogicLiteralContext ctx) {
    BoolLiteralExp boolLiteralExp = new BoolLiteralExp();
    boolLiteralExp.SetPosition(ctx);
    if (ctx.LogicConstant().getText().equals("true"))
      boolLiteralExp.val = true;
    else
      boolLiteralExp.val = false;
    return boolLiteralExp;
  }

  @Override
  public Ast visitCreatePrimaryExp(MgParser.CreatePrimaryExpContext ctx) {
    return visit(ctx.creator());
  }

  @Override
  public Ast visitCreator(MgParser.CreatorContext ctx) {
    CreationExp creationExp = new CreationExp();
    creationExp.SetPosition(ctx);
    if (ctx.arrayCreatorDim() != null && ctx.arrayCreatorDim().size() != 0) {
      VarTypeRef arrType = VarTypeRef.CreatePrimitiveType(ctx.simpleType().getText());
      // NOTE : for arrayType, the right most dimension is its base dimension, we should create arrayType from right to left.
      for (int i = ctx.arrayCreatorDim().size() - 1; i >= 0; --i) {
        Exp bound = null;
        if (ctx.arrayCreatorDim(i).exp() != null)
          bound = (Exp) visit(ctx.arrayCreatorDim(i).exp());
        arrType = VarTypeRef.CreateArrayType(arrType, bound);
      }
      arrType.SetPosition(ctx);
      creationExp.varTypeRef = arrType;
    } else {
      creationExp.varTypeRef = (VarTypeRef) visit(ctx.simpleType());
    }
    return creationExp;
  }

  @Override
  public Ast visitNullExp(MgParser.NullExpContext ctx) {
    NullExp nullExp = new NullExp();
    nullExp.SetPosition(ctx);
    return nullExp;
  }

  @Override
  public Ast visitVarPrimaryExp(MgParser.VarPrimaryExpContext ctx) {
    VarExp varExp = new VarExp(ctx.Identifier().getText());
    varExp.SetPosition(ctx);
    return varExp;
  }

  @Override
  public Ast visitMemberSel(MgParser.MemberSelContext ctx) {
    FieldAccessExp fieldAccessExp = new FieldAccessExp((Exp) visit(ctx.primaryExp()), ctx.memberName.getText());
    fieldAccessExp.SetPosition(ctx);
    return fieldAccessExp;
  }

  @Override
  public Ast visitMethodSel(MgParser.MethodSelContext ctx) {
    MethodCallExp methodCallExp = new MethodCallExp((Exp) visit(ctx.primaryExp()), ctx.Identifier().getText());
    methodCallExp.SetPosition(ctx);
    if (ctx.arguments().expList() != null) {
      for (int i = 0; i < ctx.arguments().expList().exp().size(); ++i)
        methodCallExp.arguments.args.add((Exp) visit(ctx.arguments().expList().exp(i)));
    }
    return methodCallExp;
  }

  @Override
  public Ast visitArrayAcsExp(MgParser.ArrayAcsExpContext ctx) {
    ArrayAccessExp arrayAccessExp = new ArrayAccessExp((Exp) visit(ctx.primaryExp()), (Exp) visit(ctx.arrayAccessor()));
    arrayAccessExp.SetPosition(ctx);
    return arrayAccessExp;
  }

  @Override
  public Ast visitFunctCallExp(MgParser.FunctCallExpContext ctx) {
    FunctCallExp functCallExp = new FunctCallExp(ctx.Identifier().getText());
    functCallExp.SetPosition(ctx);
    if (ctx.arguments().expList() != null) {
      for (int i = 0; i < ctx.arguments().expList().exp().size(); ++i)
        functCallExp.arguments.args.add((Exp) visit(ctx.arguments().expList().exp(i)));
    }
    return functCallExp;
  }

  @Override
  public Ast visitStm(MgParser.StmContext ctx) {
    return visit(ctx.getChild(0));
  }

  @Override
  public Ast visitExpStm(MgParser.ExpStmContext ctx) {
    ExpStm expStm = new ExpStm((Exp) visit(ctx.exp()));
    expStm.SetPosition(ctx);
    return expStm;
  }

  @Override
  public Ast visitArrayAccessor(MgParser.ArrayAccessorContext ctx) {
    return visit(ctx.exp());
  }
}

