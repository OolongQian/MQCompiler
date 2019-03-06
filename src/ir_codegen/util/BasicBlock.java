package ir_codegen.util;

import ir_codegen.quad.Quad;

import java.util.LinkedList;
import java.util.List;

import static ir_codegen.util.Namer.GetHintName;

public class BasicBlock {
  public List<Quad> quads = new LinkedList<>();
  public String labelName;

  public BasicBlock() {
    labelName = GetHintName("BB");
  }

  public BasicBlock(String name) {
    labelName = GetHintName(name);
  }

  public void Append(Quad quad) {
    quads.add(quad);
  }
}
