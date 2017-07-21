package functionality.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

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

import functionality.housekeeping.logging.LoggingHandler;

public class SubstancePack extends Pack{

	// fields	
	private static final String ROOT = "SubstancePack"; // the XML root node
	private static final String SUBSTANCE = "Substance"; // the substance node containing all relevant information for substance recreation on load
	private static final String SUB_MOLECULARWEIGHT = "MolecularWeight"; // a substance attribute node
	private static final String SUB_DENSITY = "Density"; // a substance attribute node
	private static final String SUB_NAME = "TrivialName"; // a node representing a name of the substance
	private static final String SUB_DISPLAY ="DisplayName"; // a node representing the name index of the displayed substance name
	private static final String[] SUB_PROPERTIES = {SUB_ID, SUB_MOLECULARWEIGHT, SUB_DENSITY, SUB_NAME, SUB_DISPLAY}; // a list of all properties of a substance
	
	
	// constructor
	public SubstancePack(File file) {
		super(file);
		this.root = ROOT;
		this.node = SUBSTANCE;
		this.subProperties = SUB_PROPERTIES;
	}
	
	// methods
	
	// loads substances from XML
	public boolean loadPack() {
		if (this.packDoc != null) {
			NodeList substanceList = this.packDoc.getDocumentElement().getElementsByTagName(SUBSTANCE);
			for (int i = 0; i < substanceList.getLength(); i++) {
				System.out.println(i + ": " + substanceList.item(i).getNodeName());
				Element el = (Element) substanceList.item(i);
				String subID = ""; // ID
				BigDecimal M = new BigDecimal(0); // molecular weight
				BigDecimal d = new BigDecimal(0); // density
				int name = 0; // display name index
				String[] names = {}; // trivial names
				for(String prop : SUB_PROPERTIES) { // iterate over all properties of the substance
					NodeList propList = el.getElementsByTagName(prop); // mainly relevant for trivial names as there is more than one instance
					switch (prop) {
					
					case SUB_ID:
						if (propList != null && propList.getLength() > 0) {
							if (propList.getLength() != 1) {
								LoggingHandler.getLog().warning("Multiple instances of " + prop + " detected for subtance " + i + " in substance pack " + this.packFile);
							}
							subID = propList.item(0).getTextContent(); // take the first item
						} else {
							LoggingHandler.getLog().severe("No instances of " + prop + " detected for subtance " + i + " in substance pack " + this.packFile);
						}
						break;
						
					case SUB_MOLECULARWEIGHT:
						if (propList != null && propList.getLength() > 0) {
							if (propList.getLength() != 1) {
								LoggingHandler.getLog().warning("Multiple instances of " + prop + " detected for subtance " + i + " in substance pack " + this.packFile);
							}
							M = new BigDecimal(propList.item(0).getTextContent()); // take the first item
						} else {
							LoggingHandler.getLog().severe("No instances of " + prop + " detected for subtance " + i + " in substance pack " + this.packFile);
						}
						break;
						
					case SUB_DENSITY:
						if (propList != null && propList.getLength() > 0) {
							if (propList.getLength() != 1) {
								LoggingHandler.getLog().warning("Multiple instances of " + prop + " detected for subtance " + i + " in substance pack " + this.packFile);
							}
							d = new BigDecimal(propList.item(0).getTextContent()); // take the first item
						} else {
							LoggingHandler.getLog().severe("No instances of " + prop + " detected for subtance " + i + " in substance pack " + this.packFile);
						}
						break;
						
					case SUB_NAME:
						if (propList != null && propList.getLength() > 0) {
							names = new String[propList.getLength()];
							for (int n = 0; n < propList.getLength(); n++) {
								names[n] = propList.item(n).getTextContent();
							}
						} else {
							LoggingHandler.getLog().info("No instances of " + prop + " detected for subtance " + i + " in substance pack " + this.packFile);
						}
						break;
						
					case SUB_DISPLAY:
						if (propList != null && propList.getLength() > 0) {
							if (propList.getLength() != 1) {
								LoggingHandler.getLog().warning("Multiple instances of " + prop + " detected for subtance " + i + " in substance pack " + this.packFile);
							}
							name = Integer.parseInt(propList.item(0).getTextContent()); // take the first item
						} else {
							LoggingHandler.getLog().info("No instances of " + prop + " detected for subtance " + i + " in substance pack " + this.packFile);
						}
						break;
						
					default:
						LoggingHandler.getLog().warning("Detected unknown property " + prop + "for subtance " + i + " in substance pack " + this.packFile);
						break; // do nothing
					}
					
				}
				if (subID != null && !subID.isEmpty()) {
					Substance sub = new Substance(subID, M, d, this);
					sub.setTrivialNames(new ArrayList<String>(Arrays.asList(names)));
					sub.setDisplayName(name);
					this.addEntry(sub);
				}
			}
			//TODO: load file
		}		
		return false;
	}
	
	// save this pack to XML
	public void save() {
//		appendSubstance((Substance) entries.get(0));
	//	editSubstance(substances.get(0));
		try {
			Transformer trans = TransformerFactory.newInstance().newTransformer();
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no"); // write a XML declaration as header
			trans.setOutputProperty(OutputKeys.METHOD, "xml"); // write a XML document
			trans.setOutputProperty(OutputKeys.INDENT, "yes"); // allow auto whitespace
			trans.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name()); //use UTF-8 as encoding
			DOMSource source = new DOMSource(this.packDoc);
			StreamResult result;
			try {
				result = new StreamResult(new FileOutputStream(this.packFile));
				try {
					trans.transform(source, result);
				} catch (TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		} catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void appendSubstance(Substance substance) {
		if (this.packDoc.getDocumentElement() == null) {
			this.packDoc.appendChild(this.packDoc.createElement(ROOT));
		}
		Node sub = this.packDoc.createElement(SUBSTANCE);
		this.packDoc.getDocumentElement().appendChild(sub);
		Node subID = this.packDoc.createElement(SUB_ID);
		sub.appendChild(subID);
		subID.appendChild(this.packDoc.createTextNode(substance.getId().toString()));
		Node subMW = this.packDoc.createElement(SUB_MOLECULARWEIGHT);
		sub.appendChild(subMW);
		subMW.appendChild(this.packDoc.createTextNode(substance.getMolecularWeight().toString()));
	}
	
	private void editSubstance(Substance substance) {
		NodeList substanceList = this.packDoc.getDocumentElement().getElementsByTagName(SUBSTANCE);
		for (int i = 0; i < substanceList.getLength(); i++) {
			System.out.println(i + ": " + substanceList.item(i).getFirstChild().getTextContent());
			if (substanceList.item(i).getFirstChild().getTextContent().equals(substance.getId())) {
				System.out.println("Found you! " + i);
				return;
			}
		}
	}
}
