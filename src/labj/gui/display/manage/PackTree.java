package labj.gui.display.manage;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

public class PackTree extends JTree {

	// serialisation
	private static final long serialVersionUID = 1L;

	// construct a file tree showing all packs in a specific root folder
	public PackTree(File root) {
		super(new DefaultMutableTreeNode(root));
	}
	
	public PackTree() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PackTree(Hashtable<?, ?> arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public PackTree(Object[] arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public PackTree(TreeModel arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public PackTree(TreeNode arg0, boolean arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public PackTree(TreeNode arg0) {
		super(arg0);
		
	}

	public PackTree(Vector<?> arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			return ((File) ((DefaultMutableTreeNode) value).getUserObject()).getName();
	}
	
	

}
