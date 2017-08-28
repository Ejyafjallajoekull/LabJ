package labj.main;

import java.awt.event.WindowEvent;
import java.io.File;
import java.util.logging.Level;

import javax.swing.JFrame;

import labj.functionality.packs.LabJPackHandler;
import labj.functionality.packs.SubstancePack;
import labj.functionality.packs.Taxonomy;
import labj.functionality.packs.TaxonomyPack;
import labj.gui.display.LabJMainFrame;
import labjframework.logging.LoggingHandler;
import labjframework.packs.PackHandler;

public class LabJStartingClass {
// starting class
	
	// fields
	private static JFrame mainWindow = null; // the main window
	
	public static void main(String[] args) {
//		test();
		startGUI();
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
		sp.setHandler(ph); // handler necessary for references to work
		TaxonomyPack tp = (TaxonomyPack) ph.getLoadedPacks(TaxonomyPack.class).get(0);
//		System.out.println(tp.getEntries().get(0));
//		for (Taxonomy child : ((Taxonomy) tp.getEntries().get(0)).getChildTaxa(ph)) {
//			System.out.println(child);
//		}
//		tp.addEntry(new Taxonomy("life", new String[]{"Life"}, (Taxonomy) null, tp));
//		for (int i = 0; i < 10000; i++) {
//			tp.addEntry(new Taxonomy(String.valueOf(i), new String[]{"Procaryotes"}, (Taxonomy) tp.getEntries().get(0), tp));
//
//		}
		tp.addEntry(new Taxonomy("life", new String[]{"Life"},(Taxonomy) null, tp));
		tp.addEntry(new Taxonomy("procaryotes", new String[]{"Procaryotes"}, (Taxonomy) tp.getEntries().get(0), tp));
		tp.addEntry(new Taxonomy("viruses", new String[]{"Viruses"}, (Taxonomy) tp.getEntries().get(0), tp));
		tp.addEntry(new Taxonomy("escherichia", new String[]{"Escherichia"}, (Taxonomy) tp.getEntries().get(1), tp));
//		XMLFormattedText com = sp.getEntries().get(0).getComments().get(0);
//		NodeList nl = ((Element) com.toNode()).getElementsByTagName(PackReference.REFERENCE);
//		PackReference ref = new PackReference(nl.item(0));
//		System.out.println(ref.findReference(ph));
//		sp.addEntry(new Substance("oewijfjojdl", new String[]{"Hydrogen"}, new BigDecimal("0.5"), new BigDecimal("0.245"), sp));
//		sp.addEntry(new Substance("oewijajojdl", new String[]{"Oxygen"}, new BigDecimal("0.2"), new BigDecimal("0.245"), sp));
//		sp.getEntries().get(0).getComments().add(new XMLFormattedText("Hydrogen is cool."));
//		XMLFormattedText cool = new XMLFormattedText("cooler");
//		PackReference ref = new PackReference(sp.getEntries().get(0), cool);
//		XMLFormattedText com = new XMLFormattedText();
//		System.out.println();
//		com.toNode().appendChild(com.toNode().getOwnerDocument().adoptNode(ref.toNode()));
//		sp.getEntries().get(0).getComments().add(com);
//		sp.getEntries().get(0).getComments().add("Oxygen is \n a liar.");
//		sp.getEntries().get(0).getComments().add("Hydrogen is a gas.");
//		System.out.println(sp.getEntries().get(0).getComments().get(0).toNode().getTextContent());
		sp.save();
		tp.save();
	}


}
