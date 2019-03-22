package ir.structure;

public class Reg extends IrValue {
	public String name;
	
	public Reg(String name) {
		this.name = name;
	}
	
	public boolean IsNull() {
		return name.equals("@null");
	}
	
	@Override
	public String getText() {
		return name;
	}
}
