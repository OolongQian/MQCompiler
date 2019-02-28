package type_check.symbol_table;

/**
 * A class for efficient String comparison.
 * Note that string in java is compared using .equals() method, which traverse over all character literals in string.
 * But if Strings are encapsulated in Symbol Obj, and the 'literally equal' strings corresponds to the same Symbol Obj,
 * we could compare the pointer value (their position in main memory) to determine the equality of the strings.
 * */
public class Symbol {
  private String name;
  private static java.util.Hashtable<String, Symbol> dict = new java.util.Hashtable<String, Symbol>();
  // private constructor, Symbols are constructed by GetSymbol() method.
  private Symbol(String n) { name = n; }

  /**
   * Get the corresponding Symbol for input String
   * @param n: input string.
   * @return Symbol : the normalized representation for input String n.
   * */
  public static Symbol GetSymbol (String n) {
    String n_norm = n.intern();

    Symbol s = dict.get(n_norm);
    if (s == null) { // if no record, create and store.
      s = new Symbol(n_norm);
      dict.put(n_norm, s);
    }

    return s;
  }

  public static boolean HaveDefined (String n) {
    String n_norm = n.intern();
    return dict.containsKey(n_norm);
  }

  /**
   * Get the underlying String represented by the Symbol.
   * */
  @Override
  public String toString() {
    return name;
  }
}
