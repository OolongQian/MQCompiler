import antlr_tools.MgLexer;
import antlr_tools.MgParser;
import ast.Builder;
import ast.ParserErrorHandlerStrategy;
import ast.node.Prog;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import semantic.Semanticar;

import java.io.FileInputStream;
import java.io.InputStream;


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
    
//    AstBuilder astBuilder = new AstBuilder();
//    Prog prog = (Prog) astBuilder.visit(tree);
//
//    TypeThreadVisitor typeThreadVisitor = new TypeThreadVisitor();
//    typeThreadVisitor.BuildThread(prog);
//
//    TypeCheckVisitor typeCheckVisitor = new TypeCheckVisitor(typeThreadVisitor);
//    typeCheckVisitor.ExpTypeCheck(prog);
//
//    FormatCheckVisitor formatCheckVisitor = new FormatCheckVisitor(typeCheckVisitor);
//    formatCheckVisitor.FormatCheck(prog);

//    String irFilePath = "ir.txt";
//    BuilderContext irCtx = new BuilderContext(formatCheckVisitor.functTable);
//    Builder irBuilder = new Builder(irCtx);
//    irBuilder.build(prog);
//    Printer irPrinter = new Printer(irFilePath);
//    irCtx.Print(irPrinter);
//
//    SSA ssaOptimizer = new SSA();
//    ssaOptimizer.ConstructSSA(irCtx);
//
//    irPrinter.getFout().println();
//
//    irCtx.Print(irPrinter);
//
//    if (!TEST) {
//      Interpreter interp = new Interpreter();
//      interp.Config(irFilePath, null);
//      interp.Parse();
//      interp.Execute();
//    }

//    IRPrintVisitor irPrintVisitor = new IRPrintVisitor(deprecate);
//    String irStr = irPrintVisitor.IrPrint(deprecate.irCopy);
//    System.out.println(irStr);
  }
}
