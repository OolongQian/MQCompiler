
import antlr_tools.MgLexer;
import antlr_tools.MgParser;
import ast.builder.AstBuilder;
import ast.builder.ParserErrorHandlerStrategy;
import ast.node.prog.Prog;
import ir_codegen_.IRPrintVisitor;
import ir_codegen.builder.IRGenVisitor;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import type_check.FormatCheckVisitor;
import type_check.TypeCheckVisitor;
import type_check.TypeThreadVisitor;

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

    AstBuilder astBuilder = new AstBuilder();
    Prog prog = (Prog) astBuilder.visit(tree);

    System.out.println(prog.PrettyPrint());
    TypeThreadVisitor typeThreadVisitor = new TypeThreadVisitor();
    typeThreadVisitor.BuildThread(prog);

    TypeCheckVisitor typeCheckVisitor = new TypeCheckVisitor(typeThreadVisitor);
    typeCheckVisitor.ExpTypeCheck(prog);

    FormatCheckVisitor formatCheckVisitor = new FormatCheckVisitor(typeCheckVisitor);
    formatCheckVisitor.FormatCheck(prog);

    IRGenVisitor irGenVisitor = new IRGenVisitor(formatCheckVisitor);
    irGenVisitor.IRCodegen(prog);

    IRPrintVisitor irPrintVisitor = new IRPrintVisitor(irGenVisitor.GetIR());
    System.out.println(irPrintVisitor.VisitAndPrintIR());
  }
}
