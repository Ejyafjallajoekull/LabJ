package functionality.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import functionality.housekeeping.logging.LoggingHandler;

public class SubstancePack {

	// fields
	private ArrayList<Substance> substances = new ArrayList<Substance>(); // a list of all substances in the pack
	private File substanceFile = null; // the XML containing all those substances
	private Document packDoc = null; // the document representing the substance pack XML
	
	private static final String ROOT = "SubstancePack"; // the XML root node
	private static final String SUBSTANCE = "Substance"; // the substance node containing all relevant information for substance recreation on load
	private static final String SUB_ID = "ID"; // the pack unique identifier of a substance
	private static final String SUB_MOLECULARWEIGHT = "MolecularWeight"; // a substance attribute node
	private static final String SUB_DENSITY = "Density"; // a substance attribute node
	private static final String SUB_NAME = "TrivialName"; // a node representing a name of the substance
	private static final String SUB_DISPLAY ="DisplayName"; // a node representing the name index of the displayed substance name
	private static final String[] SUB_PROPERTIES = {SUB_ID, SUB_MOLECULARWEIGHT, SUB_DENSITY, SUB_NAME, SUB_DISPLAY}; // a list of all properties of a substance
	
	
	// constructor
	public SubstancePack(File file) {
		if (file != null) {
			this.substanceFile = file;
			this.load();
		} else {
			LoggingHandler.getLog().warning("This substance pack has no corresponding file");
		}	
	}
	
	// methods
	// add a unique substance
	public boolean addSubstance(Substance substance) {
		if (substance != null && !substances.contains(substance)) {
			for (Substance item : substances) {
				if (item.getSubstanceId().equals(substance.getSubstanceId())) {
					return false; // only allow substances with different Id
				}
			}
			substance.setPack(this);
			substances.add(substance);
			return true;
		} else {
			return false;
		}
	}
	
	// loads substances from XML
	private void load() {
		// only load a file if it exists 
//		if (this.substanceFile.exists() && this.substanceFile.isFile()) {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				if (this.substanceFile.exists() && this.substanceFile.isFile()) {
					try {
						this.packDoc = db.parse(this.substanceFile);
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
											LoggingHandler.getLog().warning("Multiple instances of " + prop + " detected for subtance " + i + " in substance pack " + this.substanceFile);
										}
										subID = propList.item(0).getTextContent(); // take the first item
									} else {
										LoggingHandler.getLog().severe("No instances of " + prop + " detected for subtance " + i + " in substance pack " + this.substanceFile);
									}
									break;
									
								case SUB_MOLECULARWEIGHT:
									if (propList != null && propList.getLength() > 0) {
										if (propList.getLength() != 1) {
											LoggingHandler.getLog().warning("Multiple instances of " + prop + " detected for subtance " + i + " in substance pack " + this.substanceFile);
										}
										M = new BigDecimal(propList.item(0).getTextContent()); // take the first item
									} else {
										LoggingHandler.getLog().severe("No instances of " + prop + " detected for subtance " + i + " in substance pack " + this.substanceFile);
									}
									break;
									
								case SUB_DENSITY:
									if (propList != null && propList.getLength() > 0) {
										if (propList.getLength() != 1) {
											LoggingHandler.getLog().warning("Multiple instances of " + prop + " detected for subtance " + i + " in substance pack " + this.substanceFile);
										}
										d = new BigDecimal(propList.item(0).getTextContent()); // take the first item
									} else {
										LoggingHandler.getLog().severe("No instances of " + prop + " detected for subtance " + i + " in substance pack " + this.substanceFile);
									}
									break;
									
								case SUB_NAME:
									if (propList != null && propList.getLength() > 0) {
										names = new String[propList.getLength()];
										for (int n = 0; n < propList.getLength(); n++) {
											names[n] = propList.item(n).getTextContent();
										}
									} else {
										LoggingHandler.getLog().info("No instances of " + prop + " detected for subtance " + i + " in substance pack " + this.substanceFile);
									}
									break;
									
								case SUB_DISPLAY:
									if (propList != null && propList.getLength() > 0) {
										if (propList.getLength() != 1) {
											LoggingHandler.getLog().warning("Multiple instances of " + prop + " detected for subtance " + i + " in substance pack " + this.substanceFile);
										}
										name = Integer.parseInt(propList.item(0).getTextContent()); // take the first item
									} else {
										LoggingHandler.getLog().info("No instances of " + prop + " detected for subtance " + i + " in substance pack " + this.substanceFile);
									}
									break;
									
								default:
									LoggingHandler.getLog().warning("Detected unknown property " + prop + "for subtance " + i + " in substance pack " + this.substanceFile);
									break; // do nothing
								}
								
							}
							if (subID != null && !subID.isEmpty()) {
								Substance sub = new Substance(subID, M, d, this);
								sub.setTrivialNames(new ArrayList<String>(Arrays.asList(names)));
								sub.setDisplayName(name);
								this.addSubstance(sub);
							}
						}
						//TODO: load file
					} catch (SAXException | IOException e) {
						LoggingHandler.getLog().log(Level.SEVERE, "Could not load " + this.substanceFile, e);
						e.printStackTrace();
					}
				} else {
					this.packDoc = db.newDocument();
				}
			} catch (ParserConfigurationException e) {
				LoggingHandler.getLog().log(Level.SEVERE, "Could not load " + this.substanceFile, e);
				e.printStackTrace();
			}
//		} else {
	//		this.packDoc = null;
//		}
	}
	
	// save this pack to XML
	public void save() {
		appendSubstance(substances.get(0));
		editSubstance(substances.get(0));
		try {
			Transformer trans = TransformerFactory.newInstance().newTransformer();
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no"); // write a XML declaration as header
			trans.setOutputProperty(OutputKeys.METHOD, "xml"); // write a XML document
			trans.setOutputProperty(OutputKeys.INDENT, "yes"); // allow auto whitespace
			trans.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name()); //use UTF-8 as encoding
			DOMSource source = new DOMSource(this.packDoc);
			StreamResult result;
			try {
				result = new StreamResult(new FileOutputStream(this.substanceFile));
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
		subID.appendChild(this.packDoc.createTextNode(substance.getSubstanceId().toString()));
		Node subMW = this.packDoc.createElement(SUB_MOLECULARWEIGHT);
		sub.appendChild(subMW);
		subMW.appendChild(this.packDoc.createTextNode(substance.getMolecularWeight().toString()));
	}
	
	private void editSubstance(Substance substance) {
		NodeList substanceList = this.packDoc.getDocumentElement().getElementsByTagName(SUBSTANCE);
		for (int i = 0; i < substanceList.getLength(); i++) {
			System.out.println(i + ": " + substanceList.item(i).getFirstChild().getTextContent());
			if (substanceList.item(i).getFirstChild().getTextContent().equals(substance.getSubstanceId())) {
				System.out.println("Found you! " + i);
				return;
			}
		}
	}
}
