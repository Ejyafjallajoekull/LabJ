package labj.functionality.packs;

import java.util.ArrayList;

import labjframework.packs.Pack;
import labjframework.packs.PackEntry;
import labjframework.packs.PackHandler;
import labjframework.packs.PackReference;

public class Taxonomy extends PackEntry {
// a taxonomic representation of life
	
//	protected ArrayList<PackReference> childTaxa = new ArrayList<PackReference>(); // all subclasses of this taxonomical entity
	protected PackReference parentTaxon = null; // the superclass of this taxonomical entity
	
	public Taxonomy(String id, String[] names, Taxonomy parentTaxon, TaxonomyPack owningPack) {
		super(id, names, owningPack);
		this.setParentTaxon(parentTaxon);
	}
	
	public Taxonomy(String id, String[] names, PackReference parentTaxon, TaxonomyPack owningPack) {
		super(id, names, owningPack);
		this.parentTaxon = parentTaxon;	
	}

	@Override
	public PackEntry clone() {
		Taxonomy tax = new Taxonomy(this.id, this.trivialNames.toArray(new String[this.trivialNames.size()]), this.parentTaxon, (TaxonomyPack) this.pack);
		tax.setAttachments(this.attachments);
		tax.setComments(this.comments);
		tax.setDisplayName(this.displayName);
	//	tax.setChildTaxaReferences(this.childTaxa);
		return tax;
	}

//	public ArrayList<PackReference> getChildTaxaReferences() {
//		return this.childTaxa;
//	}

//	public void setChildTaxaReferences(ArrayList<PackReference> childTaxa) {
//		this.childTaxa = childTaxa;
//	}
	
	public ArrayList<Taxonomy> getChildTaxa(PackHandler handler) {
		ArrayList<Taxonomy> children = new ArrayList<Taxonomy>();
		ArrayList<Pack> taxPacks = handler.getLoadedPacks(TaxonomyPack.class); // all taxonomy packs loaded by the handler
		
		for (Pack pack : taxPacks) {
			if (pack != null) {
				ArrayList<PackEntry> entries = pack.getEntries();
				for (PackEntry entry : entries) {
					if (entry != null && ((Taxonomy) entry).getParentTaxonReference() != null && ((Taxonomy) entry).getParentTaxonReference().equals(this.getReference())) {
						children.add((Taxonomy) entry);
					}
				}
			}
		}
		return children;
	}

//	public void setChildTaxa(ArrayList<Taxonomy> childTaxa) {
//		ArrayList<PackReference> children = new ArrayList<PackReference>();
//		for (Taxonomy child : childTaxa) {
//			if (child != null) {
//				children.add(child.getReference());
//			}
//		}
//		this.childTaxa = children;
//	}

	public PackReference getParentTaxonReference() {
		return this.parentTaxon;
	}

	public void setParentTaxon(Taxonomy parentTaxon) {
		if (parentTaxon != null) {
			this.parentTaxon = parentTaxon.getReference();
		} else {
			this.parentTaxon = null;
		}
	}

	public Taxonomy getParentTaxon(PackHandler handler) {
		return (Taxonomy) this.parentTaxon.findReference(handler);
	}

	public void setParentTaxonReference(PackReference parentTaxon) {
		this.parentTaxon = parentTaxon;
	}
}
