package nasm.reg;

import java.util.LinkedList;
import java.util.List;

import static nasm.reg.PhysicalReg.PhyRegType.*;


// physical rergisters not only have type,  but also have hintNames.
// hintName is for description and lower bits or other register width.
public class PhysicalReg {
	
	public static int REGNUM = 14;
	
	public enum PhyRegType {
		rax, rcx, rdx, rbx, rsp, rbp, rsi, rdi,
		r8, r9, r10, r11, r12, r13, r14, r15,
		dummy // only for debug purposes.
	}
	
	// registers used for allocation, exclude rsp and rbp.
	public static List<PhyRegType> okColors = new LinkedList<>();
	static {
		okColors.add(rax);
		okColors.add(rcx);
		okColors.add(rdx);
		okColors.add(rbx);
		okColors.add(rsi);
		okColors.add(rdi);
		okColors.add(r8);
		okColors.add(r9);
		okColors.add(r10);
		okColors.add(r11);
		okColors.add(r12);
		okColors.add(r13);
		okColors.add(r14);
		okColors.add(r15);
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
//		callerSave.add(rax);
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
