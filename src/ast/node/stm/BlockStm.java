package ast.node.stm;


import ast.usage.AstBaseVisitor;

import java.util.LinkedList;
import java.util.List;

public class BlockStm extends Stm {
  public List<Stm> stms = new LinkedList<>();
  
  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
