package labj.functionality.packs;

import java.util.ArrayList;

import labjframework.packs.PackEntry;

public class Organism extends PackEntry {
// class representing an organism
	
	private ArrayList<String> taxonomy = new ArrayList<String>(); // the taxonomy of the organism

	// constructor	
	public Organism(String id, String[] names, OrganismPack owningFile) {
		this.id = id;
		if (names.length > 0) {
			this.displayName = names[0]; // first off display the initial name
			this.addName(names);
		}
		this.setPack(owningFile);
	}
	
	@Override
	public Organism clone() {
		Organism org = new Organism(this.id, this.trivialNames.toArray(new String[this.trivialNames.size()]), (OrganismPack) this.pack);
		org.setTaxonomy(this.taxonomy);
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

	public ArrayList<String> getTaxonomy() {
		return taxonomy;
	}

	// setters
	public void setTaxonomy(ArrayList<String> taxonomy) {
		this.taxonomy = taxonomy;
	}
	
}
