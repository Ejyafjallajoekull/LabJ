package labjframework.packs;

import java.io.File;
import java.util.ArrayList;

public abstract class PackHandler {
// keeps track of loaded packs

//	private static ArrayList<Substance> substances = new ArrayList<Substance>(); // a list of all loaded substances
	protected ArrayList<Pack> loadedPacks = new ArrayList<Pack>(); // list of all loaded packs
	
	
/*	public static boolean addSubstance(Substance substance) {
		if (substance != null && !substances.contains(substance)) {
			for (Substance item : substances) {
				if (item.getId().equals(substance.getId())) {
					if (!duplicateSubstances.contains(substance)) { // add to duplicate list if not already present
						duplicateSubstances.add(substance);
					}
					return false; // only allow substances with different Id
				}
			}
			substances.add(substance);
			return true;
		} else {
			return false;
		}
	}*/
	
	// loads all possible entries from a XML
	public abstract void loadPack(File file);
	
	// whether the handler has this pack loaded or not
	public boolean hasPack(Pack pack) {
		for (Pack loadedPack : this.loadedPacks) {
			if (loadedPack.equals(pack)) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Pack> getLoadedPacks() {
		return this.loadedPacks;
	}
	
	// return all packs of the given class
	public ArrayList<Pack> getLoadedPacks(Class<? extends Pack> type) {
		ArrayList<Pack> packsOfType = new ArrayList<Pack>();
		if (type != null) {
			for (Pack loadedPack : this.loadedPacks) {
				if (loadedPack.getClass().equals(type)) {
					packsOfType.add(loadedPack);
				}
			}
		}
		return packsOfType;
	}
	
	// get all loaded packs in the specified file
	public ArrayList<Pack> getLoadedPacks(File file) {
		ArrayList<Pack> packsOfFile = new ArrayList<Pack>();
		if (file != null && file.exists()) {
			for (Pack loadedPack : this.loadedPacks) {
				if (loadedPack.getPackFile() == file) {
					packsOfFile.add(loadedPack);
				}
			}
		}
		return packsOfFile;
	}
	
	// saves all packs to XMLs // convenience method
	public void saveAll() {
		for (Pack pack : this.loadedPacks) {
			if (pack != null) { // failsafe check
				pack.save();
			}
		}
	}
}
