package ir.structure;

/**
 * It's a reg, encoded by its id number.
 * */
public class StringLiteral extends Reg {
	// string's name means id.
	private static int cnt = 0;
	public String val;
	// *0
	public int id;
	

	public StringLiteral(String val) {
		super(null);
		this.val = val;
		this.id = cnt++;
		name = "*" + Integer.toString(id);
	}
	
	@Override
	public String getText() {
		return '*' + Integer.toString(id);
	}
	
	public StringLiteral() {
		super(null);
	}
	@Override
	public IrValue Copy() {
		StringLiteral copy = new StringLiteral();
		copy.id = id;
		copy.val = val;
		copy.name = name;
		return copy;
	}
}
