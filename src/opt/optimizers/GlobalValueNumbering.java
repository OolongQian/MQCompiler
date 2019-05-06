package opt.optimizers;

import ir.IrProg;
import ir.quad.*;
import ir.structure.*;

import java.util.*;

// goes immediately after SSA construction.
// need to use dominance tree info.
public class GlobalValueNumbering {

  // local value numbering map. Mapping from code 'form' to 'entity'.
  // form includes the literal name of 'var' 'constant' 'expression'.
  // entity refers to SSA reg.
  // SSA does numbering for us.
  private Map<IrValue, IrValue> val2vn = new HashMap<>();
  private Map<String, IrValue> hash2vn = new HashMap<>();

  // local value numbering is for eliminating unnecessary operation.
  public void LocalValueNumbering(IrProg ir) {
    for (IrFunct funct : ir.functs.values()) {
      for (BasicBlock bb = funct.bbs.list.Head(); bb != null; bb = bb.next) {
        val2vn.clear();
        hash2vn.clear();
        for (int i = 0; i < bb.quads.size(); ++i) {
          Quad inst = bb.quads.get(i);

          if (inst instanceof Mov) {
            Mov mov = (Mov) inst;
            PutValueNo(mov.dst, GetValueNo(mov.src));
          }
          else if (inst instanceof Binary) {
            Binary bin = (Binary) inst;
            IrValue vn1 = GetValueNo(bin.src1);
            IrValue vn2 = GetValueNo(bin.src2);
					
            if (vn1 instanceof Constant && vn2 instanceof Constant) {
              // change binary into an assign.
	            if (bin.op == Binary.Op.DIV && ((Constant) vn2).GetConstant() == 0)
	            	continue;
              Constant eval = ConstFolding(bin.op, (Constant) vn1, (Constant) vn2);
              Mov mov = new Mov (bin.ans, eval);
              mov.blk = bb;
              bb.quads.add(++i, mov);
              val2vn.put(bin.ans, eval);
            }
            else {
              // check whether this expr combination has existed.
              String hashKey = bin.op.name() + vn1.getText() + vn2.getText();
              if (hash2vn.containsKey(hashKey)) {
                // replace operation by a copy from old value.
                Mov mov = new Mov (bin.ans, hash2vn.get(hashKey));
                mov.blk = bb;
//                bb.quads.remove(i);
                bb.quads.add(++i, mov);
//                bb.quads.add(i, mov);
                // redundant, associate old value.
                val2vn.put(bin.ans, hash2vn.get(hashKey));
              }
               else {
                 // remember this expr format, associate it with itself.
                hash2vn.put(hashKey, bin.ans);
                val2vn.put(bin.ans, bin.ans);
              }
            }
          } else if (inst instanceof Unary) {
            Unary uni = (Unary) inst;
            IrValue vn = GetValueNo(uni.src);
            if (vn instanceof Constant) {
              Constant eval = ConstFolding(uni.ans, uni.op, (Constant) vn);
              Mov mov = new Mov (uni.ans, eval);
              mov.blk = bb;
              bb.quads.add(++i, mov);
              val2vn.put(uni.ans, eval);
            }
            else {
              // similar to binary case.
              String hashKey = uni.op.name() + vn.getText();
              if (hash2vn.containsKey(hashKey)) {
                Mov mov = new Mov (uni.ans, hash2vn.get(hashKey));
                mov.blk = bb;
//                bb.quads.remove(i);
                bb.quads.add(++i, mov);
//                bb.quads.add(i, mov);
                val2vn.put(uni.ans, hash2vn.get(hashKey));
              }
              else {
                hash2vn.put(hashKey, uni.ans);
                val2vn.put(uni.ans, uni.ans);
              }
            }
          }
        }
      }
    }
  }

  // get associated value numbering, or just create one if doesn't exist.
  // evaluate 'src IrValue'.
  // Use its type information to obtain its form.
  // Its form leads us to entity by looking up val2vn.
  private IrValue GetValueNo(IrValue val) {
    if (!val2vn.containsKey(val))
      val2vn.put(val, val);
    return val2vn.get(val);
  }

  private void PutValueNo (IrValue key, IrValue val) {
    assert !val2vn.containsKey(key);
    val2vn.put(key, val);
  }

  private Constant ConstFolding(Binary.Op op, Constant const1, Constant const2) {
    int c1 = const1.GetConstant();
    int c2 = const2.GetConstant();
    Integer eval = null;
    switch (op) {
      case ADD:
        eval = c1 + c2;
        break;
      case SUB:
        eval = c1 - c2;
        break;
      case MUL:
        eval = c1 * c2;
        break;
      case DIV:
        eval = c1 / c2;
        break;
      case MOD:
        eval = c1 % c2;
        break;
      case SHL:
        eval = c1 << c2;
        break;
      case SHR:
        eval = c1 >> c2;
        break;
      case AND:
        eval = c1 & c2;
        break;
      case XOR:
        eval = c1 ^ c2;
        break;
      case OR:
        eval = c1 | c2;
        break;
      case GT:
        eval = (c1 > c2) ? 1 : 0;
        break;
      case GE:
        eval = (c1 >= c2) ? 1 : 0;
        break;
      case LT:
        eval = (c1 < c2) ? 1 : 0;
        break;
      case LE:
        eval = (c1 <= c2) ? 1 : 0;
        break;
      case EQ:
        eval = (c1 == c2) ? 1 : 0;
        break;
      case NE:
        eval = (c1 != c2) ? 1 : 0;
        break;
      case LAND:
        eval = c1 & c2;
        break;
      case LOR:
        eval = c1 | c2;
        break;
      default:
        assert false;
    }
    // associate constant with binary ans.
    return new Constant(eval);
  }

  private Constant ConstFolding(Reg ans, Unary.Op op, Constant const_) {
    int c = const_.GetConstant();
    Integer eval = null;
    switch (op) {
      case BITNOT:
        assert c == 0 || c == 1;
        eval = (c == 0) ? 1 : 0;
        break;
      case NEG:
        eval = - c;
        break;
      default:
        assert false;
    }
    return new Constant(eval);
  }


  /*
  private ValNoTable vnTable = new ValNoTable();

  public void DominanceValueNumbering(IrFunct funct, Map<BasicBlock, GraphInfo> dt) {
    DomValNumTech(funct.bbs.list.Head(), dt);
  }


  private void DomValNumTech(BasicBlock cur, Map<BasicBlock, GraphInfo> dt) {
    vnTable.BeginScope();

    // check for all phi node.
    for (Quad quad : cur.quads) {
      if (quad instanceof Phi) {
        boolean redundant = PhiRedundant((Phi) quad);
        if (!redundant) {
          vnTable.Put(((Phi) quad).var, ((Phi) quad).var.name);
        }
      }
    }

    for (int i = 0; i < cur.quads.size(); ++i) {
      Quad cqd = cur.quads.get(i);
      if (cqd instanceof Binary) {
        Binary bin = (Binary) cqd;
        assert vnTable.ContainsKey(bin.src1) &&
        bin.src1
      }
      else if (cqd instanceof Unary) {
        Unary unary = (Unary) cqd;
      }
      else if (cqd instanceof Mov) {
        Mov mov = (Mov) cqd;
      }
    }

    // visit child in dom recursively.
    dt.get(cur).domTree.forEach(x -> DomValNumTech(x, dt));
    vnTable.EndScope();
  }
  */
}

class ValNoTable {
  private List<HashMap<Reg, Reg>> vnMap = new ArrayList<>();

  // a new scope will always be allocated before first put.
  ValNoTable() {
  }

  void Put(Reg reg, Reg vn) {
    vnMap.get(vnMap.size() - 1).put(reg, vn);
  }

  Reg Get(Reg reg) {
    for (int i = vnMap.size() - 1; i >= 0; --i) {
      if (vnMap.get(i).containsKey(reg))
        return vnMap.get(i).get(reg);
    }
    assert false;
    return null;
  }

  void BeginScope() {
    vnMap.add(new HashMap<>());
  }

  void EndScope() {
    assert vnMap.size() >= 1;
    vnMap.remove(vnMap.size() - 1);
  }

  boolean ContainsKey(Reg key) {
    for (int i = vnMap.size() - 1; i >= 0; --i) {
      if (vnMap.get(i).containsKey(key))
        return true;
    }
    return false;
  }
}