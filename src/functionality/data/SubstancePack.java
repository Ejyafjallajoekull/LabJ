package functionality.data;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import functionality.housekeeping.logging.LoggingHandler;

public class SubstancePack extends Pack{

	// fields	
	private static final String SUBSTANCE = "Substance"; // the substance node containing all relevant information for substance recreation on load
	private static final String SUB_MOLECULARWEIGHT = "MolecularWeight"; // a substance attribute node
	private static final String SUB_DENSITY = "Density"; // a substance attribute node
	private static final String SUB_NAME = "TrivialName"; // a node representing a name of the substance
	private static final String SUB_DISPLAY ="DisplayName"; // a node representing the name index of the displayed substance name
	private static final String[] SUB_PROPERTIES = {SUB_ID, SUB_MOLECULARWEIGHT, SUB_DENSITY, SUB_NAME, SUB_DISPLAY}; // a list of all properties of a substance
	
	
	// constructor
	public SubstancePack(File file) {
		super(file, SUBSTANCE, SUB_PROPERTIES);
	}
	
	// methods
	
	// loads substances from XML
	public boolean loadEntry(Element entry) {
		if (entry != null) {
			String subID = ""; // ID
			BigDecimal M = new BigDecimal(0); // molecular weight
			BigDecimal d = new BigDecimal(0); // density
			ArrayList<String> comm = new ArrayList<String>(); // comments
			ArrayList<String> att = new ArrayList<String>(); // attachments
			String name = ""; // display name index
			String[] names = {}; // trivial names
			for(String prop : SUB_PROPERTIES) { // iterate over all properties of the substance
				NodeList propList = entry.getElementsByTagName(prop); // mainly relevant for trivial names as there is more than one instance
				switch (prop) {
				
				case SUB_ID:
					if (propList != null && propList.getLength() > 0) {
						if (propList.getLength() > 1) {
							LoggingHandler.getLog().warning("Multiple IDs detected for subtance " + propList.item(0).getTextContent() + " in substance pack " + this.packFile);
						}
						subID = propList.item(0).getTextContent(); // take the first item
					} else {
						LoggingHandler.getLog().severe("No ID detected for a subtance in substance pack " + this.packFile);
					}
					break;
					
				case SUB_COMMENT:
					if (propList != null && propList.getLength() > 0) {
						for (int n = 0; n < propList.getLength(); n++) {
							comm.add(propList.item(n).getTextContent());
						}
					} else {
						LoggingHandler.getLog().info("No instances of " + prop + " detected for subtance " + subID + " in substance pack " + this.packFile);
					}
					break;
					
				case SUB_ATTACHMENT:
					if (propList != null && propList.getLength() > 0) {
						for (int n = 0; n < propList.getLength(); n++) {
							att.add(propList.item(n).getTextContent());
						}
					} else {
						LoggingHandler.getLog().info("No instances of " + prop + " detected for subtance " + subID + " in substance pack " + this.packFile);
					}
					break;
					
				case SUB_MOLECULARWEIGHT:
					if (propList != null && propList.getLength() > 0) {
						if (propList.getLength() > 1) {
							LoggingHandler.getLog().warning("Multiple instances of " + prop + " detected for subtance " + subID + " in substance pack " + this.packFile);
						}
						M = new BigDecimal(propList.item(0).getTextContent()); // take the first item
					} else {
						LoggingHandler.getLog().severe("No instances of " + prop + " detected for subtance " + subID + " in substance pack " + this.packFile);
					}
					break;
					
				case SUB_DENSITY:
					if (propList != null && propList.getLength() > 0) {
						if (propList.getLength() > 1) {
							LoggingHandler.getLog().warning("Multiple instances of " + prop + " detected for subtance " + subID + " in substance pack " + this.packFile);
						}
						d = new BigDecimal(propList.item(0).getTextContent()); // take the first item
					} else {
						LoggingHandler.getLog().severe("No instances of " + prop + " detected for subtance " + subID + " in substance pack " + this.packFile);
					}
					break;
					
				case SUB_NAME:
					if (propList != null && propList.getLength() > 0) {
						names = new String[propList.getLength()];
						for (int n = 0; n < propList.getLength(); n++) {
							names[n] = propList.item(n).getTextContent();
						}
					} else {
						LoggingHandler.getLog().info("No instances of " + prop + " detected for subtance " + subID + " in substance pack " + this.packFile);
					}
					break;
					
				case SUB_DISPLAY:
					if (propList != null && propList.getLength() > 0) {
						if (propList.getLength() > 1) {
							LoggingHandler.getLog().warning("Multiple instances of " + prop + " detected for subtance " + subID + " in substance pack " + this.packFile);
						}
						name = propList.item(0).getTextContent(); // take the first item
					} else {
						LoggingHandler.getLog().info("No instances of " + prop + " detected for subtance " + subID + " in substance pack " + this.packFile);
					}
					break;
					
				default:
					LoggingHandler.getLog().warning("Detected unknown property " + prop + "for subtance " + subID + " in substance pack " + this.packFile);
					break; // do nothing
				}
				
			}
			if (subID != null && !subID.isEmpty()) { // confirm valid ID
				Substance sub = new Substance(subID, names, M, d, this);
				sub.setComments(comm);
				sub.setAttachments(att);
				sub.setDisplayName(name);
				this.addEntry(sub);
			}
		}
		return false;
	}
	
/*	@Override
	public void appendEntry(PackEntry entry) {
		if (entry != null) {
			Substance substance = (Substance) entry;
			if (this.packDoc.getDocumentElement() == null) {
				this.packDoc.appendChild(this.packDoc.createElement(ROOT));
			}
			Node sub = this.packDoc.createElement(SUBSTANCE);
			this.packDoc.getDocumentElement().appendChild(sub);
			// ID
			Node subProp = this.packDoc.createElement(SUB_ID);
			sub.appendChild(subProp);
			subProp.appendChild(this.packDoc.createTextNode(substance.getId().toString()));
			// comments
			for (String comment : substance.getComments()) {
				subProp = this.packDoc.createElement(SUB_COMMENT);
				sub.appendChild(subProp);
				subProp.appendChild(this.packDoc.createTextNode(comment));

			}
			// attachments
			for (String attachment : substance.getAttachments()) {
				subProp = this.packDoc.createElement(SUB_ATTACHMENT);
				sub.appendChild(subProp);
				subProp.appendChild(this.packDoc.createTextNode(attachment));

			}
			// trivial names
			for (String trivialName : substance.getTrivialNames()) {
				subProp = this.packDoc.createElement(SUB_NAME);
				sub.appendChild(subProp);
				subProp.appendChild(this.packDoc.createTextNode(trivialName));

			}
			// display name
			subProp = this.packDoc.createElement(SUB_DISPLAY);
			sub.appendChild(subProp);
			subProp.appendChild(this.packDoc.createTextNode(substance.getDisplayName()));
			// molecular weight
			subProp = this.packDoc.createElement(SUB_MOLECULARWEIGHT);
			sub.appendChild(subProp);
			subProp.appendChild(this.packDoc.createTextNode(substance.getMolecularWeight().toString()));
			// density
			subProp = this.packDoc.createElement(SUB_DENSITY);
			sub.appendChild(subProp);
			subProp.appendChild(this.packDoc.createTextNode(substance.getDensity().toString()));

		}
	}
	
	@Override
	public void editEntry(PackEntry entry) {
		if (entry != null) {
			Substance substance = (Substance) entry;
			NodeList substanceList = this.packDoc.getDocumentElement().getElementsByTagName(SUBSTANCE);
			for (int i = 0; i < substanceList.getLength(); i++) {
				System.out.println(i + ": " + substanceList.item(i).getFirstChild().getTextContent());
				if (substanceList.item(i).getFirstChild().getTextContent().equals(substance.getId())) {
					System.out.println("Found you! " + i);
					return;
				}
			}
		}
	}*/

	@Override
	public void updateDocument() {
		if (this.packDoc != null) {
			Substance substance = null;
			if (this.packDoc.getDocumentElement() == null) {
				this.packDoc.appendChild(this.packDoc.createElement(ROOT));
			}
			NodeList nodes = this.getNodes();
			ArrayList<Element> elements = Pack.getElementList(nodes); // get all substance nodes and convert them to elements for extended functionality
			ArrayList<String> iDs = Pack.getIDList(elements); // get all corresponding IDs
			for (int i = 0; i < this.entries.size(); i++) {
				substance = (Substance) this.entries.get(i); 
				
					Node sub = this.packDoc.createElement(SUBSTANCE);
					// ID
					Node subProp = this.packDoc.createElement(SUB_ID);
					sub.appendChild(subProp);
					subProp.appendChild(this.packDoc.createTextNode(substance.getId().toString()));
					// comments
					for (String comment : substance.getComments()) {
						subProp = this.packDoc.createElement(SUB_COMMENT);
						sub.appendChild(subProp);
						subProp.appendChild(this.packDoc.createTextNode(comment));

					}
					// attachments
					for (String attachment : substance.getAttachments()) {
						subProp = this.packDoc.createElement(SUB_ATTACHMENT);
						sub.appendChild(subProp);
						subProp.appendChild(this.packDoc.createTextNode(attachment));

					}
					// trivial names
					for (String trivialName : substance.getTrivialNames()) {
						subProp = this.packDoc.createElement(SUB_NAME);
						sub.appendChild(subProp);
						subProp.appendChild(this.packDoc.createTextNode(trivialName));

					}
					// display name
					subProp = this.packDoc.createElement(SUB_DISPLAY);
					sub.appendChild(subProp);
					subProp.appendChild(this.packDoc.createTextNode(substance.getDisplayName()));
					// molecular weight
					subProp = this.packDoc.createElement(SUB_MOLECULARWEIGHT);
					sub.appendChild(subProp);
					subProp.appendChild(this.packDoc.createTextNode(substance.getMolecularWeight().toString()));
					// density
					subProp = this.packDoc.createElement(SUB_DENSITY);
					sub.appendChild(subProp);
					subProp.appendChild(this.packDoc.createTextNode(substance.getDensity().toString()));
				if (iDs.contains(substance.getId())) { // update substance if already existent
					this.packDoc.getDocumentElement().replaceChild(sub, nodes.item(iDs.indexOf(substance.getId())));
				} else { // add new substance if not present in the document
					this.packDoc.getDocumentElement().appendChild(sub);
				}
				
			}
		}			
	}
	
}
