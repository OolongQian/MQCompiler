package nasm.reg;

/** Virtual registers are for def-use. */
public class Reg extends AsmReg {
	
	public String hintName;
	
	public PhysicalReg color;
	
	
	public Reg(String hintName) {
		this.color = null;
		this.hintName = hintName;
	}
	
	public Reg(String hintName, PhysicalReg color) {
		this.hintName = hintName;
		this.color = color;
	}

	public boolean isAllocd() {
		return color != null;
	}
	
	public void AllocReg(PhysicalReg color) {
		this.color = color;
	}
	
	@Override
	public String GetText() {
		if (!isAllocd())
			// hasn't allocated, return virtual register name.
			return hintName;
		else
			// has allocated, return physical register name.
			return color.GetPhyName();
	}
}

