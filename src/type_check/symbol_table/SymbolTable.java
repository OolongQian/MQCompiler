package type_check.symbol_table;

/**
 * SymbolTable is a special mapper catered to compiler's variable and type scanning and checking.
 * It maps Symbol Obj to 'concrete types' / 'actual values'.
 * Its interfaces are like map data structure, but with extra scope mechanism.
 *
 * Without enough sophisticated data structure, we adopt imperative front.Symbol table.
 * It means that a stack has to be incorporated with this table.
 * */

import java.util.Enumeration;

/**
 * This is a class private to front.Symbol package, used by SymbolTable class, and it is used to
 * 1. record value corresponding to key.
 * 2. record the previous Binder in the form of linkedList to builder1 a stack (together with hashTable we get a linked
 *    -list for Binders).
 * 3. record the previous Binder with the same key (override by its insertion) so that the
 *    previous version of value can be recovered.
 *
 * You know, this is actually something tricky. Because I may well insert different values multiple times with
 * the same key into HashTable, which will certainly override the old values. But since they are still referenced
 * in the binder's linked list, they aren't gone but still somewhere within my reach... I can still recover them
 * when endingScope without copy anything or explicitly do nasty stuff, etc... They are pure elegant pointer operations.
 * Amazing garbage management system!!! Good Job Java.
 * */
class Binder<T> {
  T value;
  Symbol prev_stack_key;
  Binder<T> overridden_binder;
  Binder(T v, Symbol pre_k, Binder<T> pre_b) { value = v; prev_stack_key = pre_k; overridden_binder = pre_b; }

  @Override
  public String toString() {
    return value.toString();
  }
}

public class SymbolTable<T> {

  private java.util.Hashtable<Symbol, Binder<T>> table = new java.util.Hashtable<>();
  private Symbol top_stack_key = null; // latest inserted key, located at top of stack.
  private Binder<T> null_marker = null;

  /**
   * Add an element in table, and deal with the linked-everything.
   * */
  public void Put(Symbol key, T value) {
//    System.out.println("put " + key.toString() + "\n");
    Binder<T> new_binder = new Binder<>(value, top_stack_key, table.get(key)); // link stack-top and overridden poor guy.
    top_stack_key = key; // update stack top.
    table.put(key, new_binder);
  }

  /**
   * Get the most recent version of value associated with input key.
   * */
  public T Get(Symbol key) throws NullPointerException {
    return table.get(key).value;
  }

  /**
   * Add a null marker to the linked-list, the null markers don't enter the HashTable, but they themselves
   * for a linked-list so that a stack-like scope structure is formed.
   * */
  public void BeginScope() {
//    System.out.println("begin scope\n");
    null_marker = new Binder<>(null, top_stack_key, null_marker);
    top_stack_key = null;
  }


  /**
   * Pop and recover Binders until reaching null_marker.
   * */
  public void EndScope() {
//    System.out.println("end scope\n");
    // TODO : This can be optimized for efficiency, but now I want clarity.
    while (top_stack_key != null) {
      // remove stack top binder
      Binder<T> now_binder = table.get(top_stack_key);
      table.remove(top_stack_key);

      // recover overridden version, if exist
      if (now_binder.overridden_binder != null)
        table.put(top_stack_key, now_binder.overridden_binder);

      // progress down along stack
      top_stack_key = now_binder.prev_stack_key;
    }
    top_stack_key = null_marker.prev_stack_key;
    null_marker = null_marker.overridden_binder;
  }

  /**
   * compiler variables name must be unique under current scope.
   * */
  public boolean ExistCurScope(Symbol key) {
    Binder<T> now_binder;
    Symbol cur_key = top_stack_key;
    while (cur_key != null) {
      if (cur_key == key)
        return true;
      now_binder = table.get(cur_key);
      cur_key = now_binder.prev_stack_key;
    }
    return false;
  }

  /**
   * Display all element in current scope, table.
   * */
  @Override
  public String toString() {
    String selfie = "";
    Enumeration<Symbol> e = table.keys();
    while (e.hasMoreElements()) {
      Symbol key = e.nextElement();
      selfie += table.get(key).toString();
    }
    return selfie;
  }
}
