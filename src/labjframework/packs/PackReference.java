package labjframework.packs;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import labjframework.logging.LoggingHandler;
import labjframework.utilities.XMLFormattedText;
import labjframework.utilities.XMLUtilities;

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
		try {
			// create reference root
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Node ref = doc.createElement(REFERENCE);
			doc.appendChild(ref);
			// create pack reference
			Node refChild = doc.createElement(REF_PACK);
			Node refValue = doc.createTextNode(this.refPack);
			refChild.appendChild(refValue);
			ref.appendChild(refChild);
			// create class reference
			refChild = doc.createElement(REF_CLASS);
			refValue = doc.createTextNode(this.refClass);
			refChild.appendChild(refValue);
			ref.appendChild(refChild);
			// create ID reference
			refChild = doc.createElement(REF_ID);
			refValue = doc.createTextNode(this.refID);
			refChild.appendChild(refValue);
			ref.appendChild(refChild);
			// append representation
			ref.appendChild(this.representation.toNode());
			return ref;
		} catch (ParserConfigurationException e) {
			LoggingHandler.getLog().log(Level.SEVERE, "Could not initialise a DocumentBuilder", e);
			e.printStackTrace();
			return null;
		}
	}
	
	public String toXMLString() {
		return XMLUtilities.asText(this.toNode());
	}
	
	public PackEntry findReference(PackHandler handler) {
		if (handler != null) {
			ArrayList<Pack> packList = handler.getLoadedPacks(new File(this.refPack)); // get all loaded packs from this file
			PackEntry entry = null;
			for (Pack pack : packList) {
				entry = pack.getByID(this.refID);
				if (entry != null) { // return reference when found
					return entry;
				}
			}
		}
		return null; // entry is not loaded, so return null
	}

	// return the representation of this object as String
	@Override
	public String toString() {
		return (this.refPack + ":" + this.refClass + ":" + this.refID);
	}
}
