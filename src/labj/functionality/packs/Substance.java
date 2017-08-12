package labj.functionality.packs;

import java.math.BigDecimal;

import labjframework.packs.PackEntry;


public class Substance extends PackEntry{
// a chemical substance
	
	// BigDecimal for exact representation of values
	private BigDecimal molecularWeight = new BigDecimal(0); // the molecular weight
	private BigDecimal density = new BigDecimal(0); // the density
	
	// constructor	
	public Substance(String id, String[] names, BigDecimal molecularWeight, BigDecimal density, SubstancePack owningPack) {
		super(id, names, owningPack);
		this.molecularWeight = molecularWeight;
		this.density = density;
	}
	
	// clones the substance and returns an identical one
	@Override
	public Substance clone() {
		Substance sb = new Substance(this.id, this.trivialNames.toArray(new String[this.trivialNames.size()]), this.molecularWeight, this.density, (SubstancePack) this.pack);
		sb.setDisplayName(this.getDisplayName());
		sb.setComments(this.comments);
		sb.setAttachments(this.attachments);
		return sb;
	}	

	// getters
	public BigDecimal getMolecularWeight() {
		return molecularWeight;
	}

	public BigDecimal getDensity() {
		return density;
	}
	
	@Override
	public SubstancePack getPack() {
		return (SubstancePack) pack;
	}

	// setters
	public void setMolecularWeight(BigDecimal molecularWeight) {
		this.molecularWeight = molecularWeight;
	}

	public void setDensity(BigDecimal density) {
		this.density = density;
	}


}
