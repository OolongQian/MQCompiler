package ir_codegen.util;

import java.util.Hashtable;

public class Namer {
  private static Hashtable<String, Integer> record = new Hashtable<>();

  public static String GetHintName(String hintName) {
    if (!record.containsKey(hintName.intern())) {
      record.put(hintName, 0);
    }
    int index = record.get(hintName.intern());
    record.put(hintName.intern(), index + 1);
    return hintName + Integer.toString(index);
  }
}
