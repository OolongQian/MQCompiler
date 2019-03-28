package ir.structure;

import ir.List_;
import ir.quad.*;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static ir.Utility.MakeGreg;

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
	
	/**
	 * Alloca must be put at entry.
	 * */
	public void EmplaceInst(Quad quad) {
		assert !curBB.complete;
		quad.blk = curBB;

		if (quad instanceof Alloca) {
			bbs.GetHead().AllocaFront((Alloca) quad);
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
	
	/**
	 * Edges will be added more than once, thus using Set to record edges.
	 * */
	public void ConstructCFG() {
		BasicBlock cur = bbs.GetHead();
		while (cur.next != null) {
			// don't add default CFG edge if there's a branch or jump instruction that guards at the bottom.
			boolean skipJump = false;
			List<Quad> quads = cur.TraverseQuad();
			if (!quads.isEmpty()) {
				Quad last = quads.get(quads.size() - 1);
				if (last instanceof Jump || last instanceof Branch)
					skipJump = true;
			}
			if (!skipJump) {
				Jump fallThrough = new Jump((BasicBlock) cur.next);
				fallThrough.blk = cur;
				cur.AddDefaultJump(fallThrough);
				cur.JumpTo((BasicBlock) cur.next);
			}
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
		
		cur = bbs.GetHead();
		while (cur.next != null) cur = (BasicBlock) cur.next;
		Ret ret = new Ret(MakeGreg("null"));
		ret.blk = cur;
		
		if (cur.TraverseQuad().isEmpty()) {
			cur.AddDefaultJump(ret);
		}
		else {
			Quad quad = cur.GetLastQuad();
			if (!(quad instanceof Jump) && !(quad instanceof Branch) && !(quad instanceof Ret)) {
				cur.AddDefaultJump(ret);
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
	
	/**
	 * Use serial to implement deep copy.
	 * */
	public Function DeepClone() throws IOException,ClassNotFoundException{
		// write into stream
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		objectOutputStream.writeObject(this);
		// get from stream
		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
		return (Function) objectInputStream.readObject();
	}
}
