package ir.structure;

import ir.List_;
import ir.quad.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Function {
	public String name;
	
	public List_ bbs = new List_();
	
	/**
	 * Inherited from AST. foo(int a, int b)
	 * */
	public List<String> formalArgs = new LinkedList<>();
	
	/**
	 * Ir reg's representation of arguments, foo(%0, %1)
	 * for invoking argument passing.
	 * */
	public List<Reg> regArgs = new LinkedList<>();
	
	/**
	 * cursor to insert basic block
	 * */
	public BasicBlock curBB;
	
	/**
	 * If this function is a method, no AST nodes could save this register.
	 * */
	public Reg this_;
	
	private List<Quad> brjp = new LinkedList<>();
	
	/**
	 * Function initializes with an entry basic block.
	 * Set current BB to it.
	 * */
	public Function(String name) {
		this.name = name;
		BasicBlock entry = new BasicBlock(GetBBName("entry"), this);
		bbs.PushBack(entry);
		curBB = entry;
	}
	
	public void EmplaceInst(Quad quad) {
		assert !curBB.complete;
		quad.blk = curBB;
		
		if (quad instanceof Alloca) {
			curBB.Add(0, quad);
		}
		else {
			curBB.Add(quad);
		}
		
		// record CFG information.
		if (quad instanceof Jump || quad instanceof Branch) {
			brjp.add(quad);
		}
		
		if (quad instanceof Ret) {
			curBB.complete = true;
		}
	}
	
	/**
	 * Basic block management
	 * */
	public void SetCurBB(BasicBlock bb) {
		curBB = bb;
	}
	public BasicBlock GetCurBB() {
		return curBB;
	}
	public BasicBlock NewBBAfter(BasicBlock bb, String name) {
		assert bb.parentFunct == this;
		String renameBB = GetBBName(name);
		BasicBlock nb = new BasicBlock(renameBB, bb.parentFunct);
		bbs.InsertAfter(bb, nb);
		return nb;
	}
	
	public void ConstructCFG() {
		BasicBlock cur = bbs.GetHead();
		while (cur.next != null) {
			cur.JumpTo((BasicBlock) cur.next);
			cur = (BasicBlock) cur.next;
		}
		for (Quad jp : brjp) {
			if (jp instanceof Jump) {
				jp.blk.JumpTo(((Jump) jp).target);
			} else if (jp instanceof Branch) {
				jp.blk.JumpTo(((Branch) jp).ifTrue);
				jp.blk.JumpTo(((Branch) jp).ifFalse);
			} else {
				throw new RuntimeException("unexpected brjp");
			}
		}
	}
	
	/******************** Utility *******************/
	/**
	 * Function is a namespace itself.
	 *
	 * Avoid naming collision for both temp and local registers.
	 * */
	private int tmpNameCnt = 0;
	public String GetTmpName() {
		return "%" + Integer.toString(tmpNameCnt++);
	}
	
	private HashMap<String, Integer> localNamer = new HashMap<>();
	public String GetLocalName(String name) {
		String locName = "$" + name;
		if (!localNamer.containsKey(locName))
			localNamer.put(locName, 0);
		int index = localNamer.get(locName);
		localNamer.put(locName, index + 1);
		return locName + '(' + Integer.toString(index) + ')';
	}
	
	public String GetReserveName(String name) {
		String locName = "`" + name;
		if (!localNamer.containsKey(locName))
			localNamer.put(locName, 0);
		int index = localNamer.get(locName);
		localNamer.put(locName, index + 1);
		return locName + '(' + Integer.toString(index) + ')';
	}
	
	private HashMap<String, Integer> bbNamer = new HashMap<>();
	public String GetBBName(String name) {
		if (!bbNamer.containsKey(name))
			bbNamer.put(name, 0);
		int index = bbNamer.get(name);
		bbNamer.put(name, index + 1);
		return '$' + name + Integer.toString(index);
	}
	
}
