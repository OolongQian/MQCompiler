package ast.node.stm;

import ast.usage.AstBaseVisitor;

import java.util.LinkedList;
import java.util.List;

public class BlockStm extends Stm {
  public List<Stm> children = new LinkedList<>();

  @Override
  protected String SelfDeclare() {
    return "BlockStm: \n";
  }

  @Override
  public String PrettyPrint() {
    String selfie = "{\n";
    ++tab;
    for (int i = 0; i < children.size(); ++i)
      selfie += children.get(i).PrettyPrint();
    --tab;
    selfie += TabShift("}\n");
    return Formatter(this, selfie);
  }

  @Override
  public void Accept(AstBaseVisitor visitor) {
    visitor.visit(this);
  }
}
