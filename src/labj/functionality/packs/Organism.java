package labj.functionality.packs;

import labjframework.packs.PackEntry;
import labjframework.packs.PackHandler;
import labjframework.packs.PackReference;

public class Organism extends PackEntry {
// class representing an organism
	
	private PackReference taxonomy = null; // the taxonomy of the organism

	// constructor	
	public Organism(String id, String[] names, OrganismPack owningPack) {
		super(id, names, owningPack);
	}

	public Organism(String id, String[] names, Taxonomy taxonomy, OrganismPack owningPack) {
		super(id, names, owningPack);
		this.setTaxonomy(taxonomy);
	}
	
	public Organism(String id, String[] names, PackReference taxonomy, OrganismPack owningPack) {
		super(id, names, owningPack);
		this.taxonomy = taxonomy;
	}
	
	@Override
	public Organism clone() {
		Organism org = new Organism(this.id, this.trivialNames.toArray(new String[this.trivialNames.size()]), this.taxonomy, (OrganismPack) this.pack);
		org.setAttachments(this.attachments);
		org.setComments(this.comments);
		org.setDisplayName(this.getDisplayName());
		return org;
	}	

	// getters
	@Override
	public OrganismPack getPack() {
		return (OrganismPack) pack;
	}

	public Taxonomy getTaxonomy(PackHandler handler) {
		if (taxonomy != null) {
			return (Taxonomy) taxonomy.findReference(handler);
		}
		return null;
	}
	
	public PackReference getTaxonomyReference() {
		return this.taxonomy;
	}

	// setters
	public void setTaxonomy(Taxonomy taxonomy) {
		if (taxonomy != null) {
			this.taxonomy = taxonomy.getReference();
		} else {
			this.taxonomy = null;
		}
	}
	
	public void setTaxonomyReference(PackReference taxonomy) {
		this.taxonomy = taxonomy;
	}
	
}
