package labjframework.utilities;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import labjframework.logging.LoggingHandler;

public class XMLUtilities {
	

	// returns the string representation of a node and all its children
	public static String asText(Node node) {
		if (node != null) {
			try {
				Transformer trans = TransformerFactory.newInstance().newTransformer();
				trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes"); // write no XML declaration as header
				trans.setOutputProperty(OutputKeys.METHOD, "xml"); // write plain text
				trans.setOutputProperty(OutputKeys.INDENT, "no"); // allow auto whitespace
				trans.setOutputProperty(OutputKeys.ENCODING, Configurations.xmlEncoding); //use UTF-8 as encoding
				DOMSource source = new DOMSource(node); // set the node as source
				StreamResult result = new StreamResult(new StringWriter()); // transform it to a stringwriter
				try {
					trans.transform(source, result);
					return result.getWriter().toString(); // read the writer
				} catch (TransformerException e) {
					LoggingHandler.getLog().log(Level.SEVERE, "Transformation of node " + node + " failed", e);
					e.printStackTrace();
				}
			} catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
				LoggingHandler.getLog().log(Level.SEVERE, "Transformer cannot be created for node " + node, e);
				e.printStackTrace();
			}
		}
		return ""; // return an empty String
	}
	
	public static String asText(Document doc) {
		if (doc != null) {
			try {
				Transformer trans = TransformerFactory.newInstance().newTransformer();
				trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes"); // write no XML declaration as header
				trans.setOutputProperty(OutputKeys.METHOD, "xml"); // write plain text
				trans.setOutputProperty(OutputKeys.INDENT, "no"); // allow auto whitespace
				trans.setOutputProperty(OutputKeys.ENCODING, Configurations.xmlEncoding); //use UTF-8 as encoding
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new StringWriter());
				try {
					trans.transform(source, result);
					return result.getWriter().toString();
				} catch (TransformerException e) {
					LoggingHandler.getLog().log(Level.SEVERE, "Transformation of document " + doc + " failed", e);
					e.printStackTrace();
				}
			} catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
				LoggingHandler.getLog().log(Level.SEVERE, "Transformer cannot be created for document " + doc, e);
				e.printStackTrace();
			}
		}
		return ""; // return an empty String
	}
	
	public static String asInnerText(Node node) {
		String output = asText(node);
		return output.substring(output.indexOf(">") + 1, output.lastIndexOf("<")); // get rid of the outer node
	}
	
	// returns weather or not the passed string contains XML formatted information
	public static boolean hasXMLContent(String text) {
		if (!text.isEmpty() && text.contains("</")) { // exclude everything that is clearly no XML info
			int[] starts = findAll(text, "<");
			int[] ends = findAll(text, ">");
			for (int i : starts) {
				for (int n : ends) {
					if (i < n) { // index of end point of the XML node must be higher than the one of the start point
						String sub = "</" + text.substring(i + 1, n + 1); // the string representing a node end
						if (text.contains(sub) && text.lastIndexOf(sub) > n) { // if contains end of the node, it's XML formatted info
							return true;
						}
					}
				}
			}	
		}
		return false;
	}
	
	public static String[] separateXMLContent(String text) {
		ArrayList<String> contentList = new ArrayList<String>();
		if (!text.isEmpty()) {
			String sub = text; // a variable to generate various substrings
			while (hasXMLContent(sub)) {
				int[] xmlContent = findXMLContent(sub); // return the starting and ending index of the first bit of XML content
				if (xmlContent[0] > 0) {
					contentList.add(sub.substring(0, xmlContent[0])); // add everything before the XML content as plain text
				}
				contentList.add(sub.substring(xmlContent[0], xmlContent[1] + 1)); // add the XML content
				if (sub.length() > xmlContent[1] + 1) {
					sub = sub.substring(xmlContent[1] + 1);
				} else {
					sub = ""; // if the XML content end is the end of the string, set the substring to be empty
				}
			}
			if (!sub.isEmpty()) { // if there is no more XML content just output the rest
				contentList.add(sub);
			}
		}
		return contentList.toArray(new String[contentList.size()]);
	}
	
	// returns a 1d int array of start and end point of XML content
	public static int[] findXMLContent(String text) {
		int[] content = new int[2]; // start and end point of the XML content 
		if (!text.isEmpty() && hasXMLContent(text)) { // exclude everything that is clearly no XML info
			int[] starts = findAll(text, "<");
//			System.out.println("Starts: " + Arrays.toString(starts));
			int[] ends = findAll(text, ">");
//			System.out.println("Ends: " + Arrays.toString(ends));
			for (int i : starts) {
				for (int n : ends) {
					if (i < n) { // index of end point of the XML node must be higher than the one of the start point
						String sub = "</" + text.substring(i + 1, n + 1); // the string in between lesser and greater sign
						if (text.contains(sub)  && text.lastIndexOf(sub) > n) { // if contains end of the node, it's XML formatted info
							content[0] = i; // set start point
							String transformedText = text.substring(n + 1); // substring starting after the first XML declaration
							content[1] = transformedText.indexOf(sub) + sub.length() + n;
//							System.out.println("Start to end: " + content[0] + "/" + content[1]);
							return content;
						}
					}
				}
			}	
		}
		return content;
	}
	
	// finds all indices of the given query in the specified search string
	private static int[] findAll(String text, String query) {
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		if (!text.isEmpty() && !query.isEmpty()) {
			int offset = 0;  // offset of the original string compared to the substring
			String sub = text;
			while (sub.indexOf(query) >= 0) {
//				System.out.println("Sub: " + sub);
//				System.out.println("Query: " + query + " : " + sub.indexOf(query));
//				System.out.println("Offset: " + offset);
				indexList.add(sub.indexOf(query) + offset);
				if (sub.length() > query.length()) { // ensure correct function even when last char sequence is just the querry
					offset += sub.indexOf(query) + query.length();
					sub = sub.substring(sub.indexOf(query) + query.length()); 
				} else {
					break;
				}
			}			
		}
		int[] indices = new int[indexList.size()];
		for (int i = 0; i < indexList.size(); i++) {
			indices[i] = indexList.get(i);
		}
		return indices;
	}
}
