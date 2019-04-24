package nasm.reg;

public class Imm extends AsmReg {
	public int imm;
	
	public Imm(int imm) {
		this.imm = imm;
	}
	
	@Override
	public String GetText() {
		return Integer.toString(imm);
	}
	
	@Override
	public String GetVreg() {
		return GetText();
	}
}
