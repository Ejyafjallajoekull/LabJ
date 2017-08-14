package labj.main;

import java.awt.event.WindowEvent;
import java.io.File;
import java.math.BigDecimal;
import java.util.logging.Level;

import javax.swing.JFrame;

import labj.functionality.packs.LabJPackHandler;
import labj.functionality.packs.Substance;
import labj.functionality.packs.SubstancePack;
import labj.gui.display.LabJMainFrame;
import labjframework.logging.LoggingHandler;
import labjframework.packs.PackHandler;
import labjframework.utilities.XMLFormattedText;

public class LabJStartingClass {
// starting class
	
	// fields
	private static JFrame mainWindow = null; // the main window
	
	public static void main(String[] args) {
		test();
	//	startGUI();
	}
	
	// initiates the GUI
	public static void startGUI() {
		if (mainWindow == null) {
			mainWindow = new LabJMainFrame();
			LoggingHandler.getLog().info("GUI started.");
//			ConfigurationHandler.readINI();
		} else {
			LoggingHandler.getLog().warning("Only one instance of LabJ can be started at the same time.");
		}
	}
	
	// closes the GUI
	public static void closeGUI() {
		if (mainWindow != null) {
			mainWindow.dispatchEvent(new WindowEvent(mainWindow, WindowEvent.WINDOW_CLOSING)); // close the main window
			mainWindow = null;
			LoggingHandler.getLog().info("GUI closed.");
		}
	}
	
	private static void test() {
		LoggingHandler.getLog().setLevel(Level.ALL);
		LoggingHandler.startLogWriting();
		PackHandler ph = new LabJPackHandler();
		ph.loadPack(new File("sp_test_01.xml"));
//		SubstancePack sp = new SubstancePack(new File("sp_test_01.xml"));
		SubstancePack sp = (SubstancePack) ph.getLoadedPacks(SubstancePack.class).get(0);
//		sp.addEntry(new Substance("oewijfjojdl", new String[]{"Hydrogen"}, new BigDecimal("0.5"), new BigDecimal("0.245"), sp));
//		sp.addEntry(new Substance("oewijajojdl", new String[]{"Oxygen"}, new BigDecimal("0.2"), new BigDecimal("0.245"), sp));
//		sp.getEntries().get(0).getComments().add(new XMLFormattedText("Hydrogen is cool."));
//		sp.getEntries().get(0).getComments().add("Oxygen is much <Reference><ReferenceA>coo\nler</ReferenceA><ReferenceB>lala</ReferenceB></Reference>!");
//		sp.getEntries().get(0).getComments().add("Oxygen is \n a liar.");
//		sp.getEntries().get(0).getComments().add("Hydrogen is a gas.");
//		System.out.println(sp.getEntries().get(0).getComments().get(0));
		sp.save();
	}


}
