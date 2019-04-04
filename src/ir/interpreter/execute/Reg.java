package ir.interpreter.execute;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ir.Config.NULL;

/**
 * A run-time used data structure representing a def-use chain.
 * */
public class Reg {
	public String name;
	private Integer val;
	private boolean valSet;
	public boolean alloc_d;
	
	/**
	 * Immediate value is automatically constructed as Reg.
	 * Reg can be set as null.
	 * */
	public Reg(String name) {
		this.name = name;
		valSet = false;
		alloc_d = false;
		// for immediate value.
		if (isNumeric(name)) {
			SetValue(Integer.parseInt(name));
		}
		// set fow null value.
		else if (name.equals("@null")) {
			SetValue(NULL);
		}
	}
	
	/**
	 * Mark the register to be allocated
	 * */
	public void SetAllocd() {
		alloc_d = true;
	}
	
	/**
	 * Null value can be set as value.
	 * */
	public void SetValue(Integer val) {
		valSet = true;
		this.val = val;
	}
	
	public Integer GetValue() {
		if (!valSet) {
			int a =1 ;
		}
		assert valSet;
		return val;
	}
	
	/**
	 * Used to distinguish immediate value.
	 * */
	private boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if( !isNum.matches() ){
			return false;
		}
		return true;
	}
}
