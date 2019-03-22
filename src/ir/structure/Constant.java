package ir.structure;

/**
 * Distinguish constant with reg, to do constant folding.
 *
 * Only have int constant in IR.
 * */
public class Constant extends IrValue {
	private int val;
	
	public Constant(int val) {
		this.val = val;
	}
	
	@Override
	public String getText() {
		return Integer.toString(val);
	}
}
