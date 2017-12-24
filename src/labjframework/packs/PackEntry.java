package labjframework.packs;

import java.util.AbstractList;
import java.util.ArrayList;

import labjframework.utilities.XMLFormattedText;

/**
 * The PackEntry class is the abstract representation of a specific entry of 
 * a XML-Pack.
 * 
 * @author Planters
 *
 */
public abstract class PackEntry {
// entry of a XML pack

	protected Pack pack = null; // the pack containing this entry
	protected String id; // the pack unique identification string of the entry
	protected ArrayList<XMLFormattedText> comments = new ArrayList<XMLFormattedText>(); // a comment for the entry
	protected ArrayList<XMLFormattedText> attachments = new ArrayList<XMLFormattedText>(); // the references to the attachments
	protected ArrayList<String> trivialNames = new ArrayList<String>(); // the different (trivial) names of the substance
	protected String displayName = NO_DISPLAYNAME; // the name to be displayed // can be substanceId or another trivial name
	
	// constants
	private static final String NO_DISPLAYNAME = "No name defined"; // constant to be displayed, if no names are defined

	public abstract PackEntry clone(); // clone this entry and return an identical one
	
	
	// constructor
	/**
	 * Create an entry with the specified ID, names and a containing Pack.
	 * 
	 * @param id - the Pack unique identification of this entry
	 * @param names - the names this entry may carry
	 * @param owningPack - the Pack containing this entry
	 */
	public PackEntry(String id, String[] names, Pack owningPack) {
		this.id = id;
		if (names.length > 0) {
			this.displayName = names[0]; // first off display the initial name
			this.addName(names);
		}
		this.setPack(owningPack);
	}
	
	/**
	 * Add a name to this entry.
	 * Duplicate names are not allowed.
	 * 
	 * @param name - the name to add
	 * @return true if the addition has been successful and no duplicates were made
	 */
	public boolean addName(String name) {
		for (String item : this.trivialNames) {
			if (item.equals(name)) {
				return false;
			}
		}
		return this.trivialNames.add(name);
	}
	
	/**
	 * Add multiple names to the entry at once.
	 * Duplicate names are not allowed.
	 * 
	 * @param names - the names to add
	 * @return true if at least one name has been added successfully
	 */
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
	/**
	 * Add multiple names to the entry at once.
	 * Duplicate names are not allowed.
	 * @param names - the names to add
	 * @return true if at least one name has been added successfully
	 */
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
	/**
	 * Remove the specified name if present.
	 * 
	 * @param name - the name to remove
	 * @return true if the removal was successful
	 */
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
	
	
	/**
	 * Removes the name at the specified index and return the removed name.
	 * 
	 * @param nameIndex - the index of the name to remove
	 * @return the name that has been removed from the entry
	 */
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
	/**
	 * Set the name to be displayed for this entry.
	 * 
	 * @param nameIndex - the index of the name to represent this entry
	 */
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
	
	// returns a string reference to this specific XML entry
	public PackReference getReference() {
		return new PackReference(this, null);
	}
	
	@Override
	public String toString() {
		return (this.pack + ":" + this.getClass().getName() + ":" + this.id + ":" + this.displayName);
	}
	
	// getters
	public Pack getPack() {
		return pack;
	}
	
	public ArrayList<XMLFormattedText> getComments() {
		return comments;
	}
	
	public ArrayList<XMLFormattedText> getAttachments() {
		return attachments;
	}
	
	public String getId() {
		return id;
	}
	
	public ArrayList<String> getTrivialNames() {
		return trivialNames;
	}

	/**
	 * Get the current representative name of this entry.
	 * 
	 * @return the name, which will be displayed for this entry
	 */
	public String getDisplayName() {
		return displayName;
	}
	// setters
	/**
	 * Set the Pack that this entry is part of.
	 * 
	 * @param pack - the pack owning this entry
	 */
	public void setPack(Pack pack) {
		this.pack = pack;
	}

	/**
	 * Set the comments attached to this entry.
	 * 
	 * @param comments - the comments to set
	 */
	public void setComments(ArrayList<XMLFormattedText> comments) {
		this.comments = comments;
	}
	
	public void setAttachments(ArrayList<XMLFormattedText> attachments) {
		this.attachments = attachments;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void setTrivialNames(ArrayList<String> trivialNames) {
		this.trivialNames = trivialNames;
	}

}
