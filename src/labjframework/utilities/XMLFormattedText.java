package labjframework.utilities;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import labjframework.logging.LoggingHandler;

public class XMLFormattedText {
	
	// constants
	public static final String FORMATTED_TEXT = "FormattedText"; // the node indicating XML formatted text
	
	// fields
	private Node formattedText = null; // the XML node this piece of text is based on
	
	// constructors
	public XMLFormattedText(String text) {
		if (text != null && !text.isEmpty()) {
			try {
				DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				try { // parse the string to a node
					Document doc = db.parse(new InputSource(new ByteArrayInputStream(text.getBytes(Configurations.xmlEncoding))));
					Node docNode = doc.getDocumentElement(); // root node of the string
					if (docNode.getNodeName().equals(FORMATTED_TEXT)) { // only accept formatted text as a root node
						this.formattedText = docNode;
					} else { // otherwise create a formatted text node to wrap this node
						doc = db.newDocument();
						Node root = doc.createElement(FORMATTED_TEXT);
						root.appendChild(docNode);
						doc.appendChild(root);
					}
				} catch (SAXException | IOException e) {
					if (!text.startsWith("<" + FORMATTED_TEXT + ">") && !text.startsWith("</" + FORMATTED_TEXT + ">")) {
						text = "<" + FORMATTED_TEXT + ">" + text;
					}
					if (!text.endsWith("</" + FORMATTED_TEXT + ">")) {
						text += "</" + FORMATTED_TEXT + ">";
					}
					try {
						Document doc = db.parse(new InputSource(new ByteArrayInputStream(text.getBytes(Configurations.xmlEncoding))));
						this.formattedText = doc.getDocumentElement();
					} catch (SAXException | IOException e1) {
						LoggingHandler.getLog().log(Level.SEVERE, "DocumentBuilder could not parse \"" + text + "\"", e1);
						e1.printStackTrace();
					}
					//e.printStackTrace();
				}
			} catch (ParserConfigurationException e) {
				LoggingHandler.getLog().log(Level.SEVERE, "Could not initialise a DocumentBuilder", e);
				e.printStackTrace();
			}
		}
	}
	
	public XMLFormattedText(Node node) {
		if (node != null) {
			if (node.getNodeName().equals(FORMATTED_TEXT)) { // is this node really XML formatted text?
				this.formattedText = node;
			} else { // search for formatted text child nodes
				NodeList nl = ((Element) node).getElementsByTagName(FORMATTED_TEXT);
				if (nl.getLength() > 0) {
					this.formattedText = nl.item(0);
				} else { // if no formatted text is present, create a formatted text parent node
					try {
						Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
						Node ft = doc.appendChild(doc.createElement(FORMATTED_TEXT)); // create a new document with formatted text as root
						ft.appendChild(doc.adoptNode(node));
						this.formattedText = ft;
					} catch (ParserConfigurationException e) {
						LoggingHandler.getLog().log(Level.SEVERE, "Could not initialise a DocumentBuilder", e);
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	@Override
	public String toString() {
		if (this.formattedText != null) {
			return XMLUtilities.asText(this.formattedText);
		}
		return null;
	}
	
	public Node toNode() {
		return this.formattedText;
	}

}
