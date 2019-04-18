package nasm.reg;

// stackMem has hintName to show their identity.
public class StackMem extends AsmReg {
	public String varHintName;
	public Integer ebpOffset;
	
	public StackMem(String varHintName) {
		this.varHintName = varHintName;
		this.ebpOffset = null;
	}
	
	@Override
	public String GetText() {
		// if offset hasn't been set, display hintName.
		if (this.ebpOffset == null)
			return String.format("qword [%s]", varHintName);
		else if (ebpOffset >= 0)
			return String.format("qword [rbp-%d]", this.ebpOffset);
		else
			return String.format("qword [rbp+%d]", -this.ebpOffset);
	}
	
	// lea text has no qword
	public String GetLeaText() {
		// if offset hasn't been set, display hintName.
		if (this.ebpOffset == null)
			return String.format("[%s]", varHintName);
		else if (ebpOffset >= 0)
			return String.format("[rbp-%d]", this.ebpOffset);
		else
			return String.format("[rbp+%d]", -this.ebpOffset);
	}
}
