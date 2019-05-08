package ir.structure;

import ir.quad.Branch;
import ir.quad.Jump;
import ir.quad.Quad;
import ir.quad.Ret;

import java.util.*;

import static ir.Utility.MakeGreg;

public class IrFunct {
	public String name;

	public BBS bbs = new BBS();
	
	public BasicBlock curBB;
	
	public boolean retInt = false;
	
	// for later optimization. redundant.
//	public Set<Quad> brjp = new HashSet<>();
	
	/**
	 * Inherited from AST. foo(int a, int b).
	 * This formal parameters have been renamed to be @a @b.
	 * They have no version problem because they are the first being renamed.
	 * */
	public List<String> formalArgs = new LinkedList<>();
	/**
	 * Ir reg's representation of arguments, foo(%0, %1)
	 * for invoking argument passing.
	 * */
	public List<Reg> regArgs = new LinkedList<>();
	
	/**
	 * If this function is a method, no AST nodes could save this register.
	 * */
	public Reg this_;
	
	public IrFunct(String name) {
		this.name = name;
		BasicBlock entry = new BasicBlock(GetBBName("entry"), this, 0);
		bbs.list.PushBack(entry);
		curBB = entry;
	}
	
	/**
	 * The last basic block jumps to nowhere by default. We complete a function
	 * by inserting a pseudo return statement
	 * */
	public void EnsureFunctRet() {
		BasicBlock tail = bbs.list.Tail();
		Quad last = (tail.quads.isEmpty()) ? null : tail.quads.get(tail.quads.size() - 1);
		if (last instanceof Jump || last instanceof Branch || last instanceof Ret) ;
		else {
			Ret pseudo = new Ret(MakeGreg("null"));
			pseudo.blk = tail;
			tail.quads.add(pseudo);
		}
	}
	
	public void BuildCFG() {
		bbs.BuildCFG();
	}
	
	public void ReverseCFG () {
		bbs.ReverseCFG();
	}
	/******************** Utility *******************/
	/**
	 * IrFunct is a namespace itself.
	 *
	 * Avoid naming collision for both temp and local registers.
	 * */
	// name for temp registers. %1
	private int tmpNameCnt = 0;
	public Reg GetTmpReg() {
		return new Reg(GetTmpName());
	}
	private String GetTmpName() {
		return "%" + Integer.toString(tmpNameCnt++);
	}
	
	// name for local registers. $v
	private HashMap<String, Integer> localNamer = new HashMap<>();
	public String RenameLocal(String name) {
		String locName = "$" + name;
		if (!localNamer.containsKey(locName))
			localNamer.put(locName, 0);
		int index = localNamer.get(locName);
		localNamer.put(locName, index + 1);
		return locName + '(' + Integer.toString(index) + ')';
	}
	
	// rename for loop condition variable or something, reserved.
	// rename for phi after destruction.
	public Reg GetReserveReg(String name) {
		return new Reg(GetReserveName(name));
	}
	public String GetReserveName(String name) {
		String locName = "`" + name;
		if (!localNamer.containsKey(locName))
			localNamer.put(locName, 0);
		int index = localNamer.get(locName);
		localNamer.put(locName, index + 1);
		return locName + '(' + Integer.toString(index) + ')';
	}
	
	// shadow basic block name clash.
	private HashMap<String, Integer> bbNamer = new HashMap<>();
	public String GetBBName(String name) {
		if (!bbNamer.containsKey(name))
			bbNamer.put(name, 0);
		int index = bbNamer.get(name);
		bbNamer.put(name, index + 1);
		return '$' + name + Integer.toString(index);
	}
	
	public void CheckQuadsBlk() {
		for (BasicBlock cur = bbs.list.Head(); cur != null; cur = cur.next) {
			for (Quad quad : cur.quads) {
				if (quad.blk != cur)
					throw new RuntimeException("quad's block not maintained.\n");
			}
		}
	}
}
