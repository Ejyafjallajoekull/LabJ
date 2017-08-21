package labj.gui.display;

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
		JTree fileTree = new JTree(packFolder);
		for (File file : getPackFolderFiles()) {
			packFolder.add(new DefaultMutableTreeNode(file));
		}
		JScrollPane scrollPane = new JScrollPane(fileTree);
		this.add(scrollPane);
		this.setVisible(true);
	}
	
	public ArrayList<File> getPackFolderFiles() {
		File packFolder = ConfigurationHandler.getPackFolder();
		ArrayList<File> files = new ArrayList<File>();
		if (packFolder != null) {
			if (packFolder.isDirectory()) { // will return false if not existent
				// create and autoclose recursive stream of xml files
				 try (Stream<Path> xmlPaths = Files.find(packFolder.toPath(), MAX_SEARCH_DEPTH_PACK_FOLDER, (filePath, fileAttr) -> isXMLFile(filePath))) {
					xmlPaths.forEach(path -> files.add(path.toFile()));
				 } catch (IOException e) {
					LoggingHandler.getLog().log(Level.SEVERE, "Could not get pack files in folder " + packFolder, e);
					e.printStackTrace();
				}
			} else { // can never be a file so no check needed
				packFolder.mkdirs(); // create folder if not existent
				LoggingHandler.getLog().info("Pack folder \"" + packFolder + "\" does not exist and will be created.");
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
	
	// helper function to get all XML pack files
	private static boolean isXMLFile(Path path) {
		if (path != null) {
			File file = path.toFile();
			if (file != null && file.isFile()) {
				return file.getName().toLowerCase().endsWith(".xml");
			}
		}
		return false;
	}


}
