package labjframework.packs;

import java.io.File;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import labjframework.utilities.XMLFormattedText;

public class PackReference {
// representation of a reference to a specific XML entry
	
	private String refPack = "";
	private String refClass = "";
	private String refID = "";
	private XMLFormattedText representation = null; // the string representing this reference
	
	// constants
	public static final String REFERENCE = "Reference"; // the root node of the reference
	public static final String REF_PACK = "ReferencePack"; // the pack to be referenced
	public static final String REF_CLASS = "ReferenceClass"; // the class represented by this entry
	public static final String REF_ID = "ReferenceID"; // the ID to be referenced
	public static final String REF_REPRESENTATION = "ReferenceRepresentation"; // the representative formatted text
	
	// create reference from classes
	public PackReference(Pack pack, Class<? extends PackEntry> myClass, String iD, XMLFormattedText representation) {
		this.refPack = pack.getPackFile().getName();
		this.refClass = myClass.getName();
		this.refID = iD;
		this.representation = representation;
	}
	
	// create reference from Strings
	public PackReference(String pack, String myClass, String iD, XMLFormattedText representation) {
		this.refPack = pack;
		this.refClass = myClass;
		this.refID = iD;
		this.representation = representation;
	}
	
	// create reference from an entry
	public PackReference(PackEntry entry, XMLFormattedText representation) {
		this.refPack = entry.getPack().getPackFile().getName();
		this.refClass = entry.getClass().getName();
		this.refID = entry.getId();
		this.representation = representation;
	}
	
	// create reference from node
	public PackReference(Node node) {
		if (node != null) {
			Element el = (Element) node;
			// fetch ID
			NodeList nl = el.getElementsByTagName(REF_ID);
			if (nl != null && nl.getLength() > 0) {
				this.refID = nl.item(0).getTextContent();
			}
			// fetch pack
			nl = el.getElementsByTagName(REF_PACK);
			if (nl != null && nl.getLength() > 0) {
				this.refPack = nl.item(0).getTextContent();
			}
			// fetch class
			nl = el.getElementsByTagName(REF_CLASS);
			if (nl != null && nl.getLength() > 0) {
				this.refClass = nl.item(0).getTextContent();
			}
			// fetch representation
			nl = el.getElementsByTagName(REF_REPRESENTATION);
			if (nl != null && nl.getLength() > 0) {
				this.representation = new XMLFormattedText(nl.item(0));
			}
		}
	}
	
	public Node toNode() {
		
	}
	
	public String toXMLString() {
		
	}
	
	public PackEntry findReference() {
		
	}

	// return the representation if cast to a String
	@Override
	public String toString() {
		return this.representationString;
	}
}
