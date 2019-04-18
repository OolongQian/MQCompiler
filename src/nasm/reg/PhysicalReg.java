package nasm.reg;

import java.util.LinkedList;
import java.util.List;

import static nasm.reg.PhysicalReg.PhyRegType.*;


public class PhysicalReg {
	
	public enum PhyRegType {
		rax, rcx, rdx, rbx, rsp, rbp, rsi, rdi,
		r8, r9, r10, r11, r12, r13, r14, r15,
		dummy // only for debug purposes.
	}
	
	public static List<PhyRegType> regArgsPass = new LinkedList<>();
	static {
		regArgsPass.add(rdi);
		regArgsPass.add(rsi);
		regArgsPass.add(rdx);
		regArgsPass.add(rcx);
		regArgsPass.add(r8);
		regArgsPass.add(r9);
	}
	
	public static List<PhyRegType> callerSave = new LinkedList<>();
	static {
		callerSave.add(rax);
		callerSave.add(rcx);
		callerSave.add(rdx);
		callerSave.add(r8);
		callerSave.add(r9);
		callerSave.add(r10);
		callerSave.add(r11);
	}
	
	public static List<PhyRegType> calleeSave = new LinkedList<>();
	static {
		// reserve rsp and rbp.
//		calleeSave.add(rsp);
//		calleeSave.add(rbp);
		calleeSave.add(rbx);
		calleeSave.add(r12);
		calleeSave.add(r13);
		calleeSave.add(r14);
		calleeSave.add(r15);
	}
	
	// physical register may take on several different names,
	// hintName to address strange x86 naming issue.
	public String hintName;
	public PhyRegType phyReg;
	
	public PhysicalReg(PhyRegType phyReg) {
		this.hintName = null;
		this.phyReg = phyReg;
	}
	
	public PhysicalReg(String hintName, PhyRegType phyReg) {
		this.hintName = hintName;
		this.phyReg = phyReg;
	}
	
	public String GetPhyName () {
		return (hintName != null) ? hintName : phyReg.name();
	}
}
