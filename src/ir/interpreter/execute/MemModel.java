package ir.interpreter.execute;

import java.util.HashMap;
import java.util.Map;

import static config.Config.INT_SIZE;

/**
 * A run-time simulated memory.
 * */
public class MemModel {
	static int sp = 4;
	static int hp = 40950000;
	
	Map<Integer, Byte> mem = new HashMap<>();
	

	/**
	 * Int store and load, apparent
	 * */
	public int LoadInt(int addr) {
		int ret = 0;
		for (int i = 0; i < 4; ++i) {
			try {
				byte b = LoadMem(addr + i);
				ret = ret | ((b & 0xFF) << 8 * i);
			} catch (NullPointerException e) {
				int a = 1;
			}
		}
		return ret;
	}
	
	
	public void StoreInt(int addr, int val) {
//			if(err) System.err.println("store int " + val + " to " + addr);
		// NOTE : identical to LoadInt, headAddr is lower address.
		for (int i = 0; i < 4; ++i) {
			// take the value of 4i bytes, counting from lower.
			byte b = (byte)((val >> (8 * i)) & 0xFF);
			StoreMem(addr + i, b);
		}
	}
	
	/**
	 * addr -> strlen -->>> followed by strContent.
	 * */
	public String LoadStr(int addr) {
		int strLen = LoadInt(addr);
		byte[] bytes = new byte[strLen];
		for (int i = 0; i < strLen; ++i) {
			bytes[i] = LoadMem(addr + INT_SIZE + i);
		}
		return new String(bytes);
	}
	
	
	/**
	 * addr -> strContent
	 * NOTE : strLen should be stored beforehead
	 * */
	public void StoreStr(int addr, String str) {
		byte[] bytes = str.getBytes();
		for (int i = 0; i < bytes.length; ++i) {
			StoreMem(addr + i, bytes[i]);
		}
	}
	
	/**
	 * safely initialize stack content to be all 0.
	 * */
	public int AllocMem(int size_) {
		int headAddr = sp;
		sp += size_;
		for (int i = headAddr; i < sp; ++i) {
			assert !mem.containsKey(i);
			mem.put(i, (byte) -1);
		}
		assert sp <= hp;
		return headAddr;
	}
	/**
	 * safely initialize heap content to be all 0.
	 * */
	public int MallocMem(int size_) {
		int headAddr = hp;
		// hp points to the first non-allocated address
		hp -= size_;
		assert sp <= hp;
		for (int i = headAddr; i > hp; --i) {
			assert !mem.containsKey(i);
			mem.put(i, (byte) 0);
		}
		// return the first allocated address.
		return hp + 1;
	}
	
	public Byte LoadMem(int addr) {
		return mem.get(addr);
	}
	
	private void StoreMem(int addr, byte byte_) {
		assert mem.containsKey(addr);
		mem.put(addr, byte_);
	}
}
