import antlr_tools.MgLexer;
import antlr_tools.MgParser;
import ast.builder.AstBuilder;
import ast.builder.ParserErrorHandlerStrategy;
import ast.node.prog.Prog;
import ir.Interpreter.Interpreter;
import ir.builder.Builder;
import ir.builder.BuilderContext;
import ir.builder.Printer;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import semantic.FormatCheckVisitor;
import semantic.TypeCheckVisitor;
import semantic.TypeThreadVisitor;

import java.io.FileInputStream;
import java.io.InputStream;

import static ir.builder.Config.TEST;


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

    AstBuilder astBuilder = new AstBuilder();
    Prog prog = (Prog) astBuilder.visit(tree);

    TypeThreadVisitor typeThreadVisitor = new TypeThreadVisitor();
    typeThreadVisitor.BuildThread(prog);

    TypeCheckVisitor typeCheckVisitor = new TypeCheckVisitor(typeThreadVisitor);
    typeCheckVisitor.ExpTypeCheck(prog);

    FormatCheckVisitor formatCheckVisitor = new FormatCheckVisitor(typeCheckVisitor);
    formatCheckVisitor.FormatCheck(prog);

    String irFilePath = "ir.txt";
    BuilderContext irCtx = new BuilderContext(formatCheckVisitor);
    Builder irBuilder = new Builder(irCtx);
    irBuilder.build(prog);
    Printer irPrinter = new Printer(irFilePath);
    irCtx.Print(irPrinter);

    if (!TEST) {
      Interpreter interp = new Interpreter();
      interp.Config(irFilePath, null);
      interp.Parse();
      interp.Execute();
    }

//    IRPrintVisitor irPrintVisitor = new IRPrintVisitor(deprecate);
//    String irStr = irPrintVisitor.IrPrint(deprecate.ir);
//    System.out.println(irStr);
  }
}
