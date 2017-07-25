package functionality.integrity;

import java.io.File;
import java.util.ArrayList;

import functionality.data.SubstancePack;
import functionality.housekeeping.logging.LoggingHandler;

public class PackHandler {
// keeps track of all loaded packs

//	private static ArrayList<Substance> substances = new ArrayList<Substance>(); // a list of all loaded substances
	private static ArrayList<SubstancePack> loadedSubstancePacks = new ArrayList<SubstancePack>(); // list of all loaded packs
	
	
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
	
	// loads entries from a XML
	public static void loadAllPacks(File file) {
		if (file != null) {
			loadSubstancePack(file); // load substance file
			// TODO: load anything else
		} else {
			LoggingHandler.getLog().warning("Cannot load a null file");
		}
	}
	
	// loads a substance pack from a XML
	public static void loadSubstancePack(File file) {
		if (file != null) {
			loadedSubstancePacks.add(new SubstancePack(file));
		} else {
			LoggingHandler.getLog().warning("Cannot load a null file");
		}
	}
	
//	public static void saveAll()
}
