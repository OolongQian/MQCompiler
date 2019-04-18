package nasm.reg;

/** GlobalMem consists of global inst register and string literal.
 * hintName starts with __ to indicate it's a string literal. */
public class GlobalMem extends AsmReg {
	// for string, hintName is _S_id.
	// for global variable, hintName should be _G_
	public String hintName;
	
	public boolean isString = false;
	
	public GlobalMem(String hintName) {
		this.hintName = hintName;
	}
	
	@Override
	public String GetText() {
		// print string label to indicate address.
		if (isString)
			return hintName;
		// global var has to be referenced.
		else
			return String.format("qword [rel %s]", hintName);
	}
}
