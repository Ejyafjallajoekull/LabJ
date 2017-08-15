package labj.gui.display;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import labj.functionality.housekeeping.configuration.ConfigurationHandler;

public class LabJMainFrame extends JFrame {

	// serialisation
	private static final long serialVersionUID = 1L;
	
	// constants
	private static final String MAIN_TITLE = "LabJ"; // the title of the main frame
	
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
		mainMenu.add(new JMenu("Nana"));
		
		this.add(mainMenu); // add the main menu bar
		this.setJMenuBar(mainMenu); // set to menu bar
	}

}
