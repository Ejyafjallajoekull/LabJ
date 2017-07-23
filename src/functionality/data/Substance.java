package functionality.data;

import java.math.BigDecimal;
import java.util.AbstractList;
import java.util.ArrayList;

import functionality.housekeeping.logging.LoggingHandler;


public class Substance extends PackEntry{
// a chemical substance
	
	// BigDecimal for exact representation of values
	private BigDecimal molecularWeight = new BigDecimal(0); // the molecular weight
	private BigDecimal density = new BigDecimal(0); // the density
	
	private ArrayList<String> trivialNames = new ArrayList<String>(); // the different (trivial) names of the substance
	private String displayName = NO_DISPLAYNAME; // the name to be displayed // can be substanceId or another trivial name
	
	// constants
	private static final String NO_DISPLAYNAME = "No name defined"; // constant to be displayed, if no names are defined
	
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
	public Substance(String id, String[] names, BigDecimal molecularWeight, BigDecimal density, SubstancePack owningFile) {
		this.id = id;
		this.molecularWeight = molecularWeight;
		this.density = density;
		if (names.length > 0) {
			this.displayName = names[0]; // first off display the initial name
			this.addName(names);
		}
		this.setPack(owningFile);
		if (!SubstanceHandler.addSubstance(this)) { // add the substance to the substance list
			LoggingHandler.getLog().warning("Substance \"" + this.id + "\" is not unique");
		}
	}
	
	// clones the substance and returns an identical one
	@Override
	public Substance clone() {
		Substance sb = new Substance(this.id, this.trivialNames.toArray(new String[this.trivialNames.size()]), this.molecularWeight, this.density, (SubstancePack) this.pack);
		sb.setDisplayName(this.getDisplayName());
		sb.setComment(this.comment);
		return sb;
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
	
	// adds multiple (trivial) names handed over as array
	public boolean addName(String[] names) {
		boolean add = false; // tracker if a name was actually added
		for (String name : names) {
			if (this.addName(name)) {
				add = true; // if at least one name gets added return true
			}
		}
		return add;
	}
	
	// adds multiple (trivial) names handed over as list
		public boolean addName(AbstractList<String> names) {
			boolean add = false; // tracker if a name was actually added
			for (String name : names) {
				if (this.addName(name)) {
					add = true; // if at least one name gets added return true
				}
			}
			return add;
		}
	
	// removes the given name if present
	public boolean removeName(String name) {
		boolean removed = this.trivialNames.remove(name);
		if (removed && this.displayName.equals(name)) {
			if (!this.trivialNames.isEmpty()) {
				this.setDisplayName(0);
			} else {
				this.displayName = NO_DISPLAYNAME;
			}
		}
		return removed;
	}
	
	// removes the name at given index
	public String removeName(int nameIndex) {
		if (nameIndex < this.trivialNames.size() && nameIndex >= 0) {
			String name = this.trivialNames.remove(nameIndex);
			if (this.displayName.equals(name)) {
				if (!this.trivialNames.isEmpty()) {
					this.setDisplayName(0);
				} else {
					this.displayName = NO_DISPLAYNAME;
				}
			}
			return name;
		}
		return null;
	}
	
	// sets the name to be displayed to the given (trivial) name or the Id if -1 is given as parameter
	public void setDisplayName(int nameIndex) {
		if (nameIndex < this.trivialNames.size() && nameIndex >= 0) {
			this.displayName = this.trivialNames.get(nameIndex);
		} else {
			this.displayName = this.id;
		}
	}
	
	// sets the name to be displayed to the given (trivial) name or the Id if -1 is given as parameter
	public void setDisplayName(String name) {
		if (!this.trivialNames.contains(name)) {
			this.addName(name);
		}
		this.displayName = this.trivialNames.get(this.trivialNames.indexOf(name));
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
