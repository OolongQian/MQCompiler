package opt.optimizers;

import ir.quad.Binary;
import ir.quad.Mov;
import ir.quad.Quad;
import ir.quad.Unary;
import ir.structure.BasicBlock;
import ir.structure.Reg;
import opt.Defuse;

import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

import static ir.quad.Binary.Op.*;
import static ir.quad.Binary.Op.CONCAT;
import static ir.quad.Binary.Op.NE;
import static ir.quad.Unary.Op.BITNOT;
import static ir.quad.Unary.Op.NEG;

/********************** Within a BB, locally. *************************/
/**
 * This is a very simple local value numbering.
 * Because we are using SSA form, this could be easy.
 *
 * Def-use chain needs to be re-analyzed after deleting commmon expression.
 *
 * I need to do constant folding later on.
 * */
public class CommonExprDeleter {
	private HashMap<Integer, Reg> congruence = new HashMap<>();
	
	private final int MOD_ = (int) (1e9 + 7);
	
	private int GetHash(int hash1, int hash2) {
		if (hash1 > hash2)
			return GetHash(hash2, hash1);
		return (hash1 + hash2 * 31) % MOD_;
	}
	
	private int CalQuadHash(Binary quad) {
		int hashOp = binOpStrMap.get(quad.op).hashCode();
		int hash1 = quad.src1.hashCode();
		int hash2 = quad.src2.hashCode();
		return GetHash(GetHash(hash1, hash2), hashOp);
	}
	
	private int CalQuadHash(Unary quad) {
		int hashOp = uniOpStrMap.get(quad.op).hashCode();
		int hash = quad.src.hashCode();
		return GetHash(hash, hashOp);
	}
	
	public void WipeCommonExpr() {
		BasicBlock curB = Defuse.entry;
		while (curB != null) {
			WipeCommonExpr(curB);
			curB = (BasicBlock) curB.next;
		}
	}
	
	private void WipeCommonExpr(BasicBlock blk) {
		congruence.clear();
		for(ListIterator<Quad> iter = blk.TraverseQuad().listIterator(); iter.hasNext(); ) {
			Quad quad = iter.next();
			if (quad instanceof Binary || quad instanceof Unary) {
				int quadHash = (quad instanceof Binary) ? CalQuadHash((Binary) quad) : CalQuadHash((Unary) quad);
				if (!congruence.containsKey(quadHash)) {
					if (quad instanceof Binary)
						congruence.put(quadHash, ((Binary) quad).ans);
					else
						congruence.put(quadHash, ((Unary) quad).ans);
				} else {
					// remove current binary operation, replace it with a MOV
					iter.remove();
					Mov comExprReplace = new Mov(quad.GetDefReg(), congruence.get(quadHash));
					comExprReplace.blk = blk;
					iter.add(comExprReplace);
				}
			}
		}
	}
	
	private static Map<Binary.Op, String> binOpStrMap = new HashMap<>();
	static {
		binOpStrMap.put(ADD, "add");
		binOpStrMap.put(SUB, "sub");
		binOpStrMap.put(MUL, "mul");
		binOpStrMap.put(DIV, "div");
		binOpStrMap.put(MOD, "mod");
		binOpStrMap.put(SHL, "shl");
		binOpStrMap.put(SHR, "shr");
		binOpStrMap.put(AND, "and");
		binOpStrMap.put(XOR, "xor");
		binOpStrMap.put(OR,  "or");
		binOpStrMap.put(LAND, "land");
		binOpStrMap.put(LOR, "lor");
		binOpStrMap.put(GT, "gt");
		binOpStrMap.put(GE, "ge");
		binOpStrMap.put(LT, "lt");
		binOpStrMap.put(LE, "le");
		binOpStrMap.put(EQ, "eq");
		binOpStrMap.put(NE, "ne");
		binOpStrMap.put(CONCAT, "concat");
	}
	
	private static Map<Unary.Op, String> uniOpStrMap = new HashMap<>();
	static {
		uniOpStrMap.put(NEG, "neg");
		uniOpStrMap.put(BITNOT, "bitnot");
	}
}
