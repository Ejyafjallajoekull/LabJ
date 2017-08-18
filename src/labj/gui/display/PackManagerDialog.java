package labj.gui.display;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;

import javax.swing.JDialog;

public class PackManagerDialog extends JDialog {

	// serialisation
	private static final long serialVersionUID = 1L;
	
	// constants
	public static final String TITLE = "Pack Manager";

	public PackManagerDialog() {
		// TODO Auto-generated constructor stub
	}

	public PackManagerDialog(Frame arg0) {
		super(arg0);
		this.setTitle(TITLE);
		this.setSize(600, 700);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
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
