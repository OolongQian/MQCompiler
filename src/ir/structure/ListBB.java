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
		mark.next = nb;
		size++;
		if (tail == mark)
			tail = nb;
	}
}
