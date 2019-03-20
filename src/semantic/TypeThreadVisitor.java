package semantic;

import ast.node.dec.Dec;
import ast.node.dec.class_.ClassDec;
import ast.node.dec.function.ConstructDec;
import ast.node.dec.function.FunctDec;
import ast.node.dec.function.MethodDec;
import ast.node.dec.variable.VarDec;
import ast.node.exp.lvalue.ThisExp;
import ast.node.exp.lvalue.VarExp;
import ast.node.prog.Prog;
import ast.node.stm.*;
import ast.typeref.VarTypeRef;
import semantic.symbol_table.SymbolTable;

import java.util.Hashtable;
import java.util.Stack;

import static ast.typeref.VarTypeRef.CreatePrimitiveType;

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
  public Hashtable<String, ClassDec> classTable = new Hashtable<>();

  /**
   * Create association from function's literal name to its actual functionDec.
   * */
  public Hashtable<String, FunctDec> functTable = new Hashtable<>();


  /**
   * SymbolTable creates association from identifier literal name to its type-reference,
   * with which the actual classDec could be found in classTable.
   * */
  private SymbolTable<Dec> symbolTable = new SymbolTable<>();

  /**
   * record returnType, check when returnStm is encountered.
   * TODO : requirement 4. function invoking is in the form of a stack.
   * */
  private Stack<VarTypeRef> returnStack = new Stack<>();

  /**
   * curClass resolve the type of 'this'.
   * TODO : requirement 3. maybe there's a nicer way.
   * */
  private ClassDec curClass = null;
  
  private MethodDec curMethod = null;


  /**
   * Built-in type initialization.
   * */
  private void InitDefaultClass() {
    ClassDec classDec;
    String key;

    classDec = new ClassDec("int");
    key = "int";
    classTable.put(key, classDec);
    symbolTable.Put(key, classDec);
    classDec.MarkBuiltIn();

    classDec = new ClassDec("bool");
    key = "bool";
    classTable.put(key, classDec);
    symbolTable.Put(key, classDec);
    classDec.MarkBuiltIn();

    classDec = CreateStringClass();
    key = "string";
    classTable.put(key, classDec);
    symbolTable.Put(key, classDec);
    classDec.MarkBuiltIn();

    classDec = new ClassDec("void");
    key = "void";
    classTable.put(key, classDec);
    symbolTable.Put(key, classDec);
    classDec.MarkBuiltIn();

    classDec = new ClassDec("null");
    key = "null";
    classTable.put(key, classDec);
    symbolTable.Put(key, classDec);
    classDec.MarkBuiltIn();

    classDec = CreateArrayClass();
    key = "-array";
    classTable.put(key, classDec);
    symbolTable.Put(key, classDec);
    classDec.MarkBuiltIn();
  }

  /**
   * Invoked by InitDefaultClass(), string type has complicated built-in methods.
   * */
  private ClassDec CreateStringClass() {
    ClassDec stringClass = new ClassDec("string");

    MethodDec lengthMethod = new MethodDec();
	  lengthMethod.MarkBuiltIn();
	  lengthMethod.functName = "length";
    lengthMethod.returnType = CreatePrimitiveType("int");
    stringClass.method.add(lengthMethod);
    functTable.put(AddPrefix(stringClass, "length"), lengthMethod);

    MethodDec substringMethod = new MethodDec();
    substringMethod.MarkBuiltIn();
    substringMethod.functName = "substring";
    substringMethod.returnType = CreatePrimitiveType("string");
    substringMethod.arguments.varDecs.add(new VarDec(CreatePrimitiveType("int"), "left"));
    substringMethod.arguments.varDecs.add(new VarDec(CreatePrimitiveType("int"), "right"));
    stringClass.method.add(substringMethod);
    functTable.put(AddPrefix(stringClass, "substring"), substringMethod);

    MethodDec parseIntMethod = new MethodDec();
    parseIntMethod.MarkBuiltIn();
    parseIntMethod.functName = "parseInt";
    parseIntMethod.returnType = CreatePrimitiveType("int");
    stringClass.method.add(parseIntMethod);
    functTable.put(AddPrefix(stringClass, "parseInt"), parseIntMethod);

    MethodDec ordMethod = new MethodDec();
    ordMethod.MarkBuiltIn();
    ordMethod.functName = "ord";
    ordMethod.returnType = CreatePrimitiveType("int");
    ordMethod.arguments.varDecs.add(new VarDec(CreatePrimitiveType("int"), "pos"));
    stringClass.method.add(ordMethod);
    functTable.put(AddPrefix(stringClass, "ord"), ordMethod);

    lengthMethod.parentClass = stringClass;
    substringMethod.parentClass = stringClass;
    parseIntMethod.parentClass = stringClass;
    ordMethod.parentClass = stringClass;

    return stringClass;
  }

  /**
   * Array has a built-in method called size().
   * It's invisible from other classes.
   * */
  private ClassDec CreateArrayClass() {
    ClassDec arrayClass = new ClassDec("-array");

    MethodDec sizeMethod = new MethodDec();
    sizeMethod.MarkBuiltIn();
    sizeMethod.functName = "size";
    sizeMethod.returnType = CreatePrimitiveType("int");
    arrayClass.method.add(sizeMethod);
    functTable.put(AddPrefix(arrayClass, "size"), sizeMethod);

    sizeMethod.parentClass = arrayClass;
    return arrayClass;
  }
  /**
   * Built-in function initialization.
   * */
  private void InitDefaultFunct() {
    FunctDec functDec;
    String key;

    functDec = CreatePrintDec();
    key = "print";
    functTable.put(key, functDec);
    symbolTable.Put(key, functDec);
    functDec.MarkBuiltIn();

    functDec = CreatePrintlnDec();
    key = "println";
    functTable.put(key, functDec);
    symbolTable.Put(key, functDec);
    functDec.MarkBuiltIn();

    functDec = CreateGetStringDec();
    key = "getString";
    functTable.put(key, functDec);
    symbolTable.Put(key, functDec);
    functDec.MarkBuiltIn();

    functDec = CreateGetIntDec();
    key = "getInt";
    functTable.put(key, functDec);
    symbolTable.Put(key, functDec);
    functDec.MarkBuiltIn();

    functDec = CreateToStringDec();
    key = "toString";
    functTable.put(key, functDec);
    symbolTable.Put(key, functDec);
    functDec.MarkBuiltIn();

    functDec = CreateStringConcatDec();
    key = "-stringConcat";
    functTable.put(key, functDec);
    symbolTable.Put(key, functDec);
    functDec.MarkBuiltIn();
  }
  /**
   * Invoked by InitDefaultFunct()
   * */
  private FunctDec CreateStringConcatDec() {
    FunctDec functDec = new FunctDec();
    functDec.functName = "-stringConcat";
    functDec.returnType = CreatePrimitiveType("string");
    functDec.arguments.varDecs.add(new VarDec(CreatePrimitiveType("string"), "str1"));
    functDec.arguments.varDecs.add(new VarDec(CreatePrimitiveType("string"), "str2"));
    return functDec;
  }

  private FunctDec CreatePrintDec() {
    FunctDec functDec = new FunctDec();
    functDec.functName = "print";
    functDec.returnType = CreatePrimitiveType("void");
    functDec.arguments.varDecs.add(new VarDec(CreatePrimitiveType("string"), "str"));
    return functDec;
  }

  private FunctDec CreatePrintlnDec() {
    FunctDec functDec = new FunctDec();
    functDec.functName = "println";
    functDec.returnType = CreatePrimitiveType("void");
    functDec.arguments.varDecs.add(new VarDec(CreatePrimitiveType("string"), "str"));
    return functDec;
  }

  private FunctDec CreateGetStringDec() {
    FunctDec functDec = new FunctDec();
    functDec.functName = "getString";
    functDec.returnType = CreatePrimitiveType("string");
    return functDec;
  }

  private FunctDec CreateGetIntDec() {
    FunctDec functDec = new FunctDec();
    functDec.functName = "getInt";
    functDec.returnType = CreatePrimitiveType("int");
    return functDec;

  }

  private FunctDec CreateToStringDec() {
    FunctDec functDec = new FunctDec();
    functDec.functName = "toString";
    functDec.returnType = CreatePrimitiveType("string");
    functDec.arguments.varDecs.add(new VarDec(CreatePrimitiveType("int") , "i"));
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
    // detect duplicated class name.
    String key = classDec.GetClassName();
    if (symbolTable.ExistCurScope(key))
      throw new RuntimeException("class name has been declared literally in current scope...\n" + classDec.LocationToString());
    // put
    symbolTable.Put(key, classDec);
    classTable.put(key, classDec);

    // class creates a brand-new scope.
    symbolTable.BeginScope();
    curClass = classDec;

    // builder1 fields first because they can be forwardly referenced.
    classDec.fields.forEach(x -> x.parentClass = classDec);
    classDec.fields.forEach(x -> x.Accept(this));

    // then, traverse over class methods and constructors.
    if (classDec.constructor != null) {
      classDec.constructor.parentClass = classDec;
      classDec.constructor.Accept(this);
    }

    classDec.method.forEach(x -> x.parentClass = classDec);
    classDec.method.forEach(x -> x.Accept(this));

    // exit class declaration.
    curClass = null;
    symbolTable.EndScope();

    return null;
  }

  /**
   * Check duplicated varName.
   * */
  @Override
  public Void visit(VarDec varDec) {
    // associate varDec with parent class, if this var is a field of that class.
    String key = varDec.varName;
    if (symbolTable.ExistCurScope(key))
      throw new RuntimeException("declared variable name has existed in cur-scope...\n" + varDec.LocationToString());

    if (varDec.inital != null)
      visit(varDec.inital);

    symbolTable.Put(key, varDec);
    return null;
  }

  /**
   * Check duplicated functionName.
   * Mark class methods.
   * Note returnStack.
   * */
  @Override
  public Void visit(FunctDec node) {
    // smash functionDec name with its class, if it's a method.
    String key;
    if (node instanceof MethodDec) {
      key = AddPrefix(curClass, node.GetFunctName());
    } else {
      key = node.GetFunctName();
    }
    // check whether has duplicated name.
    if (symbolTable.ExistCurScope(key))
      throw new RuntimeException("function name has been declared literally in current scope...\n" + node.LocationToString());
    symbolTable.Put(key, node);
    functTable.put(key, node);

    symbolTable.BeginScope();
    // non-constructor function has explicit returnType.
    if (!(node instanceof ConstructDec))
      returnStack.push(node.returnType);

    // function's input args are treated like local parameters.
    visit(node.arguments);

    node.functBody.forEach(x -> x.Accept(this));

    if (!(node instanceof ConstructDec))
      returnStack.pop();
    symbolTable.EndScope();
    return null;
  }

  /**
   * Pay special attention to constructor name.
   * */
  @Override
  public Void visit(MethodDec methodDec) {
    curMethod = methodDec;
    visit((FunctDec) methodDec);
    if (methodDec.GetFunctName().equals((methodDec.parentClass.GetClassName())))
      throw new RuntimeException("method takes constructor's name...\n" + methodDec.LocationToString());
    curMethod = null;
    return null;
  }

  @Override
  public Void visit(BlockStm blockStm) {
    symbolTable.BeginScope();
    blockStm.children.forEach(x -> x.Accept(this));
    symbolTable.EndScope();
    return null;
  }

  /**
   * Assgin types to 'this' as an expression.
   * */
  @Override
  public Void visit(ThisExp thisExp) {
    thisExp.varTypeRef = curClass.classType;
    thisExp.thisMethod = curMethod;
    // NOTE LV
    thisExp.lValue = true;
    return null;
  }


  /**
   * Variable resolution.
   * TODO : I don't know whether I will throw exception twice. Ugly.
   * */
  @Override
  public Void visit(VarExp varExp) {

    String key = varExp.varName;
    Dec dec;

    // ugly error check...
    try {
      dec = symbolTable.Get(key);
    }
    catch (NullPointerException e) {
      throw new RuntimeException("use undefined variable in expression...\n" + varExp.LocationToString());
    }
    if (dec == null)
      throw new RuntimeException("Reference an undefined variable...\n" + varExp.LocationToString());
    if (!(dec instanceof VarDec))
      throw new RuntimeException("I'd better go and die...\n" + varExp.LocationToString());

    // record declaration information onto varExp.
    VarDec varDec = (VarDec) dec;
    varExp.varDec = varDec;
    varExp.varTypeRef = varDec.varType;

    // NOTE LV
    varExp.lValue = true;
    return null;
  }




  @Override
  public Void visit(ForStm forStm) {
    symbolTable.BeginScope();
    super.visit(forStm);
    symbolTable.EndScope();
    return null;
  }
  @Override
  public Void visit(WhileStm whileStm) {
    symbolTable.BeginScope();
    super.visit(whileStm);
    symbolTable.EndScope();
    return null;
  }

  @Override
  public Void visit(IfStm ifStm) {
    visit(ifStm.condition);

    symbolTable.BeginScope();
    visit(ifStm.thenBody);
    symbolTable.EndScope();

    if (ifStm.elseBody != null) {
      symbolTable.BeginScope();
      visit(ifStm.elseBody);
      symbolTable.EndScope();
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
