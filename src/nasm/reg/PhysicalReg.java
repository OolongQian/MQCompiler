package nasm.reg;

public class PhysicalReg extends AsmReg {
	public String hintName;
	
	public PhysicalReg(String hintName) {
		this.hintName = hintName;
	}
	
	@Override
	public String GetText() {
		return hintName;
	}
}
