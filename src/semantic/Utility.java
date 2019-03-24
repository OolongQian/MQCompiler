package semantic;

import ast.node.dec.ClassDec;

public class Utility {
	public static String AddPrefix(ClassDec classDec, String methodName) {
		return "-" + classDec.name + '#' + methodName;
	}
	
}
