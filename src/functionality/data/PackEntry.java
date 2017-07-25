package functionality.data;

import java.util.ArrayList;

public abstract class PackEntry {
// entry of a XML pack

	protected Pack pack = null; // the substance pack containing this substance
	protected String id; // the pack unique identification string of the entry
	protected ArrayList<String> comments = new ArrayList<String>(); // a comment for the entry
	protected ArrayList<String> attachments = new ArrayList<String>(); // the references to the attachments

	public abstract PackEntry clone(); // clone this entry and return an identical one
	
	// getters
	public Pack getPack() {
		return pack;
	}
	
	public ArrayList<String> getComments() {
		return comments;
	}
	
	public ArrayList<String> getAttachments() {
		return attachments;
	}
	
	public String getId() {
		return id;
	}

	// setters
	public void setPack(Pack pack) {
		this.pack = pack;
	}

	public void setComments(ArrayList<String> comments) {
		this.comments = comments;
	}
	
	public void setAttachments(ArrayList<String> attachments) {
		this.attachments = attachments;
	}

	public void setId(String id) {
		this.id = id;
	}

}
