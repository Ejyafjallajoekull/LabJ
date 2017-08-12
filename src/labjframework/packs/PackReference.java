package labjframework.packs;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import labjframework.utilities.XMLFormattedText;

public class PackReference {
// representation of a reference to a specific XML entry
	
	private Pack refPack = null;
	private String refClass = "";
	private String refID = "";
	private XMLFormattedText representation = null; // the string representing this reference
	
	// constants
	public static final String REFERENCE = "Reference"; // the root node of the reference
	public static final String REF_PACK = "ReferencePack"; // the pack to be referenced
	public static final String REF_CLASS = "ReferenceClass"; // the class represented by this entry
	public static final String REF_ID = "ReferenceID"; // the ID to be referenced
	public static final String REF_REPRESENTATION = "ReferenceRepresentation"; // the representative formatted text
	
	public PackReference(Pack pack, Class<? extends PackEntry> myClass, String iD, XMLFormattedText representation) {
		this.refPack = pack;
		this.refClass = myClass.getName();
		this.refID = iD;
		this.representation = representation;
	}
	
	public PackReference(Pack pack, String myClass, String iD, XMLFormattedText representation) {
		this.refPack = pack;
		this.refClass = myClass;
		this.refID = iD;
		this.representation = representation;
	}
	
	public PackReference(Node node) {
		if (node != null) {
			Element el = (Element) node;
			NodeList nl = el.getElementsByTagName(REF_ID);
			if (nl != null && nl.getLength() > 0) {
				this.refID = nl.item(0).getTextContent();
			}
		}
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
