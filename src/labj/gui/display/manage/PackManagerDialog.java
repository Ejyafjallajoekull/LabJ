package labj.gui.display.manage;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.stream.Stream;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import labj.functionality.housekeeping.configuration.ConfigurationHandler;
import labjframework.logging.LoggingHandler;
import labjframework.utilities.XMLUtilities;

public class PackManagerDialog extends JDialog {

	// serialisation
	private static final long serialVersionUID = 1L;
	
	// constants
	public static final String TITLE = "Pack Manager";
	private static final int MAX_SEARCH_DEPTH_PACK_FOLDER = Integer.MAX_VALUE; // the maximum search depth for pack XMLs in the pack folder

	public PackManagerDialog() {
		// TODO Auto-generated constructor stub
	}

	public PackManagerDialog(Frame arg0) {
		super(arg0);
		this.setTitle(TITLE);
		this.setSize(600, 700);
		this.setLocationRelativeTo(null);
		DefaultMutableTreeNode packFolder = new DefaultMutableTreeNode(ConfigurationHandler.getPackFolder());
		JTree fileTree = new PackTree(packFolder);
		for (File file : getPackFolderFiles()) {
			packFolder.add(new DefaultMutableTreeNode(file));
		}
		fileTree.expandRow(0); // initially expand first row to show all available packs
		JScrollPane scrollPane = new JScrollPane(fileTree);
		this.add(scrollPane);
		this.setVisible(true);
	}
	
	public static ArrayList<File> getPackFolderFiles() {
		return getPackFiles(ConfigurationHandler.getPackFolder(), false);
	}
	
	public static ArrayList<File> getPackFiles(File folder, boolean recursive) {
		ArrayList<File> files = new ArrayList<File>();
		if (folder != null) {
			if (folder.isDirectory()) { // will return false if not existent
				// create and autoclose recursive stream of xml files
				if (recursive) {
					try (Stream<Path> xmlPaths = Files.find(folder.toPath(), MAX_SEARCH_DEPTH_PACK_FOLDER, (filePath, fileAttr) -> XMLUtilities.isXMLFile(filePath))) {
						xmlPaths.forEach(path -> files.add(path.toFile()));
					} catch (IOException e) {
						LoggingHandler.getLog().log(Level.SEVERE, "Could not get pack files in folder " + folder, e);
						e.printStackTrace();
					}
				} else { // if non recursive search is required just list all xml files and add them to the list
					for (File file : folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"))) {
						if (file != null && file.isFile() && !files.contains(file)) {
							files.add(file);
						}
					}
					
				}
			} else { // can never be a file so no check needed
				folder.mkdirs(); // create folder if not existent
				LoggingHandler.getLog().info("Pack folder \"" + folder + "\" does not exist and will be created.");
			}
		}
		return files;
	}

	public PackManagerDialog(Dialog owner) {
		super(owner);
		// TODO Auto-generated constructor stub
	}

	public PackManagerDialog(Window owner) {
		super(owner);
		// TODO Auto-generated constructor stub
	}


}
