import antlr_tools.MgLexer;
import antlr_tools.MgParser;
import ast.Builder;
import ast.ParserErrorHandlerStrategy;
import ast.node.Prog;
import ir.BuilderContext;
import ir.IrProg;
import ir.Printer;
import ir.interpreter.Interpreter;
import opt.SSA;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import semantic.Semanticar;
import java.io.FileInputStream;
import java.io.InputStream;
import static ir.Config.TEST;

public class Main {
  public static void main(String[] args) throws Exception {
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
	
	  String irFilePath = "ir.txt";
	  BuilderContext irCtx = new BuilderContext(checker.functTable);
	  ir.Builder irBuilder = new ir.Builder(irCtx);
	  irBuilder.Build(prog);
	  IrProg irProg = irCtx.ir;
	  irProg.BuildCFG();

	  SSA ssaBuilder = new SSA();
	  ssaBuilder.BuildSSA(irProg);
	  ssaBuilder.DestructSSA(irProg);
	
	  Printer irPrinter = new Printer(irFilePath);
	  irProg.Print(irPrinter);

	  if (!TEST) {
		  Interpreter interp = new Interpreter();
		  interp.ConfigIO(irFilePath, null);
		  interp.Parse();
		  interp.Execute();
	  }
  }
}
