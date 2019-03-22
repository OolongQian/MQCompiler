package ir.structure;

/**
 * It's a reg, encoded by its id number.
 * */
public class StringLiteral extends Reg {
	private static int cnt = 0;
	public String val;
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
}
