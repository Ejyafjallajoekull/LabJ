package labj.functionality.packs;

import java.util.ArrayList;

import labjframework.packs.PackEntry;

public class Taxonomy extends PackEntry {
// a taxonomic representation of life
	
	protected ArrayList<Taxonomy> childTaxa = new ArrayList<Taxonomy>(); // all subclasses of this taxonomical entity
	protected Taxonomy parentTaxon = null; // the superclass of this taxonomical entity
	
	public Taxonomy(String id, String[] names, Taxonomy parentTaxon, TaxonomyPack owningPack) {
		super(id, names, owningPack);
		this.parentTaxon = parentTaxon;
	}

	@Override
	public PackEntry clone() {
		Taxonomy tax = new Taxonomy(this.id, this.trivialNames.toArray(new String[this.trivialNames.size()]), this.parentTaxon, (TaxonomyPack) this.pack);
		tax.setAttachments(this.attachments);
		tax.setComments(this.comments);
		tax.setDisplayName(this.displayName);
		tax.setChildTaxa(this.childTaxa);
		return null;
	}

	public ArrayList<Taxonomy> getChildTaxa() {
		return this.childTaxa;
	}

	public void setChildTaxa(ArrayList<Taxonomy> childrenTaxa) {
		this.childTaxa = childrenTaxa;
	}

	public Taxonomy getParentTaxon() {
		return this.parentTaxon;
	}

	public void setParentTaxon(Taxonomy parentTaxon) {
		this.parentTaxon = parentTaxon;
	}


}
