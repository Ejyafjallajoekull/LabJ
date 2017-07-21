package functionality.data;

import java.math.BigDecimal;
import java.util.ArrayList;

import functionality.housekeeping.logging.LoggingHandler;


public class Substance extends PackEntry{
// a chemical substance
	
	// BigDecimal for exact representation of values
	private BigDecimal molecularWeight = new BigDecimal(0); // the molecular weight
	private BigDecimal density = new BigDecimal(0); // the density
	
	private ArrayList<String> trivialNames = new ArrayList<String>(); // the different (trivial) names of the substance
	private String displayName = ""; // the name to be displayed // can be substanceId or another trivial name
	
	// constructor
/*	public Substance(String name, BigDecimal molecularWeight, BigDecimal density) {
		this.id = name;
		this.molecularWeight = molecularWeight;
		this.density = density;
		this.displayName = id; // first off display the identifier
		if (!SubstanceHandler.addSubstance(this)) { // add the substance to the substance list
			LoggingHandler.getLog().warning("Substance \"" + this.id + "\" is not unique");
		}
	}
*/	
	public Substance(String name, BigDecimal molecularWeight, BigDecimal density, SubstancePack owningFile) {
		this.id = name;
		this.molecularWeight = molecularWeight;
		this.density = density;
		this.displayName = this.id; // first off display the identifier
		this.setPack(owningFile);
		if (!SubstanceHandler.addSubstance(this)) { // add the substance to the substance list
			LoggingHandler.getLog().warning("Substance \"" + this.id + "\" is not unique");
		}
	}
	
	// clones the substance and returns an identical one
	@Override
	public Substance clone() {
		return new Substance(this.id, this.molecularWeight, this.density, (SubstancePack) this.pack);
	}
	
	// adds a (trivial) name
	public boolean addName(String name) {
		for (String item : this.trivialNames) {
			if (item.equals(name)) {
				return false;
			}
		}
		this.trivialNames.add(name);
		return true;
	}
	
/*	// adds multiple (trivial) names
		public boolean addNames(String[] names) {
			for (String nameToAdd : names) {
				for (String item : this.trivialNames) {
					if (item.equals(nameToAdd)) {
						break;
					} else if () {
						
					}
				}
			}
		}
*/
	
	// removes the given name if present
	public void removeName(String name) {
		this.trivialNames.remove(name);
	}
	
	// removes the name at given index
	public void removeName(int nameIndex) {
		if (nameIndex < this.trivialNames.size() && nameIndex >= 0) {
			this.trivialNames.remove(nameIndex);
		}
	}
	
	// sets the name to be displayed to the given (trivial) name or the Id if -1 is given as parameter
	public void setDisplayName(int nameIndex) {
		if (nameIndex < this.trivialNames.size() && nameIndex >= 0) {
			this.displayName = this.trivialNames.get(nameIndex);
		} else {
			this.displayName = this.id;
		}
	}
	

	// getters
	public BigDecimal getMolecularWeight() {
		return molecularWeight;
	}

	public BigDecimal getDensity() {
		return density;
	}

	public ArrayList<String> getTrivialNames() {
		return trivialNames;
	}

	public String getDisplayName() {
		return displayName;
	}
	
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

	public void setTrivialNames(ArrayList<String> trivialNames) {
		this.trivialNames = trivialNames;
	}

	public void setPack(SubstancePack pack) {
		this.pack = pack;
	}

}
