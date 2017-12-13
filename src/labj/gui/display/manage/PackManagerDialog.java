package labj.gui.display.manage;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import labj.functionality.housekeeping.configuration.ConfigurationHandler;
import labj.functionality.project.LabJProject;
import labjframework.logging.LoggingHandler;
import labjframework.packs.Pack;
import labjframework.utilities.XMLUtilities;

public class PackManagerDialog extends JDialog{

	// serialisation
	private static final long serialVersionUID = 1L;
	
	// constants
	public static final String TITLE = "Pack Manager";
	private static final int MAX_SEARCH_DEPTH_PACK_FOLDER = Integer.MAX_VALUE; // the maximum search depth for pack XMLs in the pack folder

	private static final String BUTTON_CANCEL = "Cancel";
	private static final String BUTTON_SAVE = "Save";
	
	private static final String DETAILS_TITLE = "Details";
	private static final String DETAILS_ENTRIES = "Entries";
	private static final String DETAILS_DEPENDENCIES = "Dependencies";
	private static final String DETAILS_PACKS = "Packs";
	private static final String DETAILS_NAME = "Name";
	private static final String DETAILS_NULL_VALUE = "-----"; // String displayed if nothing is selected
	
	// fields
	private JLabel detailsNameValue = new JLabel(DETAILS_NULL_VALUE);
	private JLabel detailsPacksValue = new JLabel(DETAILS_NULL_VALUE);
	private JLabel detailsEntriesValue = new JLabel(DETAILS_NULL_VALUE);
	private JLabel detailsDependenciesValue = new JLabel(DETAILS_NULL_VALUE);
	private DefaultMutableTreeNode[] selectedNodes = null;
	
	// constructors
	public PackManagerDialog() {
		this.init();
	}

	public PackManagerDialog(Frame arg0) {
		super(arg0);
		this.init();
	}
	
	public PackManagerDialog(Dialog owner) {
		super(owner);
		this.init();
	}

	public PackManagerDialog(Window owner) {
		super(owner);
		this.init();
	}
	
	// initialises the class // called in every constructor
	private void init() {
		this.setTitle(TITLE);
		this.setSize(600, 700);
		this.setLocationRelativeTo(null);
		
		// pack selection pane
		DefaultMutableTreeNode packFolder = new DefaultMutableTreeNode(ConfigurationHandler.getPackFolder());
		JTree fileTree = new PackTree(packFolder);
		for (File file : getPackFolderFiles()) {
			packFolder.add(new DefaultMutableTreeNode(file));
		}
		fileTree.expandRow(0); // initially expand first row to show all available packs
		this.setLayout(new BorderLayout());
		JScrollPane selectionScrollPane = new JScrollPane(fileTree);
		fileTree.addTreeSelectionListener(l -> this.valueChanged(l));
//		this.add(scrollPane, BorderLayout.CENTER);
		
		// button panel
		JButton buttonSave = new JButton(BUTTON_SAVE); // button to save the current selection
		JButton buttonCancel = new JButton(BUTTON_CANCEL); // button to cancel the current selection
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		GridBagConstraints panelConstraints = new GridBagConstraints();
		panelConstraints.gridx = 0;
		panelConstraints.gridy = 0;
		panelConstraints.weightx = 1.0;
		panelConstraints.fill = 1;
		panelConstraints.anchor = GridBagConstraints.EAST;
		panelConstraints.insets = new Insets(6, 6, 6, 3);
		buttonPanel.add(buttonSave, panelConstraints);
		panelConstraints.gridx = 1;
		panelConstraints.anchor = GridBagConstraints.WEST;
		panelConstraints.insets = new Insets(6, 3, 6, 6);
		buttonPanel.add(buttonCancel, panelConstraints);
		this.add(buttonPanel, BorderLayout.SOUTH);
//		saveButton.addActionListener(this);
		buttonCancel.addActionListener(l -> this.closeWindow());
		
		// details panel
		JPanel detailsPanel = new PackDetailsPanel(DETAILS_TITLE);
		panelConstraints.gridx = 0;
		panelConstraints.gridy = 0;
		// name display
		JPanel detailsNamePanel = new PackDetailsPanel(DETAILS_NAME);
		detailsNamePanel.add(detailsNameValue);
		detailsPanel.add(detailsNamePanel, panelConstraints);
		//pack display
		panelConstraints.gridy = 1;
		JPanel detailsPacksPanel = new PackDetailsPanel(DETAILS_PACKS);
		detailsPacksPanel.add(detailsPacksValue);
		detailsPanel.add(detailsPacksPanel, panelConstraints);
		// dependencies display
		panelConstraints.gridy = 2;
		JPanel detailsDependenciesPanel = new PackDetailsPanel(DETAILS_DEPENDENCIES);
		detailsDependenciesPanel.add(detailsDependenciesValue);
		detailsPanel.add(detailsDependenciesPanel, panelConstraints);
		// entry display
		panelConstraints.gridy = 3;
		JPanel detailsEntriesPanel = new PackDetailsPanel(DETAILS_ENTRIES);
		detailsEntriesPanel.add(detailsEntriesValue);
		detailsPanel.add(detailsEntriesPanel, panelConstraints);
		
		
		JScrollPane detailsScrollPane = new JScrollPane(detailsPanel);
		
		// split pane
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.add(selectionScrollPane);
		splitPane.add(detailsScrollPane);
		splitPane.setResizeWeight(0.7); // selection panel should cover more of the window than the details panel
		this.add(splitPane, BorderLayout.CENTER);
		
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
	
	public void closeWindow() {
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)); // close the window
	}

	// function called on change of the selected entries
	public void valueChanged(TreeSelectionEvent event) {
		if (event != null && event.getSource() != null) {
			PackTree tree = (PackTree) event.getSource(); // the source must be a PackTree
			TreePath[] paths = tree.getSelectionPaths();
			if (paths != null) { // there is a selection
				ArrayList<DefaultMutableTreeNode> nodes = new ArrayList<DefaultMutableTreeNode>();
				for (TreePath path : paths) {
					if (path != null && !nodes.contains(path.getLastPathComponent())) {
						nodes.add((DefaultMutableTreeNode) path.getLastPathComponent());
					}
				}
				this.selectedNodes = nodes.toArray(new DefaultMutableTreeNode[nodes.size()]);
			} else { // everything was deselected
				this.selectedNodes = null;
				// TODO: do stuff
			}
			this.updateDetails(); // update the details section according to the selected packs
		}
	}
	
	// updates the details  section according to the selected packs
	public void updateDetails() {
		if (selectedNodes != null) {
			ArrayList<File> files = new ArrayList<File>();
			ArrayList<String> packs = new ArrayList<String>();
//			ArrayList<Pack> packs = new ArrayList<Pack>();
			File file = null;
			for (DefaultMutableTreeNode node : selectedNodes) {
				if (node.getUserObject() != null) {
					if (node.getUserObject() instanceof File &&  !files.contains((File) node.getUserObject())) {
						file = (File) node.getUserObject();
						files.add(file);
//						LabJProject dummyProject = new LabJProject(); // create a PackHandler
//						dummyProject.loadPacks((File) node.getUserObject()); // load all packs from this file
//						for (Pack pack : dummyProject.getLoadedPacks()) { // add all packs contained by this file to the list of selected packs
//							if (pack != null && !packs.contains(pack)) {
//								packs.add(pack);
//							}
//						}
//						
					for (@SuppressWarnings("rawtypes") Class c : LabJProject.PACK_CLASSES) { // add all packs contained by this file to the list of selected packs
						if (Pack.isPackType(file, c)) {
							String s = file + ":" + c.getName();
							if ( s != null && !packs.contains(s)) {
								packs.add(s);
							}
						}

					}
						
					} else if (node.getUserObject() instanceof Pack &&  !packs.contains((Pack) node.getUserObject())) {
						packs.add(((Pack) node.getUserObject()).toString());
					}
					
				}
			}
			detailsNameValue.setText(formatArray(files.toArray()));
			detailsPacksValue.setText(formatArray(packs.toArray()));
		} else {
			detailsNameValue.setText(DETAILS_NULL_VALUE);
			detailsPacksValue.setText(DETAILS_NULL_VALUE);
			detailsEntriesValue.setText(DETAILS_NULL_VALUE);
		}
	}
	
	private static String formatArray(Object[] texts) {
		if (texts != null) {
			String formattedText = "";
			for (Object text : texts) {
				if (text != null) {
					formattedText = formattedText + text.toString() + "<br>";					
				}
			}
			formattedText = "<html>" + formattedText + "</html>";
			return formattedText;
		}
		return null;
	}
}
