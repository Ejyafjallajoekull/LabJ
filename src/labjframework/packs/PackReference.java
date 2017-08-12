package labjframework.packs;

import org.w3c.dom.Node;

public class PackReference {
// representation of a reference to a specific XML entry
	
	private Pack refPack = null;
	private Class refClass = null;
	private String refID = "";
	private String representationString = ""; // the string representing this reference
	
	// constants
	public static final String REF_NODE = "Reference"; // the root node of the reference
	public static final String REF_PACK = "ReferencePack"; // the pack to be referenced
	public static final String REF_CLASS = "ReferenceClass"; // the class represented by this entry
	public static final String REF_ID = "ReferenceID"; // the ID to be referenced
	
	public PackReference(Pack pack, Class myClass, String iD, String representation) {
		this.refPack = pack;
		this.refClass = myClass;
		this.refID = iD;
		this.representationString = representation;
	}
	
	public Node toNode() {
		
	}
	
	public String toXMLString() {
		
	}

	// return the representation if cast to a String
	@Override
	public String toString() {
		return this.representationString;
	}
}
