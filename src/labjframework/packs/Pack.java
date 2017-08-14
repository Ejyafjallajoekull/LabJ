package labjframework.packs;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import labjframework.logging.LoggingHandler;
import labjframework.utilities.Configurations;
import labjframework.utilities.XMLFormattedText;
import labjframework.utilities.XMLUtilities;

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
	protected static final String SUB_NAME = "TrivialName"; // a node representing a name of the substance
	protected static final String SUB_DISPLAY ="DisplayName"; // a node representing the name index of the displayed substance name

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
	public abstract void updateCustomProperties(Node parentNode, PackEntry entry); // just updates the custom properties // handle regular properties in a predefined function
	
	
	// methods
	// save this pack to XML
	public void save() {
		if (this.packFile != null && this.packDoc != null && this.isLoaded()) {
			try {
				Transformer trans = TransformerFactory.newInstance().newTransformer();
				trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no"); // write a XML declaration as header
				trans.setOutputProperty(OutputKeys.METHOD, "xml"); // write a XML document
				trans.setOutputProperty(OutputKeys.INDENT, Configurations.xmlIndent); // allow auto whitespace
				trans.setOutputProperty(OutputKeys.ENCODING, Configurations.xmlEncoding); //use UTF-8 as encoding
				this.updateDocument();
				DOMSource source = new DOMSource(this.packDoc);
				StreamResult result;
				try {
					result = new StreamResult(new FileOutputStream(this.packFile));
					try {
						trans.transform(source, result);
					//	System.out.println("After: " + XMLUtilities.asText(packDoc));
						LoggingHandler.getLog().fine("The document " + this.packDoc + " has been successfully transformed");
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
	
	// is the given Pack equal to this one?
	public boolean isEqual(Pack eqPack) {
		if (eqPack.getClass() == this.getClass()) { // only do this if packs of a similar type are compared
			return eqPack.getPackFile().equals(this.packDoc);
		}
		return false;
	}
	
	
	// static methods
	
	public File getPackFile() {
		return packFile;
	}

	public static boolean createPackXML(File file) {
		if (file != null && !file.exists()) { // only create a file if it does not already exist
			try {
				Transformer trans = TransformerFactory.newInstance().newTransformer();
				trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no"); // write a XML declaration as header
				trans.setOutputProperty(OutputKeys.METHOD, "xml"); // write a XML document
				trans.setOutputProperty(OutputKeys.INDENT, Configurations.xmlIndent); // allow auto whitespace
				trans.setOutputProperty(OutputKeys.ENCODING, Configurations.xmlEncoding); //use UTF-8 as encoding
				DocumentBuilder db;
				try {
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					dbf.setIgnoringElementContentWhitespace(true);
					db = dbf.newDocumentBuilder();
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
					LoggingHandler.getLog().log(Level.SEVERE, "Could not initialise a DocumentBuilder", e2);
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
	
	// get the entry with the specified ID
	public PackEntry getByID(String id) {
		if (id != null && !id.isEmpty()) {
			for (PackEntry entry : this.entries) {
				if (entry.getId().equals(id)) {
					return entry;
				}
			}
		}
		return null;
	}

	// helper function to shorten code
	protected void addProperty(Node parentNode, String propertyName, String data) {
		if (this.packDoc != null && parentNode != null) {
			Node prop = this.packDoc.createElement(propertyName); // create a new property node with the specified name
			parentNode.appendChild(prop); // append it to the parent node
	//		if (!XMLUtilities.hasXMLContent(data)) { // if the string does not contain XML data, handle it as plain text
			prop.appendChild(this.packDoc.createTextNode(data)); // add the data as text to the new property node
	/*		} else {
				String[] sepContent = XMLUtilities.separateXMLContent(data); // separate XML content and text
				for (String content : sepContent) {
					if (!XMLUtilities.hasXMLContent(content)) {
						prop.appendChild(this.packDoc.createTextNode(content)); // if no XML content, add the data as text to the new property node
					} else {						
						try {
							DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
							try { // otherwise add it as node to preserve its XML identity (omit replacement of greater & lesser signs, which happens in text node creation)
								Document insert = db.parse(new InputSource(new ByteArrayInputStream(content.getBytes(Configurations.xmlEncoding))));
								prop.appendChild(this.packDoc.adoptNode(insert.getDocumentElement())); // add the data as text to the new property node
							} catch (SAXException | IOException e) { // loading failed
								LoggingHandler.getLog().log(Level.SEVERE, "Could not create an XML content insert from \"" + content + "\"", e);
								e.printStackTrace();
							}
						} catch (ParserConfigurationException e) {
							LoggingHandler.getLog().log(Level.SEVERE, "Could not initialise a DocumentBuilder", e);
							e.printStackTrace();
						}
					}
				}
			}*/
		}
	}
	
	// helper function to shorten code
	protected void addProperty(Node parentNode, String propertyName, String[] data) {
		if (this.packDoc != null && parentNode != null) {
			Node prop = null;
		//	try {
		//		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				for (String s : data) {
					prop = this.packDoc.createElement(propertyName); // create a new property node with the specified name
					parentNode.appendChild(prop); // append it to the parent node
			//		if (!XMLUtilities.hasXMLContent(s)) { // if the string does not contain XML data, handle it as plain text
					prop.appendChild(this.packDoc.createTextNode(s)); // add the data as text to the new property node
			/*		} else {
						String[] sepContent = XMLUtilities.separateXMLContent(s); // separate XML content and text
						for (String content : sepContent) {
							if (!XMLUtilities.hasXMLContent(content)) {
								prop.appendChild(this.packDoc.createTextNode(content)); // if no XML content, add the data as text to the new property node
							} else {
								try { // otherwise add it as node to preserve its XML identity (omit replacement of greater & lesser signs, which happens in text node creation)
									Document insert = db.parse(new InputSource(new ByteArrayInputStream(content.getBytes(Configurations.xmlEncoding))));
									prop.appendChild(this.packDoc.adoptNode(insert.getDocumentElement())); // add the data as text to the new property node
								} catch (SAXException | IOException e) { // loading failed
									LoggingHandler.getLog().log(Level.SEVERE, "Could not create an XML content insert from \"" + content + "\"", e);
									e.printStackTrace();
								}
							}
						}
					} 
				}
			} catch (ParserConfigurationException e) {
				LoggingHandler.getLog().log(Level.SEVERE, "Could not initialise a DocumentBuilder", e);
				e.printStackTrace(); */
			}
		}
	}
	
	// helper function to shorten code // same as addProperty(String[])
	protected void addProperty(Node parentNode, String propertyName, ArrayList<String> data) {
		if (this.packDoc != null && parentNode != null) {
			Node prop = null;
			for (String s : data) {
				prop = this.packDoc.createElement(propertyName); // create a new property node with the specified name
				parentNode.appendChild(prop); // append it to the parent node
				prop.appendChild(this.packDoc.createTextNode(s)); // add the data as text to the new property node
			}
		}
	}
	
	// helper function to shorten code
	protected void addXMLProperty(Node parentNode, String propertyName, ArrayList<XMLFormattedText> data) {
		if (this.packDoc != null && parentNode != null) {
			Node prop = null;
			for (XMLFormattedText s : data) {
				prop = this.packDoc.createElement(propertyName); // create a new property node with the specified name
				parentNode.appendChild(prop); // append it to the parent node
				prop.appendChild(this.packDoc.adoptNode(s.toNode())); // add the data as text to the new property node
			}
		}
	}
	
	// helper function to update the basic components of pack, so all variables present in an entries abstract form
	protected void updateBasics(Node parentNode, PackEntry entry) {
		if (this.packDoc != null && parentNode != null && entry != null) {
			// ID
			this.addProperty(parentNode, SUB_ID, entry.getId());
			// comments	
			this.addXMLProperty(parentNode, SUB_COMMENT, entry.getComments());
			// attachments
			this.addXMLProperty(parentNode, SUB_ATTACHMENT, entry.getAttachments());
			// trivial names
			this.addProperty(parentNode, SUB_NAME, entry.getTrivialNames());
			// display name
			this.addProperty(parentNode, SUB_DISPLAY, entry.getDisplayName());
		}
	}
	
	public void updateDocument() { // update the document to reflect all changes made
		if (this.packDoc != null) {
			PackEntry entry = null;
			if (this.packDoc.getDocumentElement() == null) {
				this.packDoc.appendChild(this.packDoc.createElement(ROOT));
			}
			NodeList nodes = this.getNodes();
			ArrayList<Element> elements = Pack.getElementList(nodes); // get all substance nodes and convert them to elements for extended functionality
			ArrayList<String> iDs = Pack.getIDList(elements); // get all corresponding IDs
			for (int i = 0; i < this.entries.size(); i++) {
				entry = this.entries.get(i); 
				Node sub = this.packDoc.createElement(this.node); // the entry node
				// basic properties
				this.updateBasics(sub, entry);
				// implement unique update behaviour for different packs
				this.updateCustomProperties(sub, entry);
				if (iDs.contains(entry.getId())) { // update substance if already existent
					this.packDoc.getDocumentElement().replaceChild(sub, nodes.item(iDs.indexOf(entry.getId())));
				} else { // add new substance if not present in the document
					this.packDoc.getDocumentElement().appendChild(sub);
				}		
			}
		}
	}
		
	public ArrayList<PackEntry> getEntries() {
		return entries;
	}
}
