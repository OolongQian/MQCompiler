package ast.typeref;

import ast.node.dec.class_.ClassDec;
import ast.node.dec.variable.VarDec;
import ast.node.exp.Exp;
import ast.usage.AstBaseVisitor;

import java.util.Hashtable;

import static ir.builder.Config.INT_SIZE;

/**
 * This class is really something.
 *
 * Every VarTypeRef has a spaceSize associated with it, so that
 * ir determines how many space should be allocated to it.
 *
 * VarTypeRef actually contains all type information, which means
 * it has combination array-type front-end, and also has baseType
 * actual classDec back-end.
 *
 * when its typeSize needs to be calculated, it pushes the work
 * down when this is an arrayType. But when it's base, it needs to
 * investigate classDec backEnd.
 * */
public class VarTypeRef extends TypeRef {

  /**
   * the most-inner type's ClassDec.
   * Built-in type is constructed as a class.
   * */
  public ClassDec baseType = null;

  /**
   * Is null if this is a primitive type.
   * otherwise it's an array type.
   * */
  public VarTypeRef innerType = null;
  public Exp dim = null;

  // NOTE : typeSize calculate method used by ir to determine class member's offset.
  // NOTE : typeSize isn't multiplied by dim
  private int typeSpace = 0;

  public int GetTypeSpace() {
    assert !isNull();

    if (typeSpace != 0) {
      return typeSpace;
    }

    // reserved for array_size and array_headPtr.
    if (isArray()) {
      typeSpace = INT_SIZE;
    }
    else if(isInt() || isBool()) {
      typeSpace = INT_SIZE;
    }
    else if (isString()) {
      typeSpace = INT_SIZE;
    }
    else {
      // user defined type
      for (VarDec field : baseType.fields) {
        // FIXME : typeSpace += field.varType.GetTypeSpace();
        // NOTE : object type seems to be pointer, thus has typeSpace 4.
        typeSpace += INT_SIZE;
      }
    }
    return typeSpace;
  }

  public int GetArrayOffset(int i) {
    if (!isArray())
      throw new RuntimeException("non-array type access...\n");
    return GetTypeSpace() * i;
  }

  public int GetFieldIndex(String fieldName) {
    int index = 0;
    for (VarDec field : baseType.fields) {
      if (field.varName.equals(fieldName))
        break;
      else
        index++;
    }
    return index;
  }

  public int GetFieldOffset(String fieldName) {
    if (isArray())
      throw new RuntimeException("arrayType has no field...\n");

    int index = GetFieldIndex(fieldName);
    int offset = 0;
    // NOTE field access cannot occur in built-in type, don't worry.
    for (int i = 0; i < index; ++i) {
      offset += baseType.fields.get(i).varType.GetTypeSpace();
    }
    return offset;
  }

  /**
   * Constructors.
   * **************************************************************/
  private static Hashtable<String, VarTypeRef> primitiveTypePool = new Hashtable<>();

  public static VarTypeRef CreatePrimitiveType(String typeName) {
    if (primitiveTypePool.containsKey(typeName)) {
      return primitiveTypePool.get(typeName);
    } else {
      VarTypeRef type = new VarTypeRef(typeName);
      primitiveTypePool.put(typeName, type);
      return type;
    }
  }

  public static VarTypeRef CreateArrayType(VarTypeRef baseType, Exp bound) {
    return new VarTypeRef(baseType, bound);
  }

  /**
   * typeThread is filled during type resolution.
   * */
  private VarTypeRef(String typeName) {
    this.typeName = typeName;
  }

  private VarTypeRef(VarTypeRef innerType, Exp dim) {
    this.innerType = innerType;
    this.dim = dim;
    typeName = innerType.GetTypeName() + "*";
  }

  /**
   * Gets and Sets
   * ***************************************************************/
  public String GetTypeName() {
    return typeName;
  }

  public void SetBaseType(ClassDec base) {
    baseType = base;
    VarTypeRef tmp = innerType;
    while (tmp != null) {
      tmp.baseType = base;
      tmp = tmp.innerType;
    }
  }

  public VarTypeRef GetPointerType() {
    return CreateArrayType(this, null);
  }

  public String GetBaseTypeName() {
    String ret = typeName;
    VarTypeRef tmp = innerType;
    while (tmp != null) {
      ret = tmp.typeName;
      tmp = tmp.innerType;
    }
    return ret;
  }

  /**
   * distinguish primitive-builtin type
   ******************************************************************/
  public boolean isInt() {
    return typeName.equals("int");
  }

  public boolean isBool() {
    return typeName.equals("bool");
  }

  public  boolean isString() {
    return typeName.equals("string");
  }

  public  boolean isVoid() {
    return typeName.equals("void");
  }

  public  boolean isNull() {
    return typeName.equals("null");
  }

  public boolean isArray() {
    return innerType != null;
  }

  /**
   * NOTE : In Mx, class types are referred as pointer types.
   * NOTE : string is excluded. pretend string is a primitive type.
   * */
  public boolean isObj() {
    return !isArray() && !isInt() && !isBool() && !isString();
  }

  @Override
  public String toString() {
    return typeName;
  }

  @Override
  public <T> T Accept(AstBaseVisitor<T> visitor) {
    return visitor.visit(this);
  }

  /**
   * Determine whether two simple varTypeRef equals, arrayTypeRef has inherited Equal method.
   * */
  public boolean Equal(VarTypeRef varTypeRef) {
    return varTypeRef.getClass() == VarTypeRef.class && typeName.equals(varTypeRef.typeName);
  }
}
