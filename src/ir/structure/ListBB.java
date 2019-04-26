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
			assert tail.next == null;
			bb.prev = tail;
			bb.next = null;
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
	
	public void Remove (BasicBlock del) {
		assert del.parentFunct == head.parentFunct;
		BasicBlock cursor = head;
		while (cursor != del) cursor = cursor.next;
		assert cursor == del && cursor != null;
		
		if (del.prev != null) del.prev.next = del.next;
		if (del.next != null) del.next.prev = del.prev;
		if (del == head) head = del.next;
		if (del == tail) tail = del.prev;
		del.prev = del.next = null;
		--size;
	}
}
