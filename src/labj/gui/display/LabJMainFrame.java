package labj.gui.display;

import javax.swing.JFrame;

import labj.functionality.housekeeping.configuration.ConfigurationHandler;

public class LabJMainFrame extends JFrame {

	// serialisation
	private static final long serialVersionUID = 1L;
	
	// constants
	private static final String MAIN_TITLE = "LabJ"; // the title of the main frame
	
	public LabJMainFrame() {
		this.setTitle(MAIN_TITLE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit process on close
		this.setSize(ConfigurationHandler.getMainWindowWidth(), ConfigurationHandler.getMainWindowHeigth());
		this.setLocationRelativeTo(null); // place in the middle of the screen
/*		this.add(mainMenu); // add the main menu bar
		this.setJMenuBar(mainMenu); // set to menu bar
		this.add(editorWindow); // add text editor window
*/		this.setVisible(true);
	}

}
