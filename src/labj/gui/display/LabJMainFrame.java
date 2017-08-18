package labj.gui.display;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import labj.functionality.housekeeping.configuration.ConfigurationHandler;
import labj.functionality.packs.LabJPackHandler;
import labj.functionality.packs.SubstancePack;
import labj.functionality.packs.TaxonomyPack;
import labj.functionality.project.LabJProject;
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
	private static final String MENU_FILE_SAVEALL = "Save all";
	
	// fields
	private PackHandler mainHandler = new LabJPackHandler(); // the handler to manage all loaded Packs
	private JMenuBar mainMenu = new JMenuBar(); // the main menu bar
	private LabJProject mainProject = null; // the current project
	
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
		JMenuItem menuItemFileOpen = new JMenuItem(MENU_FILE_OPEN);
		JMenuItem menuItemFileSave = new JMenuItem(MENU_FILE_SAVE);
		JMenuItem menuItemFileSaveAll = new JMenuItem(MENU_FILE_SAVEALL);
		JMenu menuFile = new JMenu(MENU_FILE);
		menuFile.add(menuItemFileOpen);
		menuItemFileOpen.addActionListener(l -> this.openFile());
		menuFile.addSeparator();
		menuFile.add(menuItemFileSave);
		menuItemFileSave.addActionListener(l -> this.saveFile());
		menuFile.addSeparator();
		menuFile.add(menuItemFileSaveAll);
		menuItemFileSaveAll.addActionListener(l -> this.saveAll());
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
		FileNameExtensionFilter ff = new FileNameExtensionFilter("LabJ Pack", "xml");
		fc.addChoosableFileFilter(ff);
		ff = new FileNameExtensionFilter("LabJ Project", LabJProject.FILE_EXTENSION);
		fc.addChoosableFileFilter(ff);
		fc.setFileFilter(ff);
		fc.showOpenDialog(this);
		return false;
	}
	
	public void saveFile() {
		File file = new File("testproject.labj");
		LabJProject pro = new LabJProject(file);
		pro.addPack(new SubstancePack(new File("sp_test_01.xml")));
		pro.addPack(new TaxonomyPack(new File("sp_test_01.xml")));
		pro.save();
	}
	
	public void saveAll() {
		this.mainHandler.saveAll(); // save all packs
	}

	public PackHandler getMainHandler() {
		return mainHandler;
	}


}
