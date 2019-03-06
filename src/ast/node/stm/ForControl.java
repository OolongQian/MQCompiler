package ast.node.stm;

    import ast.node.dec.variable.LocalVarDec;
    import ast.node.exp.Exp;
    import ast.usage.AstBaseVisitor;

    import java.util.LinkedList;
    import java.util.List;

public class ForControl extends Stm {
  public LocalVarDec initDec;
  public List<Exp> initExps = new LinkedList<>();
  public boolean initIsDec;
  public Exp check;
  public List<Exp> updateExps = new LinkedList<>();

  public boolean isInitNull() {
    return initDec == null && initExps.size() == 0;
  }

  @Override
  public String toString() {
    String selfie = "(";
    // init
    if (initIsDec)
      selfie += initDec.toString();
    else {
      if (initExps.size() > 0) selfie += initExps.get(0).toString();
      for (int i = 1; i < initExps.size(); ++i)
        selfie += ", " + initExps.get(i).toString();
    }
    selfie += "; ";
    // check
    selfie += (check != null) ? check.toString() : "";
    selfie += "; ";
    // udpate
    if (updateExps.size() > 0) selfie += updateExps.get(0).toString();
    for (int i = 1; i < updateExps.size(); ++i)
      selfie += ", " + updateExps.get(i).toString();
    selfie += ")";
    return selfie;
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }
}
