package nasm.reg;

/**
 * Virtual registers are for def-use.
 * */
public class VirtualReg extends AsmReg {
	public String hintName;
	
	public VirtualReg(String hintName) {
		this.hintName = hintName;
	}
	
	@Override
	public String GetText() {
		return hintName;
	}
}
