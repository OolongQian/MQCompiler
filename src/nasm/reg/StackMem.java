package nasm.reg;

public class StackMem extends AsmReg {
	public String varHintName;
	public Integer ebpOffset;
	
	public StackMem(String varHintName) {
		this.varHintName = varHintName;
		this.ebpOffset = null;
	}
	
	@Override
	public String GetText() {
		if (this.ebpOffset == null)
			return String.format("qword [%s]", varHintName);
		else if (ebpOffset >= 0)
//			return String.format("qword [rbp-%02XH]", this.ebpOffset);
			return String.format("qword [rbp-%d]", this.ebpOffset);
		else
//			return String.format("qword [rbp+%02XH]", - this.ebpOffset);
			return String.format("qword [rbp+%d]", -this.ebpOffset);
	}
}
