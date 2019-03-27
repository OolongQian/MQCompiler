package opt;

import ir.quad.Quad;

import java.util.HashSet;
import java.util.Set;

/**
 * used to store ssa register profile.
 * About def and use.
 * */
public class DefuseInfo {
	public Set<Quad> useQuads = new HashSet<>();
	public Quad defQuad;
}
