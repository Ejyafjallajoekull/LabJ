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

public class TaxonomyPack extends Pack {

	// fields	
	private static final String TAXONOMY = "Taxonomy"; // the node containing all relevant information for this taxonomy entity and its childs 
	private static final String SUB_PARENT = "Parent"; // the node referencing the parent taxon of a specific entry  
	private static final String[] SUB_PROPERTIES = {SUB_ID, SUB_NAME, SUB_DISPLAY, SUB_ATTACHMENT, SUB_COMMENT, SUB_PARENT}; // a list of all properties of a substance

	// constructor
	public TaxonomyPack(File file) {
		super(file, TAXONOMY, SUB_PROPERTIES);
	}

	@Override
	public boolean loadEntry(Element entry) {
		if (entry != null) {
			String taxID = ""; // ID
			PackReference parent = null; // parent taxon
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
						taxID = propList.item(0).getTextContent(); // take the first item
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
						LoggingHandler.getLog().info("No instances of " + prop + " detected for subtance " + taxID + " in substance pack " + this.packFile);
					}
					break;
					
				case SUB_ATTACHMENT:
					if (propList != null && propList.getLength() > 0) {
						for (int n = 0; n < propList.getLength(); n++) {
							att.add(new XMLFormattedText(propList.item(n)));
						}
					} else {
						LoggingHandler.getLog().info("No instances of " + prop + " detected for subtance " + taxID + " in substance pack " + this.packFile);
					}
					break;
					
				case SUB_PARENT:
					if (propList != null && propList.getLength() > 0) {
						if (propList.getLength() > 1) {
							LoggingHandler.getLog().warning("Multiple instances of " + prop + " detected for subtance " + taxID + " in substance pack " + this.packFile);
						}
						parent = new PackReference(propList.item(0)); // take the first item
					} else {
						LoggingHandler.getLog().info("No instances of " + prop + " detected for subtance " + taxID + " in substance pack " + this.packFile);
					}
					break;
					
				case SUB_NAME:
					if (propList != null && propList.getLength() > 0) {
						names = new String[propList.getLength()];
						for (int n = 0; n < propList.getLength(); n++) {
							names[n] = propList.item(n).getTextContent();
						}
					} else {
						LoggingHandler.getLog().info("No instances of " + prop + " detected for subtance " + taxID + " in substance pack " + this.packFile);
					}
					break;
					
				case SUB_DISPLAY:
					if (propList != null && propList.getLength() > 0) {
						if (propList.getLength() > 1) {
							LoggingHandler.getLog().warning("Multiple instances of " + prop + " detected for subtance " + taxID + " in substance pack " + this.packFile);
						}
						name = propList.item(0).getTextContent(); // take the first item
					} else {
						LoggingHandler.getLog().info("No instances of " + prop + " detected for subtance " + taxID + " in substance pack " + this.packFile);
					}
					break;
					
				default:
					LoggingHandler.getLog().warning("Detected unknown property " + prop + "for subtance " + taxID + " in substance pack " + this.packFile);
					break; // do nothing
				}
				
			}
			if (taxID != null && !taxID.isEmpty()) { // confirm valid ID
				Taxonomy tax = new Taxonomy(taxID, names, parent, this);
				tax.setComments(comm);
				tax.setAttachments(att);
				tax.setDisplayName(name);
				this.addEntry(tax);
				LoggingHandler.getLog().fine("Taxonomy " + taxID + ":" + name + " was loaded from TaxonomyPack " + this.getPackFile());
			}
		}
		return false;
	}

	@Override
	public void updateCustomProperties(Node parentNode, PackEntry entry) {
		if (this.packDoc != null && entry != null && parentNode != null) {
			Taxonomy tax = (Taxonomy) entry;
			// parent
			if (tax.getParentTaxonReference() != null) {
				this.addXMLProperty(parentNode, SUB_PARENT, tax.getParentTaxonReference().toNode());
			}
		}
	}

}
