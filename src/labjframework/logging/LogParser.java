package labjframework.logging;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.logging.Level;


public class LogParser {
// parses log files
	
	// constants
	public static final String LOG = "log"; // defining a log
	public static final String RECORD = "record"; // defining a record
	public static final String DATE = "date"; // date record in log file
	public static final String MILLIS = "millis"; // milli seconds in log file
	public static final String SEQUENCE = "sequence"; // sequence in log file
	public static final String LOGGER = "logger"; // logger in log file
	public static final String LEVEL = "level"; // level in log file
	public static final String CLASS = "class"; // class in log file
	public static final String METHOD = "method"; // method in log file
	public static final String THREAD = "thread"; // thread in log file
	public static final String MESSAGE = "message"; // message in log file
	public static final String EXCEPTION = "exception"; // exception in log file
	public static final String FRAME = "frame"; // frame of an exception in log file
	private static final String[] TEMPLATE = {DATE, MILLIS, SEQUENCE, LOGGER, LEVEL, CLASS, METHOD, THREAD, MESSAGE, EXCEPTION}; // all template strings for detection 
	private static final String[] TEMPLATE_EXC = {CLASS, METHOD}; // all template strings for detection in exceptions

	
	// methods
	
	public static String[] parseLogOld(File file) { // rudimentary
		ArrayList<String> fileContent = new ArrayList<String>();
		ArrayList<String> parsedContent = new ArrayList<String>();
		try {
			fileContent = (ArrayList<String>) Files.readAllLines(file.toPath());
			String[] info = new String[TEMPLATE.length];
			for (String item : fileContent) {
				item.trim(); // remove leading 
				if (item.equals("<" + LOG + ">")) { // start writing a record
					parsedContent.add("Logging started:"); // initialisation message
				} else if (item.equals("</" + RECORD + ">")) { // stop writing a record
					parsedContent.add(info[0] + "(" + info[1] + ") Thread " + info[7] + " / Sequence " + info[2] + " " + info[3] + " " + info[5] + "." + info[6]);
					parsedContent.add(info[4] + ": " + info[8]);
				} else { // write record items
					for (int i = 0; i < TEMPLATE.length; i++) {
						if (item.trim().startsWith("<" + TEMPLATE[i] + ">") && item.trim().endsWith("</" + TEMPLATE[i] + ">")) {
							info[i] = item.trim().substring(("<" + TEMPLATE[i] + ">").length(), item.trim().length() - ("</" + TEMPLATE[i] + ">").length());
							break;
						}
					}
				}
			}
		} catch (IOException e) {
			LoggingHandler.getLog().log(Level.WARNING, "Log file " + file + " could not be parsed.", e);
			e.printStackTrace();
		}
		return parsedContent.toArray(new String[parsedContent.size()]);
	}
	
	public static String[][] parseLog(File file) {
		ArrayList<String[]> parsedContent = new ArrayList<String[]>();
		try {
			String fileContent = new String(Files.readAllBytes(file.toPath()));
			ArrayList<String> logList = new ArrayList<String>(); // list of all logs in file
			String[] info = new String[TEMPLATE.length];
			while (fileContent.indexOf(toStartTag(LOG)) >= 0 && fileContent.indexOf(toEndTag(LOG)) >= 0) { // search for start and end tag of a log
				logList.add(fileContent.substring(fileContent.indexOf(toStartTag(LOG)) + toStartTag(LOG).length(), fileContent.indexOf(toEndTag(LOG)))); // add everything in between the tags
				String logString = fileContent.substring(fileContent.indexOf(toStartTag(LOG)) + toStartTag(LOG).length(), fileContent.indexOf(toEndTag(LOG))); // add everything in between the tags
				parsedContent.add(new String[]{"Logger initialised:" + System.getProperty("line.separator"), LOG});
				while (logString.indexOf(toStartTag(RECORD)) >= 0 && logString.indexOf(toEndTag(RECORD)) >= 0) { // search for start and end tag of a record
					String recordString = logString.substring(logString.indexOf(toStartTag(RECORD)) + toStartTag(RECORD).length(), logString.indexOf(toEndTag(RECORD))); // add everything in between the tags
					for (int i = 0; i < TEMPLATE.length; i++) { // parse the records
						if (recordString.indexOf(toStartTag(TEMPLATE[i])) >= 0 && recordString.indexOf(toEndTag(TEMPLATE[i])) >= 0) { // search for start and end tag
				//			System.out.println(recordString.indexOf(toStartTag(template[i])) + " ; " + toStartTag(template[i]).length() + " ; " + recordString.indexOf(toEndTag(template[i])));
							info[i] = recordString.substring(recordString.indexOf(toStartTag(TEMPLATE[i])) + toStartTag(TEMPLATE[i]).length(), recordString.indexOf(toEndTag(TEMPLATE[i])));
						} else { // fill null if tag is not found
							info[i] = null;
						}
					}
					parsedContent.add(new String[]{info[0] + "(" + info[1] + ") Thread " + info[7] + " / Sequence " + info[2] + " " + info[3] + " " + info[5] + "." + info[6] + System.getProperty("line.separator"), info[4]});
					parsedContent.add(new String[]{info[4] + ": " + info[8] + System.getProperty("line.separator"), info[4]}); // add content to be returned
					if (info[9] != null) { // only add if there actually is an exception
						parsedContent.add(new String[]{parseException(info[9]), EXCEPTION}); // add content to be returned
					}
					logString = logString.replaceFirst(toStartTag(RECORD), ""); // remove parsed starting tag for next search
					logString = logString.replaceFirst(toEndTag(RECORD), ""); //  remove parsed ending tag for next search
				}
				parsedContent.add(new String[]{"Logger stopped" + System.getProperty("line.separator"), LOG});
				fileContent = fileContent.replaceFirst(toStartTag(LOG), ""); // remove parsed starting tag for next search
				fileContent = fileContent.replaceFirst(toEndTag(LOG), ""); //  remove parsed ending tag for next search
			}
		} catch (IOException e) {
			LoggingHandler.getLog().log(Level.WARNING, "Log file " + file + " could not be parsed.", e);
			e.printStackTrace();
		}
		return parsedContent.toArray(new String[parsedContent.size()][]);
	}
	
	public static String fuseStrings(String[] strings) {
		String fusedString = new String();
		for (String item : strings) {
			fusedString = fusedString + item + System.getProperty("line.separator");
		}
		return fusedString;
	}
	
	// private methods
	private static String toStartTag(String string) {
		if (string != null) {
			return ("<" + string + ">");
		} else {
			LoggingHandler.getLog().info("Null was passed, but a String was expected.");
			return string;
		}
	}
	
	private static String toEndTag(String string) {
		if (string != null) {
			return ("</" + string + ">");
		} else {
			LoggingHandler.getLog().info("Null was passed, but a String was expected.");
			return string;
		}
	}
	
	private static String parseException(String string) {
		if (string != null) {
			ArrayList<String> parsedExc = new ArrayList<String>();
			String[] info = new String[TEMPLATE_EXC.length];
			String excString = new String(string); // perform operations on a different object
			if (excString.indexOf(toStartTag(MESSAGE)) >= 0 && excString.indexOf(toEndTag(MESSAGE)) >= 0) {
				parsedExc.add("    " + excString.substring(excString.indexOf(toStartTag(MESSAGE)) + toStartTag(MESSAGE).length(), excString.indexOf(toEndTag(MESSAGE))));
			}
			while (excString.indexOf(toStartTag(FRAME)) >= 0 && excString.indexOf(toEndTag(FRAME)) >= 0) {
				String frameString = excString.substring(excString.indexOf(toStartTag(FRAME)) + toStartTag(FRAME).length(), excString.indexOf(toEndTag(FRAME))); // add everything in between the tags
				for (int i = 0; i < TEMPLATE_EXC.length; i++) { // parse the records
					if (frameString.indexOf(toStartTag(TEMPLATE_EXC[i])) >= 0 && frameString.indexOf(toEndTag(TEMPLATE_EXC[i])) >= 0) { // search for start and end tag
			//			System.out.println(recordString.indexOf(toStartTag(template[i])) + " ; " + toStartTag(template[i]).length() + " ; " + recordString.indexOf(toEndTag(template[i])));
						info[i] = frameString.substring(frameString.indexOf(toStartTag(TEMPLATE_EXC[i])) + toStartTag(TEMPLATE_EXC[i]).length(), frameString.indexOf(toEndTag(TEMPLATE_EXC[i])));
					} else {
						info[i] = null;
					}
				}
				parsedExc.add("        " + info[0] + "." + info[1]);
				excString = excString.replaceFirst(toStartTag(FRAME), ""); // remove parsed starting tag for next search
				excString = excString.replaceFirst(toEndTag(FRAME), ""); //  remove parsed ending tag for next search
			}
			return fuseStrings(parsedExc.toArray(new String[parsedExc.size()]));
		} else {
			LoggingHandler.getLog().info("Null was passed, but a String was expected.");
			return string;
		}
	}
}
