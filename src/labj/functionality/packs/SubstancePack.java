package labj.functionality.packs;

import java.io.File;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import labjframework.logging.LoggingHandler;
import labjframework.packs.Pack;
import labjframework.packs.PackEntry;
import labjframework.utilities.XMLFormattedText;
import labjframework.utilities.XMLUtilities;

public class SubstancePack extends Pack{

	// fields	
	private static final String SUBSTANCE = "Substance"; // the substance node containing all relevant information for substance recreation on load
	private static final String SUB_MOLECULARWEIGHT = "MolecularWeight"; // a substance attribute node
	private static final String SUB_DENSITY = "Density"; // a substance attribute node
	private static final String[] SUB_PROPERTIES = {SUB_ID, SUB_MOLECULARWEIGHT, SUB_DENSITY, SUB_NAME, SUB_DISPLAY, SUB_ATTACHMENT, SUB_COMMENT}; // a list of all properties of a substance
	
	
	// constructor
	public SubstancePack(File file) {
		super(file, SUBSTANCE, SUB_PROPERTIES);
	}
	
	// methods
	
	// loads substances from XML
	@Override
	public boolean loadEntry(Element entry) {
		if (entry != null) {
			String subID = ""; // ID
			BigDecimal M = new BigDecimal(0); // molecular weight
			BigDecimal d = new BigDecimal(0); // density
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
						subID = propList.item(0).getTextContent(); // take the first item
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
						LoggingHandler.getLog().info("No instances of " + prop + " detected for subtance " + subID + " in substance pack " + this.packFile);
					}
					break;
					
				case SUB_ATTACHMENT:
					if (propList != null && propList.getLength() > 0) {
						for (int n = 0; n < propList.getLength(); n++) {
							att.add(new XMLFormattedText(propList.item(n)));
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
				LoggingHandler.getLog().fine("Substance " + subID + ":" + name + " was loaded from SubstancePack " + this.getPackFile());
			}
		}
		return false;
	}

	@Override
	public void updateCustomProperties(Node parentNode, PackEntry entry) {
		if (this.packDoc != null && entry != null && parentNode != null) {
			Substance substance = (Substance) entry;
			// molecular weight
			this.addProperty(parentNode, SUB_MOLECULARWEIGHT, substance.getMolecularWeight().toString());
			// density
			this.addProperty(parentNode, SUB_DENSITY, substance.getDensity().toString());
		}			
	}
	
}
