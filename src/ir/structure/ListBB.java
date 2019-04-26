package ir.structure;


public class ListBB {
	private BasicBlock head;
	private BasicBlock tail;
	private int size;
	
	public BasicBlock Head() {
		return head;
	}
	
	public BasicBlock Tail() {
		return tail;
	}
	
	public int Size() {
		return size;
	}
	
	public void PushBack(BasicBlock bb) {
		if (size == 0) {
			head = tail = bb;
			bb.prev = bb.next = null;
			size = 1;
		}
		else {
			bb.prev = tail;
			bb.next = tail.next;
			tail.next = bb;
			tail = bb;
			size++;
		}
	}
	
	public void InsertAfter(BasicBlock mark, BasicBlock nb) {
		assert mark != null && nb != null;
		assert mark.parentFunct == nb.parentFunct;
		
		BasicBlock cur = Head();
		while (cur != mark) cur = cur.next;
		nb.prev = mark;
		nb.next = mark.next;
		if (mark.next != null)
			mark.next.prev = nb;
		mark.next = nb;
		size++;
		if (tail == mark)
			tail = nb;
	}
	
	public void Clear () {
		head = tail = null;
		size = 0;
	}
	
	// blk's prev and next are effective after deletion.
	public void Remove (BasicBlock blk) {
		boolean present = false;
		for (BasicBlock cur = Head(); cur != null; cur = cur.next) {
			if (cur == blk)  {
				present = true;
				break;
			}
		}
		assert present;
		if (blk.next != null)
			blk.next.prev = blk.prev;
		if (blk.prev != null)
			blk.prev.next = blk.next;
		
		if (blk == head) head = blk.next;
		else if (blk == tail) tail = blk.prev;
		
		--size;
	}
}
