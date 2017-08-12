package labj.functionality.packs;

import java.io.File;
import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import labjframework.logging.LoggingHandler;
import labjframework.packs.Pack;
import labjframework.packs.PackEntry;

public class OrganismPack extends Pack {
	
	// constants
	public static final String ORGANISM = "Organsim"; // the node name
	public static final String SUB_NAME = "Name"; // identifier for name properties
	public static final String SUB_DISPLAY = "DisplayName"; // identifier for name to display
	public static final String SUB_TAXONOMY = "Taxonomy"; // identifier for taxonomy properties
	private static final String[] SUB_PROPERTIES = {SUB_ID, SUB_ATTACHMENT, SUB_COMMENT, SUB_NAME, SUB_DISPLAY, SUB_TAXONOMY}; // a list of all properties of a substance


	public OrganismPack(File file) {
		super(file, ORGANISM, SUB_PROPERTIES);
	}

	@Override
	public boolean loadEntry(Element entry) {
		if (entry != null) {
			String subID = ""; // ID
			ArrayList<String> tax = new ArrayList<String>(); // taxonomy
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
					
				case SUB_TAXONOMY:
					if (propList != null && propList.getLength() > 0) {
						for (int n = 0; n < propList.getLength(); n++) {
							tax.add(propList.item(n).getTextContent());
						}
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
				Organism org = new Organism(subID, names, this);
				org.setTaxonomy(tax);
				org.setComments(comm);
				org.setAttachments(att);
				org.setDisplayName(name);
				this.addEntry(org);
			}
		}
		return false;
	}

	@Override
	public void updateCustomProperties(Node parentNode, PackEntry entry) {
		// TODO Auto-generated method stub
		
	}

}
