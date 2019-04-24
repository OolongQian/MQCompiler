package nasm.reg;

import static config.Config.DEBUGPRINT_VIRTUAL;

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

	public boolean isColored() {
		return color != null;
	}
	
	public void AllocReg(PhysicalReg color) {
		this.color = color;
	}
	
	@Override
	public String GetText() {
		if (DEBUGPRINT_VIRTUAL)
			return hintName;
		if (!isColored())
			// hasn't allocated, return virtual register name.
			return hintName;
		else
			// has allocated, return physical register name.
			return color.GetPhyName();
	}
	
	@Override
	public String GetVreg() {
		return hintName;
	}
}

