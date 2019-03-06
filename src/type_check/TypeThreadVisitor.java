package type_check;

import ast.node.dec.class_.ClassDec;
import ast.node.dec.function.ConstructDec;
import ast.node.dec.function.FunctDec;
import ast.node.dec.function.MethodDec;
import ast.node.dec.variable.VarDec;
import ast.node.exp.lvalue.ThisExp;
import ast.node.exp.lvalue.VarExp;
import ast.node.prog.Prog;
import ast.node.stm.*;
import ast.typeref.TypeRef;
import ast.typeref.VarTypeRef;
import type_check.symbol_table.Symbol;
import type_check.symbol_table.SymbolTable;

import java.util.Hashtable;
import java.util.Stack;

/**
 * This visitor is an extension from AstTraverseVisitor.
 * It takes the job of variable resolution.
 *
 * It builds indirect threads for all variable to its declaration, which
 * becomes a two-hop association from variable to its actual type entity.
 *
 * The merit of this indirect association is adapting to out-of-order type reference.
 *
 * Because this is essentially a variable resolver, it throws exception under the
 * following circumstances:
 *  1. class, function, variable names are in the same namespace.
 *  2. names cannot be duplicated under the same scope.
 *  3. 'this''s also needs resolving to its own class type.
 *  4. 'returnStm' needs to know the expected returnType of the declared function.
 *  5. pay special attention to constructor's name and returnType.
 *
 * Implementation idea:
 *  1. functions and classes could be put into symbol table altogether, but this
 *    makes the values in symbol table inconsistent. Since functions and classes
 *    won't nest and are always looked-up from global scope (or lookup inside-out
 *    for the specific function/class type), we can put them in separate hashTables,
 *    while the symbol table is mainly used for duplicated name detection.
 *  2. note that it doesn't check the semantic meaning of types -- declaring an undefined
 *    variable, for example, won't raise an exception, because the forward reference is
 *    supported. All of its concerns is literal.
 * */
public class TypeThreadVisitor extends AstTraverseVisitor<Void> {
  /**
   * Create association from class's literal name to its actual classDec.
   * */
  private Hashtable<Symbol, ClassDec> classTable = new Hashtable<>();

  /**
   * Create association from function's literal name to its actual functionDec.
   * */
  private Hashtable<Symbol, FunctDec> functTable = new Hashtable<>();

  /**
   * Two tables need to be passed to the following check phase.
   * */
  public Hashtable<Symbol, ClassDec> getClassTable() {
    return classTable;
  }

  public Hashtable<Symbol, FunctDec> getFunctTable() {
    return functTable;
  }

  /**
   * SymbolTable creates association from identifier literal name to its type-reference,
   * with which the actual classDec could be found in classTable.
   * */
  private SymbolTable<TypeRef> symbolTable = new SymbolTable<>();
  private SymbolTable<VarDec> varAssociator = new SymbolTable<>();
  /**
   * curClass resolve the type of 'this'.
   * TODO : requirement 3. maybe there's a nicer way.
   * */
  private ClassDec curClass = null;

  /**
   * record returnType, check when returnStm is encountered.
   * TODO : requirement 4. function invoking is in the form of a stack.
   * */
  private Stack<VarTypeRef> returnStack = new Stack<>();

  /**
   * Built-in type initialization.
   * */
  private void InitDefaultClass() {
    ClassDec classDec;
    Symbol key;

    classDec = new ClassDec("int");
    key = Symbol.GetSymbol("int");
    classTable.put(key, classDec);
    symbolTable.Put(key, classDec.classTypeName);

    classDec = new ClassDec("bool");
    key = Symbol.GetSymbol("bool");
    classTable.put(key, classDec);
    symbolTable.Put(key, classDec.classTypeName);

    classDec = CreateStringClass();
    key = Symbol.GetSymbol("string");
    classTable.put(key, classDec);
    symbolTable.Put(key, classDec.classTypeName);

    classDec = new ClassDec("void");
    key = Symbol.GetSymbol("void");
    classTable.put(key, classDec);
    symbolTable.Put(key, classDec.classTypeName);

    classDec = new ClassDec("null");
    key = Symbol.GetSymbol("null");
    classTable.put(key, classDec);
    symbolTable.Put(key, classDec.classTypeName);

    classDec = new ClassDec("int");
    key = Symbol.GetSymbol("int");
    classTable.put(key, classDec);

    classDec = CreateArrayClass();
    key = Symbol.GetSymbol("-array");
    classTable.put(key, classDec);
    symbolTable.Put(key, classDec.classTypeName);
  }
  /**
   * Invoked by InitDefaultClass(), string type has complicated built-in methods.
   * */
  private ClassDec CreateStringClass() {
    ClassDec stringClass = new ClassDec("string");

    MethodDec lengthMethod = new MethodDec();
    lengthMethod.functType.typeName = "length";
    lengthMethod.returnType.typeName = "int";
    stringClass.method.add(lengthMethod);
    functTable.put(AddPrefix(stringClass, "length"), lengthMethod);

    MethodDec substringMethod = new MethodDec();
    substringMethod.functType.typeName = "substring";
    substringMethod.returnType.typeName = "string";
    substringMethod.arguments.varDecs.add(new VarDec(new VarTypeRef("int"), "left"));
    substringMethod.arguments.varDecs.add(new VarDec(new VarTypeRef("int"), "right"));
    stringClass.method.add(substringMethod);
    functTable.put(AddPrefix(stringClass, "substring"), substringMethod);

    MethodDec parseIntMethod = new MethodDec();
    parseIntMethod.functType.typeName = "parseInt";
    parseIntMethod.returnType.typeName = "int";
    stringClass.method.add(parseIntMethod);
    functTable.put(AddPrefix(stringClass, "parseInt"), parseIntMethod);

    MethodDec ordMethod = new MethodDec();
    ordMethod.functType.typeName = "ord";
    ordMethod.returnType.typeName = "int";
    ordMethod.arguments.varDecs.add(new VarDec(new VarTypeRef("int"), "pos"));
    stringClass.method.add(ordMethod);
    functTable.put(AddPrefix(stringClass, "ord"), ordMethod);

    return stringClass;
  }

  /**
   * Array has a built-in method called size().
   * It's invisible from other classes.
   * */
  private ClassDec CreateArrayClass() {
    ClassDec arrayClass = new ClassDec("-array");

    MethodDec sizeMethod = new MethodDec();
    sizeMethod.functType.typeName = "size";
    sizeMethod.returnType.typeName = "int";
    arrayClass.method.add(sizeMethod);
    functTable.put(AddPrefix(arrayClass, "size"), sizeMethod);

    return arrayClass;
  }
  /**
   * Built-in function initialization.
   * */
  private void InitDefaultFunct() {
    FunctDec functDec;
    Symbol key;

    functDec = CreatePrintDec();
    key = Symbol.GetSymbol("print");
    functTable.put(key, functDec);
    symbolTable.Put(key, functDec.functType);

    functDec = CreatePrintlnDec();
    key = Symbol.GetSymbol("println");
    functTable.put(key, functDec);
    symbolTable.Put(key, functDec.functType);

    functDec = CreateGetStringDec();
    key = Symbol.GetSymbol("getString");
    functTable.put(key, functDec);
    symbolTable.Put(key, functDec.functType);

    functDec = CreateGetIntDec();
    key = Symbol.GetSymbol("getInt");
    functTable.put(key, functDec);
    symbolTable.Put(key, functDec.functType);

    functDec = CreateToStringDec();
    key = Symbol.GetSymbol("toString");
    functTable.put(key, functDec);
    symbolTable.Put(key, functDec.functType);
  }
  /**
   * Invoked by InitDefaultFunct()
   * */
  private FunctDec CreatePrintDec() {
    FunctDec functDec = new FunctDec();
    functDec.functType.typeName = "print";
    functDec.returnType.typeName = "void";
    functDec.arguments.varDecs.add(new VarDec(new VarTypeRef("string"), "str"));
    return functDec;
  }

  private FunctDec CreatePrintlnDec() {
    FunctDec functDec = new FunctDec();
    functDec.functType.typeName = "println";
    functDec.returnType.typeName = "void";
    functDec.arguments.varDecs.add(new VarDec(new VarTypeRef("string"), "str"));
    return functDec;
  }

  private FunctDec CreateGetStringDec() {
    FunctDec functDec = new FunctDec();
    functDec.functType.typeName = "getString";
    functDec.returnType.typeName = "string";
    return functDec;
  }

  private FunctDec CreateGetIntDec() {
    FunctDec functDec = new FunctDec();
    functDec.functType.typeName = "getInt";
    functDec.returnType.typeName = "int";
    return functDec;

  }

  private FunctDec CreateToStringDec() {
    FunctDec functDec = new FunctDec();
    functDec.functType.typeName = "toString";
    functDec.returnType.typeName = "string";
    functDec.arguments.varDecs.add(new VarDec(new VarTypeRef("int"), "i"));
    return functDec;
  }

  /**
   * Start working!
   * The following are concrete visit actions.
   * */
  public void BuildThread(Prog prog) {
    InitDefaultClass();
    InitDefaultFunct();
    visit(prog);
  }

  /**
   * Check duplicated className.
   * Put in symbol table for duplicated name detection.
   * Put in classTable for class entity lookup.
   * */
  @Override
  public Void visit(ClassDec classDec) {
    Symbol key = Symbol.GetSymbol(classDec.classTypeName.typeName);
    if (symbolTable.ExistCurScope(key))
      throw new RuntimeException("class name has been declared literally in current scope...\n" + classDec.LocationToString());
    symbolTable.Put(key, classDec.classTypeName);
    classTable.put(key, classDec);
    // class creates a brand-new scope.
    symbolTable.BeginScope();
    varAssociator.BeginScope();
    curClass = classDec;

    // builder1 fields first because they can be forwardly referenced.
    for (int i = 0; i < classDec.field.size(); ++i)
      visit(classDec.field.get(i));
    // then, traverse over class methods and constructors.
    if (classDec.constructor != null)
      visit (classDec.constructor);
    for (int i = 0; i < classDec.method.size(); ++i)
      visit(classDec.method.get(i));

    // exit class declaration.
    curClass = null;
    symbolTable.EndScope();
    varAssociator.EndScope();
    return null;
  }


  /**
   * Check duplicated varName.
   * */
  @Override
  public Void visit(VarDec varDec) {
    Symbol key = Symbol.GetSymbol(varDec.varName);
    if (symbolTable.ExistCurScope(key))
      throw new RuntimeException("declared variable name has existed in cur-scope...\n" + varDec.LocationToString());
    if (varDec.inital != null)
      visit(varDec.inital);
    symbolTable.Put(key, varDec.varType);
    varAssociator.Put(key, varDec);
    return null;
  }

  /**
   * Check duplicated functionName.
   * Mark class methods.
   * Note returnStack.
   * */
  @Override
  public Void visit(FunctDec functDec) {
    Symbol key;
    if (functDec instanceof MethodDec)
      key = AddPrefix(curClass, functDec.functType.typeName);
    else
      key = Symbol.GetSymbol(functDec.functType.typeName);
    if (symbolTable.ExistCurScope(key))
      throw new RuntimeException("function name has been declared literally in current scope...\n" + functDec.LocationToString());
//    if (functTable.containsKey(key))
//      throw new RuntimeException("function name has been defined in functionTable...\n" + functDec.LocationToString());
    functTable.put(key, functDec);
    symbolTable.Put(key, functDec.functType);

    symbolTable.BeginScope();
    varAssociator.BeginScope();
    // non-constructor function has explicit returnType.
    if (!(functDec instanceof ConstructDec))
      returnStack.push(functDec.returnType);
    // function's input args are treated like local parameters.
    visit(functDec.arguments);
    for (int i = 0; i < functDec.functBody.size(); ++i)
      visit(functDec.functBody.get(i));

    if (!(functDec instanceof ConstructDec))
      returnStack.pop();
    symbolTable.EndScope();
    varAssociator.EndScope();
    return null;
  }

  /**
   * Pay special attention to constructor name.
   * */
  @Override
  public Void visit(MethodDec methodDec) {
    visit((FunctDec) methodDec);
    if (curClass.classTypeName.typeName.equals(methodDec.functType.typeName))
      throw new RuntimeException("method takes constructor's name...\n" + methodDec.LocationToString());
    return null;
  }

  @Override
  public Void visit(BlockStm blockStm) {
    symbolTable.BeginScope();
    varAssociator.BeginScope();
    for (int i = 0; i < blockStm.children.size(); ++i)
      visit(blockStm.children.get(i));
    symbolTable.EndScope();
    varAssociator.EndScope();
    return null;
  }

  /**
   * Assgin types to 'this' as an expression.
   * */
  @Override
  public Void visit(ThisExp thisExp) {
    thisExp.varTypeRefDec = curClass.classTypeName;
    return null;
  }

  /**
   * Variable resolution.
   * TODO : I don't know whether I will throw exception twice. Ugly.
   * */
  @Override
  public Void visit(VarExp varExp) {
    Symbol key = Symbol.GetSymbol(varExp.varName);
    VarTypeRef symbolTypeRef;
    try {
      symbolTypeRef = (VarTypeRef) symbolTable.Get(key);
    }
    catch (NullPointerException e) {
      throw new RuntimeException("use undefined variable in expression...\n" + varExp.LocationToString());
    }
    if (symbolTypeRef == null)
      throw new RuntimeException("Reference an undefined variable...\n" + varExp.LocationToString());
    varExp.varTypeRefDec = symbolTypeRef;
    varExp.varDec = varAssociator.Get(key);
    return null;
  }

  @Override
  public Void visit(ForStm forStm) {
    symbolTable.BeginScope();
    varAssociator.BeginScope();
    super.visit(forStm);
    symbolTable.EndScope();
    varAssociator.EndScope();
    return null;
  }

  @Override
  public Void visit(WhileStm whileStm) {
    symbolTable.BeginScope();
    varAssociator.BeginScope();
    super.visit(whileStm);
    symbolTable.EndScope();
    varAssociator.EndScope();
    return null;
  }

  @Override
  public Void visit(IfStm ifStm) {
    visit(ifStm.condition);
    symbolTable.BeginScope();
    varAssociator.BeginScope();
    visit(ifStm.thenBody);
    symbolTable.EndScope();
    varAssociator.EndScope();
    if (ifStm.elseBody != null) {
      symbolTable.BeginScope();
      varAssociator.BeginScope();
      visit(ifStm.elseBody);
      symbolTable.EndScope();
      varAssociator.EndScope();
    }
    return null;
  }

  /**
   * Could have multiple returnStms in a function.
   * */
  @Override
  public Void visit(ReturnStm returnStm) {
    returnStm.retType = returnStack.peek();
    super.visit(returnStm);
    return null;
  }
}
