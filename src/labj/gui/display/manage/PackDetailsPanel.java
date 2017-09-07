package labj.gui.display.manage;

import java.awt.GridBagLayout;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class PackDetailsPanel extends JPanel {

	// serilisation
	private static final long serialVersionUID = 1L;

	public PackDetailsPanel(String title) {
		super(new GridBagLayout());
		this.init();
	}

	public PackDetailsPanel(LayoutManager arg0) {
		super(arg0);
		this.init();
	}

	public PackDetailsPanel(boolean arg0) {
		super(arg0);
		this.init();
	}

	public PackDetailsPanel(LayoutManager arg0, boolean arg1) {
		super(arg0, arg1);
		this.init();
	}

	private void init() {
		
	}
}
