package labj.functionality.housekeeping.configuration;

import java.awt.Color;


public class ConfigurationHandler {
// handles all configuration
	
	// constants
	public static final String VERSION = "0.0.6"; // the version number
//	private static final String INI_NAME = "LabJ";
	
	// fields
	private static int mainWindowWidth = 1000; // width of the main window
	private static int mainWindowHeigth = 700; // heigth of the main window
	private static int logWindowWidth = 600; // width of the main window
	private static int logWindowHeigth = 800; // heigth of the main window
	private static int numberLogFiles = 5; // the number of log files to be kept
	private static String logFileName = "Log_"; // name/prefix of logging files
	private static String logFolder = "Logs\\"; // name of the log folder
	private static Color logSevereColour = Color.RED; // display colour for severe logs
	private static Color logWarningColour = Color.ORANGE; // display colour for warning logs
	private static Color logExceptionColour = Color.RED; // display colour for exceptions in log warnings
//	private static String[] logFields = {"numberLogFiles", "logFileName", "logFolder"};
//	private static String[] logColourFields = {"logSevereColour", "logWarningColour", "logExceptionColour"};

	// getters
	public static int getMainWindowWidth() {
		return mainWindowWidth;
	}
	public static int getMainWindowHeigth() {
		return mainWindowHeigth;
	}
	public static int getNumberLogFiles() {
		return numberLogFiles;
	}
	public static String getLogFileName() {
		return logFileName;
	}
	public static String getLogFolder() {
		return logFolder;
	}
	public static int getLogWindowHeigth() {
		return logWindowHeigth;
	}
	public static int getLogWindowWidth() {
		return logWindowWidth;
	}
	public static Color getLogSevereColour() {
		return logSevereColour;
	}
	public static Color getLogWarningColour() {
		return logWarningColour;
	}
	public static Color getLogExceptionColour() {
		return logExceptionColour;
	}
	
	// setters
	public static void setMainWindowWidth(int mainWindowWidth) {
		ConfigurationHandler.mainWindowWidth = mainWindowWidth;
	}
	public static void setMainWindowHeigth(int mainWindowHeigth) {
		ConfigurationHandler.mainWindowHeigth = mainWindowHeigth;
	}
	public static void setNumberLogFiles(int numberLogFiles) {
		ConfigurationHandler.numberLogFiles = numberLogFiles;
	}
	public static void setLogFileName(String logFileName) {
		ConfigurationHandler.logFileName = logFileName;
	}
	public static void setLogFolder(String logFolder) {
		ConfigurationHandler.logFolder = logFolder;
	}
	public static void setLogWindowWidth(int logWindowWidth) {
		ConfigurationHandler.logWindowWidth = logWindowWidth;
	}
	public static void setLogWindowHeigth(int logWindowHeigth) {
		ConfigurationHandler.logWindowHeigth = logWindowHeigth;
	}
	public static void setLogSevereColour(Color logSevereColour) {
		ConfigurationHandler.logSevereColour = logSevereColour;
	}
	public static void setLogWarningColour(Color logWarningColour) {
		ConfigurationHandler.logWarningColour = logWarningColour;
	}
	public static void setLogExceptionColour(Color logExceptionColour) {
		ConfigurationHandler.logExceptionColour = logExceptionColour;
	}
}
