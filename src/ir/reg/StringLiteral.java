package ir.reg;

/**
 * Think about how global string resides in the memory, some one allocates some memory space,
 * and what we have is actually a char*, you load from which to get the first char content
 * of the string.
 *
 * But you know, not all the strings are stored in the register, so we need to load to get
 * the string, with which we load to get its first char.
 *
 * Hence what we have in hand is the address of the address of the first character content.
 * And this most-outside address is what this StringLiteral represents.
 *
 * In all, this stringLiteral is a Reg type, which means it is some address.
 * Specifically, it is the address loading from which we could get our Pointer to the string content.
 * */
public class StringLiteral extends Reg {
  public String val;
  private int memAddr;
  
  public StringLiteral(String val) {
    super('*' + val);
    this.val = val;
  }
  
  public int GetMemAddr() {
    return memAddr;
  }
  
  public void SetMemAddr(int memAddr) {
    this.memAddr = memAddr;
  }
}
