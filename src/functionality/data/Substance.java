package functionality.data;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;

import functionality.housekeeping.logging.LoggingHandler;

public class Substance extends PackEntry{
// a chemical substance
	
	// BigDecimal for exact representation of values
	private BigDecimal molecularWeight = new BigDecimal(0); // the molecular weight
	private BigDecimal density = new BigDecimal(0); // the density
	
	private String substanceId = ""; // the name and unique identification string
	private ArrayList<String> trivialNames = new ArrayList<String>(); // the different (trivial) names of the substance
	private String displayName = ""; // the name to be displayed // can be substanceId or another trivial name
	
	private SubstancePack pack = null; // the substance pack containing this substance
	
	// constructor
	public Substance(String name, BigDecimal molecularWeight, BigDecimal density) {
		this.substanceId = name;
		this.molecularWeight = molecularWeight;
		this.density = density;
		this.displayName = substanceId; // first off display the identifier
		if (!SubstanceHandler.addSubstance(this)) { // add the substance to the substance list
			LoggingHandler.getLog().warning("Substance \"" + this.substanceId + "\" is not unique");
		}
	}
	
	public Substance(String name, BigDecimal molecularWeight, BigDecimal density, SubstancePack owningFile) {
		this.substanceId = name;
		this.molecularWeight = molecularWeight;
		this.density = density;
		this.displayName = substanceId; // first off display the identifier
		this.setPack(owningFile);
		if (!SubstanceHandler.addSubstance(this)) { // add the substance to the substance list
			LoggingHandler.getLog().warning("Substance \"" + this.substanceId + "\" is not unique");
		}
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
			this.displayName = this.substanceId;
		}
	}
	

	// getters
	public BigDecimal getMolecularWeight() {
		return molecularWeight;
	}

	public BigDecimal getDensity() {
		return density;
	}

	public String getSubstanceId() {
		return substanceId;
	}

	public ArrayList<String> getTrivialNames() {
		return trivialNames;
	}

	public String getDisplayName() {
		return displayName;
	}
	
	public SubstancePack getPack() {
		return pack;
	}

	// setters
	public void setMolecularWeight(BigDecimal molecularWeight) {
		this.molecularWeight = molecularWeight;
	}

	public void setDensity(BigDecimal density) {
		this.density = density;
	}

	public void setSubstanceId(String substanceId) {
		this.substanceId = substanceId;
	}

	public void setTrivialNames(ArrayList<String> trivialNames) {
		this.trivialNames = trivialNames;
	}

	public void setPack(SubstancePack pack) {
		this.pack = pack;
	}

}
