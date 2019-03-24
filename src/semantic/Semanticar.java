package semantic;

import ast.node.Prog;
import ast.node.dec.*;
import ast.node.exp.*;
import ast.node.stm.*;
import ast.type.Type;
import ast.usage.AstBaseVisitor;
import semantic.symbol_table.SymbolTable;
import java.util.Hashtable;
import static ast.type.Type.CreateBaseType;
import static ast.type.Type.Returnable;
import static semantic.Utility.AddPrefix;

public class Semanticar extends AstBaseVisitor<Void> {
	public Hashtable<String, ClassDec> classTable = new Hashtable<>();
	/**
	 * FunctTable contains functDecs with prefixed names.
	 * */
	public Hashtable<String, FunctDec> functTable = new Hashtable<>();
	/**
	 * For variable resolution and duplicated name detection. */
	private SymbolTable<Dec> symbolTable = new SymbolTable<>();

	// run-time context.
	private ClassDec curClass;
	private FunctDec curMethod;
	
	private Prog ast;
	
	private Type retCache;
	private int loopNest = 0;
	
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
		
		FunctDec lengthMethod = new FunctDec();
		lengthMethod.MarkBuiltIn();
		lengthMethod.setMethod();
		lengthMethod.name = "length";
		lengthMethod.retType = CreateBaseType("int");
		stringClass.methods.add(lengthMethod);
		functTable.put(AddPrefix(stringClass, "length"), lengthMethod);
		
		FunctDec substringMethod = new FunctDec();
		substringMethod.MarkBuiltIn();
		substringMethod.setMethod();
		substringMethod.name = "substring";
		substringMethod.retType = CreateBaseType("string");
		substringMethod.args.add(new VarDec(CreateBaseType("int"), "left"));
		substringMethod.args.add(new VarDec(CreateBaseType("int"), "right"));
		stringClass.methods.add(substringMethod);
		functTable.put(AddPrefix(stringClass, "substring"), substringMethod);
		
		FunctDec parseIntMethod = new FunctDec();
		parseIntMethod.MarkBuiltIn();
		parseIntMethod.setMethod();
		parseIntMethod.name = "parseInt";
		parseIntMethod.retType = CreateBaseType("int");
		stringClass.methods.add(parseIntMethod);
		functTable.put(AddPrefix(stringClass, "parseInt"), parseIntMethod);
		
		FunctDec ordMethod = new FunctDec();
		ordMethod.MarkBuiltIn();
		ordMethod.setMethod();
		ordMethod.name = "ord";
		ordMethod.retType = CreateBaseType("int");
		ordMethod.args.add(new VarDec(CreateBaseType("int"), "pos"));
		stringClass.methods.add(ordMethod);
		functTable.put(AddPrefix(stringClass, "ord"), ordMethod);
		
		lengthMethod.SetParentClass(stringClass);
		substringMethod.SetParentClass(stringClass);
		parseIntMethod.SetParentClass(stringClass);
		ordMethod.SetParentClass(stringClass);
		
		return stringClass;
	}
	
	/**
	 * Array has a built-in methods called size().
	 * It's invisible from other classes.
	 * */
	private ClassDec CreateArrayClass() {
		ClassDec arrayClass = new ClassDec("-array");
		
		FunctDec sizeMethod = new FunctDec();
		sizeMethod.MarkBuiltIn();
		sizeMethod.setMethod();
		sizeMethod.name = "size";
		sizeMethod.retType = CreateBaseType("int");
		arrayClass.methods.add(sizeMethod);
		functTable.put(AddPrefix(arrayClass, "size"), sizeMethod);
		
		sizeMethod.SetParentClass(arrayClass);
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
		functDec.name = "-stringConcat";
		functDec.retType = CreateBaseType("string");
		functDec.args.add(new VarDec(CreateBaseType("string"), "str1"));
		functDec.args.add(new VarDec(CreateBaseType("string"), "str2"));
		return functDec;
	}
	
	private FunctDec CreatePrintDec() {
		FunctDec functDec = new FunctDec();
		functDec.name = "print";
		functDec.retType = CreateBaseType("void");
		functDec.args.add(new VarDec(CreateBaseType("string"), "str"));
		return functDec;
	}
	
	private FunctDec CreatePrintlnDec() {
		FunctDec functDec = new FunctDec();
		functDec.name = "println";
		functDec.retType = CreateBaseType("void");
		functDec.args.add(new VarDec(CreateBaseType("string"), "str"));
		return functDec;
	}
	
	private FunctDec CreateGetStringDec() {
		FunctDec functDec = new FunctDec();
		functDec.name = "getString";
		functDec.retType = CreateBaseType("string");
		return functDec;
	}
	
	private FunctDec CreateGetIntDec() {
		FunctDec functDec = new FunctDec();
		functDec.name = "getInt";
		functDec.retType = CreateBaseType("int");
		return functDec;
		
	}
	
	private FunctDec CreateToStringDec() {
		FunctDec functDec = new FunctDec();
		functDec.name = "toString";
		functDec.retType = CreateBaseType("string");
		functDec.args.add(new VarDec(CreateBaseType("int") , "i"));
		return functDec;
	}
	
	public void Config(Prog prog) {
		ast = prog;
	}
	public void SemanticCheck() {
		InitDefaultClass();
		InitDefaultFunct();
		CollectClass();
		CollectFuncSignature();
		visit(ast);
		
		if (!functTable.containsKey("main"))
			throw new RuntimeException("no main function...\n");
		FunctDec mainDec = functTable.get("main");
		if (!mainDec.retType.isInt())
			throw new RuntimeException("main function has wrong return type...\n");
		if (mainDec.args.size() != 0)
			throw new RuntimeException("main function has arguments...\n");
	}
	
	private void CollectClass() {
		for (Dec dec : ast.decs) {
			if (dec instanceof ClassDec) {
				ClassDec class_ = (ClassDec) dec;
				String key = class_.name;
				if (symbolTable.ExistCurScope(key))
					throw new RuntimeException(
									"class name has been declared literally in current scope...\n" +
													dec.LocationToString());
				symbolTable.Put(key, class_);
				classTable.put(key, class_);
			}
		}
	}
	private void CollectFuncSignature() {
		for (Dec dec : ast.decs) {
			if (dec instanceof ClassDec) {
				ClassDec class_ = (ClassDec) dec;
				for (FunctDec method : class_.methods) {
					String key = AddPrefix(class_, method.name);
					functTable.put(key, method);
				}
			}
			else if (dec instanceof FunctDec) {
				FunctDec funct = (FunctDec) dec;
				functTable.put(funct.name, funct);
			}
		}
	}
	
	@Override
	public Void visit(Prog node) {
		node.decs.forEach(x -> x.Accept(this));
		return null;
	}
	
	@Override
	public Void visit(ClassDec node) {
		// set run time env
		symbolTable.BeginScope();
		curClass = node;
		
		if (node.ctor != null && !node.ctor.name.equals(node.name))
			throw new RuntimeException("ctor name doesn't align with class name" + node.LocationToString());
		
		node.fields.forEach(x -> x.SetParentClass(node));
		node.fields.forEach(x -> x.Accept(this));
		
		for (FunctDec method : node.methods) {
			if (method.name.equals(node.name) && !method.isCtor())
				throw new RuntimeException("method name cannot be the same with class except ctor" + node.LocationToString());
		}
		node.methods.forEach(x -> x.SetParentClass(node));
		node.methods.forEach(x -> x.Accept(this));
		
		curClass = null;
		symbolTable.EndScope();
		return null;
	}
	
	@Override
	public Void visit(VarDec node) {
		if (node.type.GetBaseTypeName().equals("void"))
			throw new RuntimeException("dec a void...\n" + node.LocationToString());
		if (symbolTable.ExistCurScope(node.name))
			throw new RuntimeException(
							"declared variable name has existed in cur-scope...\n" +
											node.LocationToString());
		
		if (node.inital != null)
			node.inital.Accept(this);
		
		if (node.type.isVoid())
			throw new RuntimeException("cannot initialize void type...\n" + node.LocationToString());
		if (!classTable.containsKey(node.type.GetBaseTypeName()))
			throw new RuntimeException("declare undefined type...\n" + node.LocationToString());
		if (node.inital != null && !node.type.Assignable(node.inital.type))
			throw new RuntimeException("variable declaration has incompatible initialization...\n" + node.LocationToString());
		
		symbolTable.Put(node.name, node);
		return null;
	}
	
	@Override
	public Void visit(FunctDec node) {
		String key = node.isMethod() ?
						AddPrefix(node.GetParentClass(), node.name) :
						node.name;
		
		// prefix is necessary because class can shadow methods with the same name.
		if (symbolTable.ExistCurScope(key))
			throw new RuntimeException(
							"function name has been declared literally in current scope...\n" +
											node.LocationToString());
		symbolTable.Put(key, node);
//		functTable.put(key, node);
		
		symbolTable.BeginScope();
		curMethod = (node.isMethod()) ? node : null;
		retCache = node.retType;
		node.args.forEach(x -> x.Accept(this));
		node.body.forEach(x -> x.Accept(this));
		if (!classTable.containsKey(node.retType.GetBaseTypeName()))
			throw new RuntimeException("function has undefined return type...\n" + node.LocationToString());
		retCache = null;
		curMethod = null;
		symbolTable.EndScope();
		
		return null;
	}
	
	@Override
	public Void visit(BlockStm node) {
		symbolTable.BeginScope();
		node.stms.forEach(x -> x.Accept(this));
		symbolTable.EndScope();
		return null;
	}
	
	// TODO : this...
	@Override
	public Void visit(ThisExp node) {
		node.type = CreateBaseType(curClass.name);
		node.SetThisMethod(curMethod);
		node.lValue = true;
		return null;
	}
	
	@Override
	public Void visit(VarExp node) {
		Dec dec;
		try {
			dec = symbolTable.Get(node.name);
		} catch (NullPointerException e) {
			throw new RuntimeException(
							"use undefined variable in expression...\n" +
											node.LocationToString());
		}
		if (dec == null) {
			throw new RuntimeException(
							"Reference an undefined variable...\n" +
											node.LocationToString());
		}
		if (!(dec instanceof VarDec)) {
			throw new RuntimeException(
							"I'd better go and die...\n" +
											node.LocationToString());
		}
		node.type = ((VarDec) dec).type;
		node.resolve = (VarDec) dec;
		
		node.lValue = true;
		return null;
	}
	
	@Override
	public Void visit(ForStm node) {
		symbolTable.BeginScope();
		
		if (node.initIsDec) {
			node.initDec.forEach(x -> x.Accept(this));
		}
		else {
			node.initExps.forEach(x -> x.Accept(this));
		}
		if (node.check != null) {
			visit(node.check);
			if (!node.check.type.isBool())
				throw new RuntimeException("for condition isn't bool...\n" + node.LocationToString());
		}
		node.updateExps.forEach(x -> x.Accept(this));
		
		++loopNest;
		node.body.Accept(this);
		--loopNest;
		symbolTable.EndScope();
		return null;
	}
	
	@Override
	public Void visit(WhileStm node) {
		visit(node.cond);
		if (!node.cond.type.isBool())
			throw new RuntimeException("If condition isn't bool...\n" + node.LocationToString());
		
		symbolTable.BeginScope();
		++loopNest;
		visit(node.body);
		--loopNest;
		symbolTable.EndScope();
		
		return null;
	}
	
	@Override
	public Void visit(BreakStm node) {
		if (loopNest <= 0)
			throw new RuntimeException("cannot break outside a loop...\n" + node.LocationToString());
		return null;
	}
	
	@Override
	public Void visit(ContinueStm node) {
		if (loopNest <= 0)
			throw new RuntimeException("cannot break outside a loop...\n" + node.LocationToString());
		return null;
	}
	
	@Override
	public Void visit(ReturnStm node) {
		node.retType = retCache;
		if (node.ret != null) {
			visit(node.ret);
			if (!Returnable(node.retType, node.ret.type))
				throw new RuntimeException("return value doesn't match return type...\n" + node.LocationToString());
		}
		else {
			if (!node.retType.isVoid())
				throw new RuntimeException("return value doesn't match return type...\n" + node.LocationToString());
		}
		return null;
	}
	
	@Override
	public Void visit(IfStm node) {
		visit(node.cond);
		if (!node.cond.type.isBool())
			throw new RuntimeException("If condition isn't bool...\n" + node.LocationToString());
		
		symbolTable.BeginScope();
		visit(node.thenBody);
		symbolTable.EndScope();
		
		if (node.elseBody != null) {
			symbolTable.BeginScope();
			visit(node.elseBody);
			symbolTable.EndScope();
		}
		return null;
	}
	
	@Override
	public Void visit(ArithBinaryExp node) {
		node.lhs.Accept(this);
		node.rhs.Accept(this);
		Type lhs = node.lhs.type;
		Type rhs = node.rhs.type;
		
		switch (node.op) {
			// arithmetic
			case "*": case "/":
			case "-": case "%":
			case "<<": case ">>":
				if (!(lhs.isInt() && rhs.isInt()))
					throw new RuntimeException("ArithBinaryExp type error...\n" + node.LocationToString());
				node.type = CreateBaseType("int");
				break;
			case "+":
				if (!(lhs.isInt() && rhs.isInt() || lhs.isString() && rhs.isString()))
					throw new RuntimeException("ArithBinaryExp type error...\n" + node.LocationToString());
				node.type = lhs;
				break;
			case ">": case ">=":
			case "<": case "<=":
				if (!(lhs.isInt() && rhs.isInt() || lhs.isString() && rhs.isString()))
					throw new RuntimeException("ArithBinaryExp type error...\n" + node.LocationToString());
				node.type = CreateBaseType("bool");
				break;
			case "==": case "!=":
				if (!lhs.Assignable(rhs))
					throw new RuntimeException("ArithBinaryExp type error...\n" + node.LocationToString());
				node.type = CreateBaseType("bool");
				break;
			case "&": case "^": case "|":
				if (!(lhs.isInt() && rhs.isInt() || lhs.isBool() && rhs.isBool()))
					throw new RuntimeException("ArithBinaryExp type error...\n" + node.LocationToString());
				node.type = lhs;
				break;
			default:
				throw new RuntimeException("Parser error...\n" + node.LocationToString());
		}
		return null;
	}
	
	@Override
	public Void visit(LogicBinaryExp node) {
		node.lhs.Accept(this);
		node.rhs.Accept(this);
		Type lhs = node.lhs.type;
		Type rhs = node.rhs.type;
		
		if (!(lhs.isBool() && rhs.isBool()))
			throw new RuntimeException("logicBinaryExp type error...\n" + node.LocationToString());
		node.type = CreateBaseType("bool");
		return null;
	}
	
	@Override
	public Void visit(PrefixExp node) {
		node.obj.Accept(this);
		switch (node.op) {
			case "!":
				if (!node.obj.type.isBool())
					throw new RuntimeException("node type error...\n" + node.LocationToString());
				break;
			case "~":
				if (!(node.obj.type.isBool() || node.obj.type.isInt()))
					throw new RuntimeException("node type error...\n" + node.LocationToString());
				break;
			case "-" :
				if (!node.obj.type.isInt())
					throw new RuntimeException("node type error...\n" + node.LocationToString());
				break;
			case "++": case "--":
				if (!node.obj.type.isInt())
					throw new RuntimeException("node type error...\n" + node.LocationToString());
				// NOTE LV
				node.lValue = true;
				break;
			default:
				throw new RuntimeException("encounter unknown prefix...\n" + node.LocationToString());
		}
		if ((node.op.equals("++") || node.op.equals("--")) && !(node.obj.lValue))
			throw new RuntimeException("node " + node.op + " need to reference lvalue...\n" + node.LocationToString());
		node.type = node.obj.type;
		return null;
	}
	
	@Override
	public Void visit(SuffixExp node) {
		node.obj.Accept(this);
		if (!node.obj.type.isInt())
			throw new RuntimeException("node type error...\n" + node.LocationToString());
		if (!node.obj.lValue)
			throw new RuntimeException("suffixExp " + node.op + " need to reference lvalue...\n" + node.LocationToString());
		node.type = node.obj.type;
		return null;
	}
	
	@Override
	public Void visit(FunctCallExp node) {
		node.args.forEach(x -> x.Accept(this));
		
		if (curClass != null) {
			FunctDec method = curClass.FindMethod(node.funcName);
			node.funcTableKey = (method != null) ?
							AddPrefix(curClass, method.name) : node.funcName;
		} else {
			node.funcTableKey = node.funcName;
		}
		
		if (!functTable.containsKey(node.funcTableKey)) {
			throw new RuntimeException(
							"invoke undefined function inside class, not method nor function...\n" +
											node.LocationToString());
		}
		
		FunctDec funcDec = functTable.get(node.funcTableKey);
		if (funcDec.args.size() != node.args.size()) {
			throw new RuntimeException(
							"arguments number doesn't much during method invoking...\n" +
											node.LocationToString());
		}
		for (int i = 0; i < funcDec.args.size(); ++i) {
			Type formal = funcDec.args.get(i).type;
			Type para = node.args.get(i).type;
			if (!formal.Assignable(para)) {
				throw new RuntimeException(
								i + "th argument type doesn't much method signature...\n" +
												node.LocationToString());
			}
		}
		node.type = funcDec.retType;
		node.setFuncResolve(funcDec);
		return null;
	}
	
	@Override
	public Void visit(MethodCallExp node) {
		node.obj.Accept(this);
		node.args.forEach(x -> x.Accept(this));
		
		ClassDec objClass;
		FunctDec methodDec;
		if (node.obj.type.isArray()) {
			objClass = classTable.get("-array");
		}
		else {
			objClass = classTable.get(node.obj.type.typeName);
		}
		
		node.funcTableKey = AddPrefix(objClass, node.funcName);
		methodDec = functTable.get(node.funcTableKey);
		if (methodDec == null)
			throw new RuntimeException("access undefined method...\n" + node.LocationToString());
		
		if (methodDec.args.size() != node.args.size())
			throw new RuntimeException("arguments number doesn't much during method invoking...\n" + node.LocationToString());
		for (int i = 0; i < methodDec.args.size(); ++i) {
			Type formal = methodDec.args.get(i).type;
			Type para = node.args.get(i).type;
			if (!formal.Assignable(para))
				throw new RuntimeException(i + "th argument type doesn't much method signature...\n" + node.LocationToString());
		}
		// NOTE : methodCall is linking methodDec and classDec
		// FIXME : pay attention to constructor.
		node.type = methodDec.retType;
		node.setClassResolve(objClass);
		node.setFuncResolve(methodDec);
		return null;
	}
	
	@Override
	public Void visit(FieldAccessExp node) {
		node.obj.Accept(this);
		ClassDec class_ = classTable.get(node.obj.type.typeName);
		VarDec fieldDec = class_.FindField(node.memberName);
		if (fieldDec == null)
			throw new RuntimeException(
							"object instance tries to access undefined reg field...\n" +
											node.LocationToString());
		
		// NOTE : record class and field declaration on fieldAccessExp.
		node.type = fieldDec.type;
		node.setClassResolve(class_);
		node.setFieldResolve(fieldDec);
		// NOTE LV
		node.lValue = true;
		return null;
	}
	
	@Override
	public Void visit(ArrayAccessExp node) {
		node.arr.Accept(this);
		node.subscript.Accept(this);
		
		if (!node.subscript.type.isInt())
			throw new RuntimeException("array accessor index isn't integer...\n" + node.LocationToString());
		if (!node.arr.type.isArray())
			throw new RuntimeException("Access non-array type by indexing...\n" + node.LocationToString());
		node.type = node.arr.type.innerType;
		
		// NOTE LV
		node.lValue = true;
		return null;
	}
	
	@Override
	public Void visit(AssignExp node) {
		node.dst.Accept(this);
		node.src.Accept(this);
		
		if (node.dst.getClass() == ThisExp.class)
			throw new RuntimeException("This cannot be assigned...\n" + node.LocationToString());
		if (!node.dst.type.Assignable(node.src.type))
			throw new RuntimeException("Incompatible assignment types...\n" + node.LocationToString());
		if (!node.dst.lValue)
			throw new RuntimeException("cannot assign a non-lvalue...\n" + node.LocationToString());
		return null;
	}
	
	
	private void TypeDimEval(Type type) {
		Type cur = type;
		while (cur.innerType != null) {
			if (cur.dim != null) cur.dim.Accept(this);
			cur = cur.innerType;
		}
	}
	private void CheckDimFormat(CreationExp node) {
		if (node.type.isArray()) {
			Type tmp = node.type;
			boolean indexed = true;
			boolean allNull = true;
			
			while (tmp != null) {
				if (!indexed && tmp.dim != null) {
					throw new RuntimeException("array creation dimension isn't continuous...\n" + node.LocationToString());
				}
				if (indexed && tmp.dim == null) {
					indexed = false;
				}
				if (tmp.dim != null) {
					allNull = false;
				}
				tmp = tmp.innerType;
			}
			if (allNull)
				throw new RuntimeException("array dimension initializer expected...\n" + node.LocationToString());
		}
	}
	
	@Override
	public Void visit(CreationExp node) {
		TypeDimEval(node.type);
		CheckDimFormat(node);
		if (node.type.GetBaseTypeName().equals("void"))
			throw new RuntimeException("new a void...\n" + node.LocationToString());
		if (node.type.isArray()) {
			Type tmp = node.type;
			while (tmp.isArray()) {
				if (tmp.dim != null && !tmp.dim.type.isInt()) {
					throw new RuntimeException("create non-integer dimension array...\n" + node.LocationToString());
				}
				tmp = tmp.innerType;
			}
		}
		
		return null;
	}
	
	@Override
	public Void visit(LocalVarDecStm node) {
		node.varDecs.forEach(x -> x.Accept(this));
		return null;
	}
	
	@Override
	public Void visit(VarDecList node) {
		node.varDecs.forEach(x -> x.Accept(this));
		return null;
	}
	
	@Override
	public Void visit(NullExp node) {
		return null;
	}
	
	@Override
	public Void visit(ExpStm node) {
		node.exp.Accept(this);
		return null;
	}
}
