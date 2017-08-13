package labjframework.packs;

import java.util.AbstractList;
import java.util.ArrayList;

import labjframework.utilities.XMLFormattedText;

public abstract class PackEntry {
// entry of a XML pack

	protected Pack pack = null; // the substance pack containing this substance
	protected String id; // the pack unique identification string of the entry
	protected ArrayList<XMLFormattedText> comments = new ArrayList<XMLFormattedText>(); // a comment for the entry
	protected ArrayList<XMLFormattedText> attachments = new ArrayList<XMLFormattedText>(); // the references to the attachments
	protected ArrayList<String> trivialNames = new ArrayList<String>(); // the different (trivial) names of the substance
	protected String displayName = NO_DISPLAYNAME; // the name to be displayed // can be substanceId or another trivial name
	
	// constants
	private static final String NO_DISPLAYNAME = "No name defined"; // constant to be displayed, if no names are defined

	public abstract PackEntry clone(); // clone this entry and return an identical one
	
	
	// constructor
	public PackEntry(String id, String[] names, Pack owningPack) {
		this.id = id;
		if (names.length > 0) {
			this.displayName = names[0]; // first off display the initial name
			this.addName(names);
		}
		this.setPack(owningPack);
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
	
	// returns a string reference to this specific XML entry
	public PackReference getReference() {
		return new PackReference(this, null);
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

	public String getDisplayName() {
		return displayName;
	}
	// setters
	public void setPack(Pack pack) {
		this.pack = pack;
	}

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
