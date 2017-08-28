package labj.functionality.packs;

import java.io.File;
import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import labjframework.logging.LoggingHandler;
import labjframework.packs.Pack;
import labjframework.packs.PackEntry;
import labjframework.packs.PackReference;
import labjframework.utilities.XMLFormattedText;

public class OrganismPack extends Pack {
	
	// constants
	public static final String ORGANISM = "Organsim"; // the node name
	public static final String SUB_TAXONOMY = "Taxonomy"; // identifier for taxonomy properties
	private static final String[] SUB_PROPERTIES = {SUB_ID, SUB_ATTACHMENT, SUB_COMMENT, SUB_NAME, SUB_DISPLAY, SUB_TAXONOMY}; // a list of all properties of a substance


	public OrganismPack(File file) {
		super(file, ORGANISM, SUB_PROPERTIES);
	}

	@Override
	public boolean loadEntry(Element entry) {
		if (entry != null) {
			String orgID = ""; // ID
			PackReference taxonomy = null; // taxonomy of this organism
			ArrayList<XMLFormattedText> comm = new ArrayList<XMLFormattedText>(); // comments
			ArrayList<XMLFormattedText> att = new ArrayList<XMLFormattedText>(); // attachments
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
						orgID = propList.item(0).getTextContent(); // take the first item
					} else {
						LoggingHandler.getLog().severe("No ID detected for a subtance in substance pack " + this.packFile);
					}
					break;
					
				case SUB_COMMENT:
					if (propList != null && propList.getLength() > 0) {
						for (int n = 0; n < propList.getLength(); n++) {
							comm.add(new XMLFormattedText(propList.item(n)));
						}
					} else {
						LoggingHandler.getLog().info("No instances of " + prop + " detected for subtance " + orgID + " in substance pack " + this.packFile);
					}
					break;
					
				case SUB_ATTACHMENT:
					if (propList != null && propList.getLength() > 0) {
						for (int n = 0; n < propList.getLength(); n++) {
							att.add(new XMLFormattedText(propList.item(n)));
						}
					} else {
						LoggingHandler.getLog().info("No instances of " + prop + " detected for subtance " + orgID + " in substance pack " + this.packFile);
					}
					break;
					
				case SUB_TAXONOMY:
					if (propList != null && propList.getLength() > 0) {
						if (propList.getLength() > 1) {
							LoggingHandler.getLog().warning("Multiple instances of " + prop + " detected for subtance " + orgID + " in substance pack " + this.packFile);
						}
						taxonomy = new PackReference(propList.item(0)); // take the first item
					} else {
						LoggingHandler.getLog().info("No instances of " + prop + " detected for subtance " + orgID + " in substance pack " + this.packFile);
					}
					break;
					
				case SUB_NAME:
					if (propList != null && propList.getLength() > 0) {
						names = new String[propList.getLength()];
						for (int n = 0; n < propList.getLength(); n++) {
							names[n] = propList.item(n).getTextContent();
						}
					} else {
						LoggingHandler.getLog().info("No instances of " + prop + " detected for subtance " + orgID + " in substance pack " + this.packFile);
					}
					break;
					
				case SUB_DISPLAY:
					if (propList != null && propList.getLength() > 0) {
						if (propList.getLength() > 1) {
							LoggingHandler.getLog().warning("Multiple instances of " + prop + " detected for subtance " + orgID + " in substance pack " + this.packFile);
						}
						name = propList.item(0).getTextContent(); // take the first item
					} else {
						LoggingHandler.getLog().info("No instances of " + prop + " detected for subtance " + orgID + " in substance pack " + this.packFile);
					}
					break;
					
				default:
					LoggingHandler.getLog().warning("Detected unknown property " + prop + "for subtance " + orgID + " in substance pack " + this.packFile);
					break; // do nothing
				}
				
			}
			if (orgID != null && !orgID.isEmpty()) { // confirm valid ID
				Organism org = new Organism(orgID, names, taxonomy, this);
				org.setComments(comm);
				org.setAttachments(att);
				org.setDisplayName(name);
				this.addEntry(org);
				LoggingHandler.getLog().fine("Taxonomy " + orgID + ":" + name + " was loaded from TaxonomyPack " + this.getPackFile());
			}
		}
		return false;
	}

	@Override
	public void updateCustomProperties(Node parentNode, PackEntry entry) {
		if (this.packDoc != null && entry != null && parentNode != null) {
			Organism organism = (Organism) entry;
			// taxonomy
			this.addXMLProperty(parentNode, SUB_TAXONOMY, organism.getTaxonomyReference().toNode());
		}	
	}

}
