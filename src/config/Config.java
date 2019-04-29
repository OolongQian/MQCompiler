package config;

public class Config {
	public static final int INT_SIZE = 8;
	public static final boolean TEST = true;
	public static final boolean LOG = false;
	public static final boolean LINENO = false;
	public static final boolean SSALOG = false;

	public static final boolean ALLOCAREGS = true;
	public static final boolean CALLERSAVE = false;
	public static final boolean CALLEESAVE = false;
	public static final boolean COMMENTNASM = false;
	
	public static final boolean DEBUGPRINT_LIVENESS = false;
	public static final boolean DEBUGPRINT_INTERFERE = false;
	public static final boolean DEBUGPRINT_INTERFERE_GRAPHVIZ = false;
	public static final boolean DEBUGPRINT_VIRTUAL = false;
	
	public static final boolean DEBUGPRINT_VREG = true;
	
	// for object null type.
	// for primitive type, will be optimized.
	public static final int NULL = 0;
	
	public static final int INLINE_LEVEL = 0;

	public static final boolean INTERP_LOG = false;
}
