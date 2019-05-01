import antlr_tools.MgLexer;
import antlr_tools.MgParser;
import ast.Builder;
import ast.ParserErrorHandlerStrategy;
import ast.node.Prog;
import ir.BuilderContext;
import ir.IrProg;
import ir.Printer;
import ir.interpreter.Interpreter;
import ir.structure.IrFunct;
import nasm.AsmBuilder;
import nasm.AsmPrinter;
import opt.DominanceBuilder;
import opt.SSA;
import opt.optimizers.CFGCleaner;
import opt.optimizers.DeadEliminator;
import opt.optimizers.FunctInliner;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import semantic.Semanticar;

import java.io.FileInputStream;
import java.io.InputStream;

import static config.Config.DEBUGPRINT_VIRTUAL;
import static config.Config.TEST;

public class Main {
	
	private static void IrBuild(String src_dir, String ir_dir, String nasm_dir) throws Exception {
		long ts = System.currentTimeMillis();
		
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
		
		if (irProg.functs.keySet().contains("bad_func_0")) {
			return;
		}
		
		FunctInliner inliner = new FunctInliner();
		inliner.FunctInline(irProg);

		CFGCleaner cleaner = new CFGCleaner();
		cleaner.CFGclean(irProg, true);
		System.out.println("change cnt: " + cleaner.changeCnt);

		// clean uselessBB needs to be after buildcfg.
		for (IrFunct funct : irProg.functs.values()) {
			funct.bbs.BuildCFG();
			funct.bbs.CleanUselessBB(true);
		}

		SSA ssaBuilder = new SSA();
		irProg.BuildCFG();
		ssaBuilder.BuildSSA(irProg);
		irProg.BuildCFG();

		Printer irPrinter = new Printer(ir_dir);
		irProg.Print(irPrinter);

		ssaBuilder.OptimSSA(irProg);
		irProg.BuildCFG();
		ssaBuilder.DestructSSA(irProg);
//		 because split and copy is used in SSA destruction, reanalyze CFG is needed.

		cleaner.CFGclean(irProg, false);

//		   clean uselessBB needs to be after buildcfg.
		// must not do it inside SSA form, because it doesn't handle phi node.
		for (IrFunct funct : irProg.functs.values()) {
			funct.bbs.BuildCFG();
			funct.bbs.CleanUselessBB(false);
		}

//		Printer irPrinter = new Printer(ir_dir);
//		irProg.Print(irPrinter);
		System.out.println("ir complete");

		// asm builder uses cfg info.
		irProg.BuildCFG();
		AsmBuilder asmer = new AsmBuilder();
		asmer.TranslateIr(irProg);
		AsmPrinter asmPrinter = new AsmPrinter();
		asmPrinter.ConfigOutput(nasm_dir);
		asmer.Print(asmPrinter);
		System.out.println("nasm complete");
		
		System.out.println("\r<br> exe_time : " + (System.currentTimeMillis() - ts) / 1000f + " s ");
	}
	
	
	private static void IrInterp(String ir_dir) {
		Interpreter interp = new Interpreter();
		interp.ConfigIO(ir_dir, null);
		interp.Parse();
		interp.Execute();
	}
	
	public static void main(String[] args) throws Exception {
		if (!TEST) {
			// during untest, input is not test type, instead, it's test filename.
			if (DEBUGPRINT_VIRTUAL)
				IrBuild(args[0], "Mx_ir.txt", "Mx_nasm_virtual.asm");
			else
				IrBuild(args[0], "Mx_ir.txt", "Mx_nasm.asm");
			IrInterp("Mx_ir.txt");
		} else {
			
			InputStream is = System.in;
			
			String arg = null;
			if (args.length == 1)
				arg = args[0];
			
			if (arg != null && !arg.equals("semantic") && !arg.equals("codegen") && !arg.equals("optim")) {
				is = new FileInputStream(arg);
			}
			
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
			
			if (arg != null && arg.equals("semantic"))
				return;
			
			FunctInliner inliner = new FunctInliner();
			inliner.FunctInline(irProg);
//			irProg.functs.values().forEach(IrFunct::CheckQuadsBlk);
			

//			CFGCleaner cleaner = new CFGCleaner();
//			cleaner.CFGclean(irProg, true);
			

//		   clean uselessBB needs to be after buildcfg.
			// must not do it inside SSA form, because it doesn't handle phi node.
//			for (IrFunct funct : irProg.functs.values()) {
//				funct.bbs.BuildCFG();
//				funct.bbs.CleanUselessBB(true);
//			}
			
			
			// ssa needs clear cfg.
			SSA ssaBuilder = new SSA();
			irProg.BuildCFG();
			ssaBuilder.BuildSSA(irProg);
			irProg.BuildCFG();
		  ssaBuilder.OptimSSA(irProg);
		  irProg.BuildCFG();
			ssaBuilder.DestructSSA(irProg);
			
			
//			Printer irPrinter = new Printer("Mx_ir.txt");
		   Printer irPrinter = new Printer(null);
			
			DominanceBuilder domBuilder = new DominanceBuilder();
			DeadEliminator eliminator = new DeadEliminator();
			for (IrFunct funct : irProg.functs.values()) {
				// I need reverse dominance frontier to help eliminate dead loop.
				funct.BuildCFG();
				funct.ReverseCFG();
				domBuilder.BuildConfig(funct.bbs.list, funct.bbs.cfg);
				domBuilder.BuildDominance();
				domBuilder.BuildImmediateDominance();
				domBuilder.DominanceFrontier();
				eliminator.EliminateDeadCode(funct, domBuilder.getgInfos());
			}
			irProg.Print(irPrinter);


//			cleaner.CFGclean(irProg, false);
//			for (IrFunct funct : irProg.functs.values()) {
//				funct.bbs.BuildCFG();
//				funct.bbs.CleanUselessBB(false);
//			}
			


//			 asm builder uses cfg info.
//			AsmBuilder asmer = new AsmBuilder();
//			irProg.BuildCFG();
//			asmer.TranslateIr(irProg);
//			AsmPrinter asmPrinter = new AsmPrinter();
//			asmPrinter.ConfigOutput("Mx_nasm.txt");
//			asmer.Print(asmPrinter);

//			IrInterp("Mx_ir.txt");
		}
	}
}
