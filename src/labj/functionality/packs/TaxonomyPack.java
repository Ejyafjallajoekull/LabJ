package labj.functionality.packs;

import java.io.File;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import labjframework.packs.Pack;
import labjframework.packs.PackEntry;

public class TaxonomyPack extends Pack {

	// fields	
	private static final String TAXONOMY = "Taxonomy"; // the node containing all relevant information for this taxonomy entity and its childs 
	private static final String SUB_PARENT = "Parent"; // the node referencing the parent taxon of a specific entry 
	private static final String SUB_CHILD = "Child"; // the node indicating child taxa of a specific entry 
	private static final String[] SUB_PROPERTIES = {SUB_ID, SUB_NAME, SUB_DISPLAY, SUB_ATTACHMENT, SUB_COMMENT}; // a list of all properties of a substance

	// constructor
	public TaxonomyPack(File file) {
		super(file, TAXONOMY, SUB_PROPERTIES);
	}

	@Override
	public boolean loadEntry(Element entry) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateCustomProperties(Node parentNode, PackEntry entry) {
		if (this.packDoc != null && entry != null && parentNode != null) {
			Taxonomy tax = (Taxonomy) entry;
			// molecular weight
			this.addProperty(parentNode, SUB_PARENT, tax.getParentTaxon().getId()); // TODO: References
			// density
			this.addProperty(parentNode, SUB_CHILD, tax.getChildTaxa());
		}
	}

}
