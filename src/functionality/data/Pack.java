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
	protected String root; // the XML root node
	protected String node; // the node containing all relevant information
	protected String[] subProperties; // a list of all properties of a node
	protected static final String SUB_ID = "ID"; // the pack unique identifier of a entry
	protected static final String SUB_COMMENT = "Comment"; // the comment node of a entry

	// constructor
	public Pack(File file) {
		if (file != null) {
			this.packFile = file;
			// load the XML to the memory
			if (this.load()) {
				this.isLoaded = this.loadPack();
			}
		} else {
			LoggingHandler.getLog().warning("This pack has no corresponding file");
		}	
	}
	
	// abstract methods
	public abstract boolean loadPack(); // load data from the XML file
	
	// methods
	// save this pack to XML
	public void save() {
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
				LoggingHandler.getLog().warning("The file " + this.packFile + "corresponding to this pack wasn't found.");
				e1.printStackTrace();
			}			
		} catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
					return true; // loading the XML worked
				} catch (SAXException | IOException e) {
					LoggingHandler.getLog().log(Level.SEVERE, "Could not load " + this.packFile, e);
					e.printStackTrace();
				}
			} else {
				this.packDoc = db.newDocument();
				return true; // at least a document was created
			}
		} catch (ParserConfigurationException e) {
			LoggingHandler.getLog().log(Level.SEVERE, "Could not initialise a DocumentBuilder", e);
			e.printStackTrace();
		}
		return false;
	}
	
	// methods
	// add a unique substance
	public boolean addEntry(PackEntry entry) {
		if (entry != null && !entries.contains(entry)) {
			for (PackEntry item : entries) {
				if (item.getId().equals(entry.getId())) {
					return false; // only allow substances with different Id
				}
			}
			// clone object and add it to the pack, so there is no problem with references in different packs
			PackEntry e = entry.clone();
			e.setPack(this);
			entries.add(e);
			return true;
		} else {
			return false;
		}
	}
}
