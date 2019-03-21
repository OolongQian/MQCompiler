package ir.Interpreter.parse;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Contains literal information of IR
 * */
public class Inst {
	public String operator;
	
	/**
	 * call: return value
	 * return: value to be returned
	 * branch: condition
	 * basic block: label
	 * */
	public String dst;  // used as `cond` for `br` // return value for ret.
	public String src1;
	public String src2;
	
	public List<String> args = new LinkedList<>();
	public String funcName;  // used by call.
	
	// bbName vs valOption
	public Map<String, String> options = new HashMap<>();
	
	public int lineNo = -1;
	
	public Inst(int lineNo) {
		this.lineNo = lineNo;
	}
}

