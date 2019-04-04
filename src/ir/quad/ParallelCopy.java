package ir.quad;

import ir.Printer;

import java.util.HashSet;
import java.util.Set;

public class ParallelCopy extends Quad {
	
	public Set<Store> copies = new HashSet<>();
	
	@Override
	public void AcceptPrint(Printer printer) {
		;
	}
}
