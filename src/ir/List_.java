package ir;

import ir.structure.BasicBlock;

public class List_ {
	private Linkable head;
	private Linkable tail;
	public int size_;
	
	public List_() {
		head = tail = null;
		size_ = 0;
	}
	
	/**
	 * push at back
	 * */
	public void PushBack(Linkable e) {
		if (size_ == 0) {
			Init(e);
		}
		else {
			e.prev = tail;
			e.next = tail.next;
			tail = e;
			size_++;
		}
	}
	
	public void InsertFront(Linkable e) {
		if (size_ == 0) {
			Init(e);
		}
		else {
			e.next = head;
			e.prev = head.prev;
			head = e;
		}
		size_++;
	}
	
	public void InsertAfter(Linkable mark, Linkable e) {
		assert size_ != 0;
		e.prev = mark;
		e.next = mark.next;
		mark.next = e;
		if (mark == tail) tail = e;
		size_++;
	}
	
	public BasicBlock GetHead() {
		return (BasicBlock) head;
	}
	
	public BasicBlock GetTail() {
		return (BasicBlock) tail;
	}
	
	public void Remove(Linkable junk) {
		Linkable cur = head;
		while (cur != null && cur != junk) {
			cur = cur.next;
		}
		if (cur == null)
			throw new RuntimeException();
		
		if (cur.prev != null)
			cur.prev.next = cur.next;
		if (cur.next != null)
			cur.next.prev = cur.prev;
		
		if (cur == head) head = cur.next;
		if (cur == tail) tail = cur.prev;
		
		--size_;
	}
	
	private void Init(Linkable e) {
		assert size_ == 0;
		head = tail = e;
		e.prev = e.next = null;
		size_++;
	}
}
