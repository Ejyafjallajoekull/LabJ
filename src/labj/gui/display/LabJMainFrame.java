package labj.gui.display;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import labj.functionality.housekeeping.configuration.ConfigurationHandler;
import labj.functionality.project.LabJProject;
import labj.gui.display.manage.PackManagerDialog;
import labjframework.packs.PackHandler;

public class LabJMainFrame extends JFrame {

	// serialisation
	private static final long serialVersionUID = 1L;
	
	// constants
	private static final String MAIN_TITLE = "LabJ"; // the title of the main frame
	// menu
	private static final String MENU_MANAGE = "Manage";
	private static final String MENU_MANAGE_PACKS = "Packs";
	private static final String MENU_FILE = "File";
	private static final String MENU_FILE_NEW = "New project";
	private static final String MENU_FILE_OPEN = "Open";
	private static final String MENU_FILE_SAVE = "Save";
	private static final String MENU_FILE_SAVEAS = "Save as";
	private static final String MENU_FILE_CLOSE = "Close";
	
	// fields
	private JMenuBar mainMenu = new JMenuBar(); // the main menu bar
	private LabJProject mainProject = new LabJProject(); // the current project && handler to manage all loaded Packs
	
	public LabJMainFrame() {
		this.setTitle(MAIN_TITLE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit process on close
		this.setSize(ConfigurationHandler.getMainWindowWidth(), ConfigurationHandler.getMainWindowHeigth());
		this.setLocationRelativeTo(null); // place in the middle of the screen
		this.initialiseMainMenu();
		this.setVisible(true);
	}
	
	public void initialiseMainMenu() {
		// File
		JMenuItem menuItemFileNew = new JMenuItem(MENU_FILE_NEW);
		JMenuItem menuItemFileOpen = new JMenuItem(MENU_FILE_OPEN);
		JMenuItem menuItemFileSave = new JMenuItem(MENU_FILE_SAVE);
		JMenuItem menuItemFileSaveAs = new JMenuItem(MENU_FILE_SAVEAS);
		JMenuItem menuItemFileClose = new JMenuItem(MENU_FILE_CLOSE);
		JMenu menuFile = new JMenu(MENU_FILE);
		menuFile.add(menuItemFileNew);
		menuItemFileNew.addActionListener(l -> this.createProject());
		menuFile.addSeparator();
		menuFile.add(menuItemFileOpen);
		menuItemFileOpen.addActionListener(l -> this.openFile());
		menuFile.addSeparator();
		menuFile.add(menuItemFileSave);
		menuItemFileSave.addActionListener(l -> this.saveFile());
		menuFile.addSeparator();
		menuFile.add(menuItemFileSaveAs);
		menuItemFileSaveAs.addActionListener(l -> this.saveAs());
		menuFile.addSeparator();
		menuFile.add(menuItemFileClose);
		menuItemFileClose.addActionListener(l -> this.close());
		mainMenu.add(menuFile);
		// Manage
		JMenuItem menuItemManagePacks = new JMenuItem(MENU_MANAGE_PACKS);
		JMenu menuManage = new JMenu(MENU_MANAGE);
		menuManage.add(menuItemManagePacks);
		mainMenu.add(menuManage);
		
		menuItemManagePacks.addActionListener(l -> this.managePacks());
		
		this.add(mainMenu); // add the main menu bar
		this.setJMenuBar(mainMenu); // set to menu bar
	}
	
	public void managePacks() {
		new PackManagerDialog(this);
	}
	
	public boolean openFile() {
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter ff = new FileNameExtensionFilter("LabJ Project", LabJProject.FILE_EXTENSION);
		fc.addChoosableFileFilter(ff);
		fc.setFileFilter(ff); // LabJ project files as standard filter
		fc.setCurrentDirectory(new File(".")); // set the working directory as current directory
		fc.setMultiSelectionEnabled(false); // only one project can be opened at any time
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY); // only allow selection of files
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File fileToLoad = fc.getSelectedFile();
			this.mainProject = new LabJProject(fileToLoad);
			this.mainProject.loadProject();
			return true;
		}
		return false;
	}
	
	// save the currently loaded project file
	public void saveFile() {
		if (this.mainProject != null && this.mainProject.getProjectFile() != null) {
			this.mainProject.saveProject(); // save the project
			this.mainProject.saveAllPacks(); // save all loaded XML packs
		} else { // if no project file, let the user specify one
			this.saveAs();
		}
	}
	
	public void saveAs() {
		OverwriteFileChooser fc = new OverwriteFileChooser();
		fc.setCurrentDirectory(new File("."));
		fc.setMultiSelectionEnabled(false); // only one file can be selected
		if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			// add file extension if not specified by user
			if (!file.getName().toLowerCase().endsWith("." + LabJProject.FILE_EXTENSION)) {
				file = new File(file.getAbsolutePath() + "." + LabJProject.FILE_EXTENSION);
			}
			// TODO: if project is null transfer changes done while project is null
			// if no project is loaded, create a new project and save it
			if (this.mainProject == null) {
				this.mainProject = new LabJProject(); // create a new project to actually save changes
			}
			this.mainProject.saveProject(file); // save the project to the new file
			this.mainProject.saveAllPacks(); // save all currently loaded packs
		}
	}
	
	// close the current project and save a new one
	public void createProject() {
		this.close();
		this.saveAs();
	}
	
	public void close() {
		// TODO: deselect all selected packs
		this.mainProject.setProjectFile(null);
		this.mainProject.unloadAll(); // unload all loaded packs
	}

	public PackHandler getMainProject() {
		return this.mainProject;
	}
	
	private final class OverwriteFileChooser extends JFileChooser {

		// serialisation
		private static final long serialVersionUID = 1L;
		
		private String message = "Do you really want to replace \"%s\"";
		private String title = "Replace existing file?";
		
		@Override
		public void approveSelection() {
			File file = this.getSelectedFile();
			if (file.exists()) {
				message = String.format(message, file.getName());
				int option = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (option != JOptionPane.OK_OPTION) {
					return;
				}
			}
			super.approveSelection();
		}
	}


}
