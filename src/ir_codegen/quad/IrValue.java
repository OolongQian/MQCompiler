package ir_codegen.quad;

/**
 * IrValue is the temp ir node name assigned to each expression with a value.
 * */
public interface IrValue {
  void AssignIrTmp(String hintName);
  String GetIrTmp();
}
