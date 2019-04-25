package ir;

import ir.structure.BasicBlock;
import ir.structure.IrFunct;
import ir.structure.Reg;
import ir.structure.StringLiteral;

import java.util.HashMap;
import java.util.Map;

/**
 * record ir building results.
 * */
public class IrProg {
	/**
	 * IR's basic building blocks are functions.
	 */
	public Map<String, IrFunct> functs = new HashMap<>();
	
	/**
	 * global information recording.
	 *
	 * Assign id for global strings. Strings are represented by stringLiteral,
	 * which contains string and its id. It is encoded as *id.
	 * Every string literal is a var, which means it is associated with a memory address.
	 * Thus it inherits from class Reg.
	 * */
	public Map<String, StringLiteral> stringPool = new HashMap<>();
	
	/**
	 * Use globals to record gvar, for output for interpreter.
	 * */
	public Map<String, Reg> globals = new HashMap<>();
	
	/**
	 * Construct control flow graph based on ir info. 
	 * */
	public void BuildCFG() {
		functs.values().forEach(IrFunct::BuildCFG);
	}
	
	public void LinkedListCheck() {
		for (IrFunct funct : functs.values()) {
			funct.LinkedListCheck();
			
		}
	}
	
	/******************** interface for printer ******************/
	public void Print(Printer printer) {
		stringPool.values().forEach(printer::print);
		globals.values().forEach(printer::print);
		functs.values().forEach(printer::print);
		printer.getFout().println();
	}
}
