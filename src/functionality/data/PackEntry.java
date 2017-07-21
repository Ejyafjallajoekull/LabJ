package functionality.data;

public abstract class PackEntry {
// entry of a XML pack

	protected Pack pack = null; // the substance pack containing this substance
	protected String id; // the pack unique identification string of the entry
	protected String comment; // a comment for the entry
	
	public abstract PackEntry clone(); // clone this entry and return an identical one
	
	// getters
	public Pack getPack() {
		return pack;
	}
	
	public String getComment() {
		return comment;
	}
	
	public String getId() {
		return id;
	}

	// setters
	public void setPack(Pack pack) {
		this.pack = pack;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setId(String id) {
		this.id = id;
	}

}
