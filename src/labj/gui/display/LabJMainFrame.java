package labj.gui.display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import labj.functionality.housekeeping.configuration.ConfigurationHandler;

public class LabJMainFrame extends JFrame {

	// serialisation
	private static final long serialVersionUID = 1L;
	
	// constants
	private static final String MAIN_TITLE = "LabJ"; // the title of the main frame
	// menu
	private static final String MENU_MANAGE = "Manage";
	private static final String MENU_MANAGE_PACKS = "Packs";
	
	// fields
	private JMenuBar mainMenu = new JMenuBar(); // the main menu bar
	
	public LabJMainFrame() {
		this.setTitle(MAIN_TITLE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit process on close
		this.setSize(ConfigurationHandler.getMainWindowWidth(), ConfigurationHandler.getMainWindowHeigth());
		this.setLocationRelativeTo(null); // place in the middle of the screen
		this.initialiseMainMenu();
		this.setVisible(true);
	}
	
	public void initialiseMainMenu() {
		JMenuItem menuItemManagePacks = new JMenuItem(MENU_MANAGE_PACKS);
		JMenu menuManage = new JMenu(MENU_MANAGE);
		menuManage.add(menuItemManagePacks);
		mainMenu.add(menuManage);
		
		menuItemManagePacks.addActionListener(l -> this.managePacks());
		
		this.add(mainMenu); // add the main menu bar
		this.setJMenuBar(mainMenu); // set to menu bar
	}
	
	public void managePacks() {
		// TODO: do stuff
	}

}
