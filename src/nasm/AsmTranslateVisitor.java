package nasm;


import ir.quad.Branch;
import ir.quad.Quad;
import ir.structure.BasicBlock;
import ir.structure.Constant;
import ir.structure.IrValue;
import nasm.asm.*;
import nasm.reg.*;

import java.util.*;

import static ir.quad.Binary.Op.*;

/**
 * Note : all registers are separate in current implementation.
 * */
public class AsmTranslateVisitor {
	
	/** current AsmBB handler */
	private AsmBB cur;
	
	// for global variable, @a -> _G_a.
	// for stringLiteral, *1 -> _S_1.
	private Map<String, GlobalMem> globals;
	
	/********************* data structure config **************************/
	// must be set before translation.
	public void ConfigTranslateContext (Map<String, GlobalMem> globals) {
		this.globals = globals;
	}
	
	// set before translation for each basic block.
	public void ConfigAsmBB (AsmBB bb) {
		cur = bb;
	}

	/******************** translation starter and utility *********************/
	/** need flexibility to get next quad or skip one quad. */
	private int qCnt = 0;
	private List<Quad> quads;
	public void TranslateQuadList (List<Quad> quads) {
		this.quads = quads;
		for (qCnt = 0; qCnt < this.quads.size(); ++qCnt)
			this.quads.get(qCnt).AcceptTranslator(this);
	}
	
	/** Return the next quad w.r.t current quad.
	 * return null if no extra quad left. */
	private Quad GetNextQuad () {
		if (qCnt + 1 >= this.quads.size())
			return null;
		return this.quads.get(qCnt + 1);
	}
	private void SkipOneQuad () {
		assert qCnt < this.quads.size();
		++qCnt;
	}
	
	
	/******************** calling convention *************************/
	public static List<String> callArgRegs = new LinkedList<>();
	{
		callArgRegs.add("rdi");
		callArgRegs.add("rsi");
		callArgRegs.add("rdx");
		callArgRegs.add("rcx");
		callArgRegs.add("r8");
		callArgRegs.add("r9");
	}
	// TODO : Fix this map.
	public static Map<String, String> builtin2Extern = new HashMap<>();
	{
		builtin2Extern.put("~print", "printf");
		builtin2Extern.put("~println", "put");
	}
	private String TranslateExtern (String functName) {
		if (functName.startsWith("~")) {
			assert builtin2Extern.containsKey(functName);
			return builtin2Extern.get(functName);
		}
		else
			return functName;
	}
	
	public void visit (ir.quad.Call quad) {
		// follow the calling convention to put the args into suitable registers.
		int i;
		for (i = 0; i < 6 && i < quad.args.size(); ++i) {
			Mov movArg = new Mov (cur);
			movArg.dst = new PhysicalReg(callArgRegs.get(i));
			movArg.src = GetAsmReg(quad.args.get(i));
			cur.asms.add(movArg);
		}
		// if there are leftover args, push them onto stack from right to left.
		// also, record the stack argument offset.
		int stackArgsOffset = 0;
		// must subtract rsp before push stack args.
		int subRspPos = cur.asms.size();
		for (int j = quad.args.size() - 1; i < quad.args.size(); ++i, --j) {
			Push pushArg = new Push (cur);
			pushArg.src = GetAsmReg(quad.args.get(j));
			cur.asms.add(pushArg);
			// each push goes in 8 bytes.
			stackArgsOffset += 8;
		}
		// add another 8 stackArgOffset
		if (stackArgsOffset % 16 != 0) {
			Binary subRsp = new Binary (cur);
			subRsp.op = Binary.Op.SUB;
			subRsp.dst = new PhysicalReg("rsp");
			subRsp.src = new Imm(8);
			cur.asms.add(subRspPos, subRsp);
			stackArgsOffset += 8;
		}
		Call call = new Call(cur);
		call.functName = TranslateExtern(quad.funcName);
		cur.asms.add(call);
		
		// subtract esp by stackArgsOffset to reset stack pointer.
		Binary reset = new Binary (cur);
		reset.op = Binary.Op.ADD;
		reset.dst = new PhysicalReg("rsp");
		reset.src = new Imm(stackArgsOffset);
		cur.asms.add(reset);
		
		// mov rax result back into the return register.
		if (!quad.ret.name.equals("@null")) {
			Mov retAns = new Mov (cur);
			retAns.dst = GetAsmReg(quad.ret);
			retAns.src = new PhysicalReg("rax");
			cur.asms.add(retAns);
		}
	}
	
	/******************* concrete visit actions ***********************/
	private void visit (ir.quad.Quad quad) {
		quad.AcceptTranslator(this);
	}
	
	/** For simple move */
	public void visit (ir.quad.Mov quad) {
		Mov mov = new Mov (cur);
		mov.dst = GetAsmReg(quad.dst);
		mov.src = GetAsmReg(quad.src);
		cur.asms.add(mov);
	}
	
	// FIXME : consider MUL and DIV are special case.
	private static Map<ir.quad.Binary.Op, nasm.asm.Binary.Op> irAsmOp = new HashMap<>();
	static {
		irAsmOp.put(ADD, Binary.Op.ADD);
		irAsmOp.put(SUB, Binary.Op.SUB);
		irAsmOp.put(MUL, Binary.Op.IMUL);
		irAsmOp.put(DIV, Binary.Op.DIV);
	}
	
	/** For arithmetic */
	public void visit (ir.quad.Binary quad) {
		switch (quad.op) {
			case DIV:
				// Divide rax by src, and put the ratio into rax, and the remainder into rdx.
				// move dividend to rax
				Mov dividend2rax = new Mov (cur);
				dividend2rax.dst = new PhysicalReg("rax");
				dividend2rax.src = GetAsmReg(quad.src1);
				cur.asms.add(dividend2rax);
				// divide rax by src2.
				Binary div = new Binary (cur);
				div.op = Binary.Op.DIV;
				div.src = GetAsmReg(quad.src2);
				cur.asms.add(div);
				// put result back to reg.
				Mov quo2ans = new Mov (cur);
				quo2ans.dst = GetAsmReg(quad.ans);
				quo2ans.src = new PhysicalReg("rax");
				cur.asms.add(quo2ans);
				break;
			case MUL:
				// Multiply rax by src, and put the ratio into rax, and the remainder into rdx.
				// move dividend to rax
				Mov mul2rax = new Mov (cur);
				mul2rax.dst = new PhysicalReg("rax");
				mul2rax.src = GetAsmReg(quad.src1);
				cur.asms.add(mul2rax);
				// divide rax by src2.
				Binary mul = new Binary (cur);
				mul.op = Binary.Op.IMUL;
				mul.src = GetAsmReg(quad.src2);
				cur.asms.add(mul);
				// put result back to reg.
				Mov prod2ans = new Mov (cur);
				prod2ans.dst = GetAsmReg(quad.ans);
				prod2ans.src = new PhysicalReg("rax");
				cur.asms.add(prod2ans);
			case GT: case GE: case LT: case LE: case EQ: case NE:
				Quad next = GetNextQuad();
				if (next != null && next instanceof Branch) {
					TranslateCmpBr(quad, (Branch) next);
					// NOTE : don't visit load again.
					SkipOneQuad();
				}
				break;
			default:
				// move src1 to dst.
				Mov mov = new Mov (cur);
				mov.dst = GetAsmReg(quad.ans);
				mov.src = GetAsmReg(quad.src1);
				// add src2 to dst (src1).
				Binary bin = new Binary(cur);
				bin.op = irAsmOp.get(quad.op);
				bin.dst = GetAsmReg(quad.ans);
				bin.src = GetAsmReg(quad.src2);
				
				cur.asms.add(mov);
				cur.asms.add(bin);
		}
		
	}
	
	public void visit (ir.quad.Unary quad) {
	
	}
	public void visit (ir.quad.Branch quad) {
		assert false;
	}
	public void visit (ir.quad.Jump quad) {
		Jmp jmp = new Jmp(cur);
		jmp.jpOp = Jmp.JmpOption.JMP;
		jmp.label = BasicBlockRenamer(quad.target);
		cur.asms.add(jmp);
	}

	// TODO : translator needs more information here.
	// TODO : function epilogue needs implementing.
	public void visit (ir.quad.Ret quad) {
		if (quad.val != null) {
			Mov mov = new Mov (cur);
			mov.dst = new PhysicalReg("rax");
			mov.src = GetAsmReg(quad.val);
			cur.asms.add(mov);
		}
	}
	
	/** For load and store */
	// FIXME : loaded mem needs more data dimension and information.
	// NOTE : globals could only appear here, in the source.
	// NOTE : we may do copy propagation after this.
	public void visit (ir.quad.Load quad) {
		Mov load = new Mov (cur);
		load.dst = GetAsmReg(quad.val);
		if (quad.addr.name.startsWith("@")) {
			// use name prefix to recognize global memory register name
			assert globals.containsKey(quad.addr.name);
			load.src = globals.get(quad.addr.name);
		}
		else if (quad.addr.name.startsWith("*")) {
			load.src = new GlobalMem(StringRenamer(quad.addr.name));
			((GlobalMem) load.src).isString = true;
			globals.put(quad.addr.name, (GlobalMem) load.src);
		}
		else
			load.src = new StackMem(quad.addr.name);
		cur.asms.add(load);
	}
	
	public void visit (ir.quad.Store quad) {
		// store irValue to target mem address.
		Mov store = new Mov (cur);
		if (quad.dst.name.startsWith("@")) {
			// use name prefix to recognize global memory register name
			assert globals.containsKey(quad.dst.name);
			store.dst = globals.get(quad.dst.name);
		}
		else
			store.dst = new StackMem(quad.dst.name);

		store.src = GetAsmReg(quad.src);
		cur.asms.add(store);
	}
	
	public void visit (ir.quad.Alloca quad) {
	
	}
	public void visit (ir.quad.Malloc quad) {
	
	}
	public void visit (ir.quad.Comment quad) {
	
	}
	
	/** For translating branch */
	private void TranslateCmpBr (ir.quad.Binary irCmp, ir.quad.Branch br) {
		Cmp cmp = new Cmp (cur);
		cmp.dst = GetAsmReg(irCmp.src1);
		cmp.src = GetAsmReg(irCmp.src2);
		cur.asms.add(cmp);
		
		Jmp jmp = new Jmp (cur);
		jmp.label = BasicBlockRenamer(br.ifTrue);
		switch (irCmp.op) {
			case GT: jmp.jpOp = Jmp.JmpOption.JG; break;
			case GE: jmp.jpOp = Jmp.JmpOption.JGE; break;
			case LT: jmp.jpOp = Jmp.JmpOption.JL; break;
			case LE: jmp.jpOp = Jmp.JmpOption.JLE; break;
			case EQ: jmp.jpOp = Jmp.JmpOption.JE; break;
			case NE: jmp.jpOp = Jmp.JmpOption.JNE; break;
			default:
				assert false;
		}
		cur.asms.add(jmp);
		
		Jmp brFalse = new Jmp (cur);
		brFalse.label = BasicBlockRenamer(br.ifFalse);
		brFalse.jpOp = Jmp.JmpOption.JMP;
		cur.asms.add(brFalse);
	}
	
	/*************** Utility ****************/
	// TODO : StringLiteral hasn't been considered.
	private AsmReg GetAsmReg (IrValue irReg) {
		if (irReg instanceof Constant)
			return new Imm(Integer.parseInt(irReg.getText()));
		else
			return new VirtualReg(irReg.getText());
	}
	
	int cnt = 0;
	private VirtualReg GetTmpReg () {
		return new VirtualReg("asm" + cnt++);
	}
	
	// turn *0 into __0
	public static String StringRenamer (String strId) {
		return "_S_" + strId.substring(1);
	}
	
	// rename basic block's name to be AsmBB, attach parentFunction to it.
	// and again, rename Basic block name in jump.
	public static String BasicBlockRenamer (BasicBlock bb) {
		return String.format("_B_%s_%s", bb.name.substring(1), bb.parentFunct.name);
	}
	
	// change @a to _G_a.
	public static String GlobalRenamer (String gName) {
		return String.format("_G_%s", gName.substring(1));
	}
}
