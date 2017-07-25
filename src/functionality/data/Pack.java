package functionality.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import functionality.housekeeping.logging.LoggingHandler;

public abstract class Pack {
// abstract class representing a XML file containing information structured in a LabJ compatible way
	
	// fields
	protected ArrayList<PackEntry> entries = new ArrayList<PackEntry>(); // a list of all entries in the pack

	protected File packFile = null; // the XML storing this pack
	protected Document packDoc = null; // the document representing the pack XML
	private boolean isLoaded = false; // is the corresponding file loaded?

	// mimic constants
	protected static final String ROOT = "Pack"; // the XML root node
	protected String node; // the node containing all relevant information
	protected String[] subProperties; // a list of all properties of a node
	protected static final String SUB_ID = "ID"; // the pack unique identifier of a entry
	protected static final String SUB_COMMENT = "Comment"; // the comment node of a entry
	protected static final String SUB_ATTACHMENT = "Attachment"; // the attachment node of a entry

	// constructor
	public Pack(File file, String node, String[] subProperties) {
		if (file != null) {
			this.packFile = file;
			this.node = node;
			this.subProperties = subProperties;
			// load the XML to the memory
			this.isLoaded = this.load();
		} else {
			LoggingHandler.getLog().warning("This pack has no corresponding file");
		}	
	}
	
	// abstract methods
	public abstract boolean loadEntry(Element entry); // implementation for all different entries on how to load them and their properties
	public abstract void updateDocument(); // update the document to reflect all changes made
	
	
	// methods
	// save this pack to XML
	public void save() {
		if (this.packFile != null && this.packDoc != null && this.isLoaded()) {
			try {
				Transformer trans = TransformerFactory.newInstance().newTransformer();
				trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no"); // write a XML declaration as header
				trans.setOutputProperty(OutputKeys.METHOD, "xml"); // write a XML document
				trans.setOutputProperty(OutputKeys.INDENT, "yes"); // allow auto whitespace
				trans.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name()); //use UTF-8 as encoding
				this.updateDocument();
				DOMSource source = new DOMSource(this.packDoc);
				StreamResult result;
				try {
					result = new StreamResult(new FileOutputStream(this.packFile));
					try {
						trans.transform(source, result);
					} catch (TransformerException e) {
						LoggingHandler.getLog().log(Level.SEVERE, "Transformation failed; file " + this.packFile + " not saved", e);
						e.printStackTrace();
					}
				} catch (FileNotFoundException e1) {
					LoggingHandler.getLog().log(Level.WARNING, "The file " + this.packFile + "corresponding to this pack wasn't found", e1);
					e1.printStackTrace();
				}			
			} catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
				LoggingHandler.getLog().log(Level.SEVERE, "Transformer cannot be created; file " + this.packFile + " not saved", e);
				e.printStackTrace();
			}
		} else {
			LoggingHandler.getLog().severe("Cannot save file " + this.packFile + " due to missing document (" + this.packDoc + ") or the pack not being loaded (" + this.isLoaded() + ")");
		}
	}
	
	// parse the XML file
	private boolean load() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			if (this.packFile.exists() && this.packFile.isFile()) {
				try {
					this.packDoc = db.parse(this.packFile);
					if (this.hasRoot()) {
						NodeList nodes = this.getNodes();
						for (int i = 0; i < nodes.getLength(); i++) {
						//	System.out.println(i + ": " + nodes.item(i).getNodeName());
							this.loadEntry((Element) nodes.item(i)); // load the entry of the given XML element
						}
						return true; // loading the XML worked
					}
				} catch (SAXException | IOException e) { // loading failed
					LoggingHandler.getLog().log(Level.SEVERE, "Could not load " + this.packFile, e);
					e.printStackTrace();
				}
			} else {
				this.packDoc = db.newDocument();
				this.packDoc.appendChild(this.packDoc.createElement(ROOT)); // create a root node
				return true; // at least a document was created
			}
		} catch (ParserConfigurationException e) { // loading failed
			LoggingHandler.getLog().log(Level.SEVERE, "Could not initialise a DocumentBuilder", e);
			e.printStackTrace();
		}
		return false;
	}
	
	
	// methods
	// add a unique substance and appends it to the XML if desired
	public boolean addEntry(PackEntry entry) {
		if (entry != null && !this.entries.contains(entry)) {
			for (PackEntry item : this.entries) {
				if (item.getId().equals(entry.getId())) {
					return false; // only allow substances with different Id
				}
			}
			// clone object and add it to the pack, so there is no problem with references in different packs
			PackEntry e = entry.clone();
			e.setPack(this);
			this.entries.add(e);
			return true;
		} else {
			return false;
		}
	}
	
	// returns whether the loaded document has a correct root or not
	public boolean hasRoot() {
		if (this.packDoc != null && this.packDoc.getDocumentElement().getTagName().equals(ROOT)) {
			return true;
		}
		return false;
	}
	
	// return a list of all nodes
	public NodeList getNodes() {
		if (this.packDoc != null) {
			return this.packDoc.getDocumentElement().getElementsByTagName(this.node);
		}
		return null;
	}
	
	public boolean isLoaded() {
		return isLoaded;
	}
	
	public static boolean createPackXML(File file) {
		if (file != null && !file.exists()) { // only create a file if it does not already exist
			try {
				Transformer trans = TransformerFactory.newInstance().newTransformer();
				trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no"); // write a XML declaration as header
				trans.setOutputProperty(OutputKeys.METHOD, "xml"); // write a XML document
				trans.setOutputProperty(OutputKeys.INDENT, "yes"); // allow auto whitespace
				trans.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name()); //use UTF-8 as encoding
				DocumentBuilder db;
				try {
					db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
					Document doc = db.newDocument();
					doc.appendChild(doc.createElement(ROOT)); // create a root node
					DOMSource source = new DOMSource(doc);
					StreamResult result;
					try {
						result = new StreamResult(new FileOutputStream(file));
						try {
							trans.transform(source, result);
							LoggingHandler.getLog().info("The pack XML " + file + " was created");
						} catch (TransformerException e) {
							LoggingHandler.getLog().log(Level.SEVERE, "Transformation failed; file " + file + " not saved", e);
							e.printStackTrace();
						}
					} catch (FileNotFoundException e1) {
						LoggingHandler.getLog().log(Level.WARNING, "The file " + file + "corresponding to this pack wasn't found", e1);
						e1.printStackTrace();
					}			
				} catch (ParserConfigurationException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			} catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
				LoggingHandler.getLog().log(Level.SEVERE, "Transformer cannot be created; file " + file + " not saved", e);
				e.printStackTrace();
			}
		}
		return false;
	}
	
	// converts a NodeList to an ArrayList of Elements for better functionality
	public static ArrayList<Element> getElementList(NodeList nodeList) {
		if (nodeList != null) {
			ArrayList<Element> elements = new ArrayList<Element>();
			for (int i = 0; i < nodeList.getLength(); i++) {
				elements.add((Element) nodeList.item(i));
			}
			return elements;
		}
		return null;
	}
	
	// converts a element list to an ArrayList of IDs
		public static ArrayList<String> getIDList(ArrayList<Element> elements) {
			if (elements != null) {
				ArrayList<String> iDs = new ArrayList<String>();
				for (Element element : elements) {
					NodeList idList = element.getElementsByTagName(SUB_ID); // get all IDs (should only be one) for this element
					if (idList != null && idList.getLength() > 0) {
							if (idList.getLength() > 1) {
								LoggingHandler.getLog().warning("Multiple IDs detected for subtance " + idList.item(0).getTextContent());
							}
							iDs.add(idList.item(0).getTextContent()); // take the first item
						} else {
							iDs.add(""); // add empty ID for troubleshooting
							LoggingHandler.getLog().severe("No ID detected for given subtance");
						}
				}
				return iDs;
			}
			return null;
		}
	
	public ArrayList<PackEntry> getEntries() {
		return entries;
	}
}
