package functionality.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

import org.w3c.dom.Document;

import functionality.housekeeping.logging.LoggingHandler;

public abstract class Pack {
// abstract class representing a XML file containing information structured in a LabJ compatible way
	
	// fields
	private ArrayList<PackEntry> entries = new ArrayList<PackEntry>(); // a list of all entries in the pack
	private File packFile = null; // the XML storing this pack
	private Document packDoc = null; // the document representing the pack XML
	private boolean isLoaded = false; // is the corresponding file loaded?
	
	// mimic constants
	private static String root; // the XML root node
	private static String node; // the node containing all relevant information
	private static String[] subProperties; // a list of all properties of a node

	// constructor
	public Pack(File file) {
		if (file != null) {
			this.packFile = file;
			this.isLoaded = this.load();
		} else {
			LoggingHandler.getLog().warning("This pack has no corresponding file");
		}	
	}
	
	// abstract methods
	public abstract boolean load(); // load data from the XML file
	
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
}
