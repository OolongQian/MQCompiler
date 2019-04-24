import antlr_tools.MgLexer;
import antlr_tools.MgParser;
import ast.Builder;
import ast.ParserErrorHandlerStrategy;
import ast.node.Prog;
import ir.BuilderContext;
import ir.IrProg;
import ir.Printer;
import ir.interpreter.Interpreter;
import nasm.AsmBuilder;
import nasm.AsmPrinter;
import opt.SSA;
import opt.optimizers.CopyPropagator;
import opt.optimizers.Defuse;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import semantic.Semanticar;
import java.io.FileInputStream;
import java.io.InputStream;

import static config.Config.DEBUGPRINT_VIRTUAL;
import static config.Config.TEST;

public class Main {
	
	private static void IrBuild (String src_dir, String ir_dir, String nasm_dir) throws Exception {
		InputStream is = new FileInputStream(src_dir);
		ANTLRInputStream input = new ANTLRInputStream(is);
		MgLexer lexer = new MgLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		MgParser parser = new MgParser(tokens);
		parser.setErrorHandler(new ParserErrorHandlerStrategy());
		ParseTree tree = parser.prog();
		
		Builder astBuilder = new Builder();
		Prog prog = (Prog) astBuilder.visit(tree);
		
		Semanticar checker = new Semanticar();
		checker.Config(prog);
		checker.SemanticCheck();
		
		BuilderContext irCtx = new BuilderContext(checker.functTable);
		ir.Builder irBuilder = new ir.Builder(irCtx);
		irBuilder.Build(prog);
		IrProg irProg = irCtx.ir;
		irProg.BuildCFG();
		
		SSA ssaBuilder = new SSA();
		ssaBuilder.BuildSSA(irProg);
		ssaBuilder.OptimSSA(irProg);
		ssaBuilder.DestructSSA(irProg);
//		 because split and copy is used in SSA destruction, reanalyze CFG is needed.
		irProg.BuildCFG();
	  irProg.functs.values().forEach(Defuse::CollectFunctDefuse);
	  CopyPropagator copy = new CopyPropagator();
	  copy.PropagateCopy();
		
		Printer irPrinter = new Printer(ir_dir);
		irProg.Print(irPrinter);
		
		AsmBuilder asmer = new AsmBuilder();
		asmer.TranslateIr(irProg);
		AsmPrinter asmPrinter = new AsmPrinter();
		asmPrinter.ConfigOutput(nasm_dir);
		asmer.Print(asmPrinter);
	}
	
	
	private static void IrInterp (String ir_dir) {
		Interpreter interp = new Interpreter();
		interp.ConfigIO(ir_dir, null);
		interp.Parse();
		interp.Execute();
	}
	
  public static void main(String[] args) throws Exception {
	  if (!TEST) {
	  	if (DEBUGPRINT_VIRTUAL)
		    IrBuild("Mx_src.txt", "Mx_ir.txt", "Mx_nasm_virtual.asm");
		  else
		  	IrBuild("Mx_src.txt", "Mx_ir.txt", "Mx_nasm.asm");
		  IrInterp("Mx_ir.txt");
	  }
	  else {
		  InputStream is = System.in;
		  String inputFile = null;
		  if (args.length > 0) inputFile = args[0];
		  if (inputFile != null) is = new FileInputStream(inputFile);
		
		  ANTLRInputStream input = new ANTLRInputStream(is);
		  MgLexer lexer = new MgLexer(input);
		  CommonTokenStream tokens = new CommonTokenStream(lexer);
		  MgParser parser = new MgParser(tokens);
		  parser.setErrorHandler(new ParserErrorHandlerStrategy());
		  ParseTree tree = parser.prog();
		
		  Builder astBuilder = new Builder();
		  Prog prog = (Prog) astBuilder.visit(tree);
		
		  Semanticar checker = new Semanticar();
		  checker.Config(prog);
		  checker.SemanticCheck();
		
		  // don't output ir in test.
//		  String irFilePath = "ir.txt";
		  BuilderContext irCtx = new BuilderContext(checker.functTable);
		  ir.Builder irBuilder = new ir.Builder(irCtx);
		  irBuilder.Build(prog);
		  IrProg irProg = irCtx.ir;
		  irProg.BuildCFG();
		
		  SSA ssaBuilder = new SSA();
		  ssaBuilder.BuildSSA(irProg);
		  ssaBuilder.OptimSSA(irProg);
		  ssaBuilder.DestructSSA(irProg);
		  irProg.functs.values().forEach(Defuse::CollectFunctDefuse);
		  CopyPropagator copy = new CopyPropagator();
		  copy.PropagateCopy();
		
//		  Printer irPrinter = new Printer(irFilePath);
//		  irProg.Print(irPrinter);
		  
		  AsmBuilder asmer = new AsmBuilder();
		  asmer.TranslateIr(irProg);
		  AsmPrinter asmPrinter = new AsmPrinter();
		  asmPrinter.ConfigOutput(null);
		  asmer.Print(asmPrinter);
	  }
  }
}
