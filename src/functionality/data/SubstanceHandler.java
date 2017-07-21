package functionality.data;

import java.io.File;
import java.util.ArrayList;

import functionality.housekeeping.logging.LoggingHandler;

public class SubstanceHandler {
// handles all substances
	
	private static ArrayList<Substance> substances = new ArrayList<Substance>(); // a list of all loaded substances
	private static ArrayList<SubstancePack> loadedPacks = new ArrayList<SubstancePack>(); // list of all loaded packs
	private static ArrayList<Substance> duplicateSubstances = new ArrayList<Substance>(); // a list of all non unique substances tried to be loaded

	
	
	public static boolean addSubstance(Substance substance) {
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
	}
	
	// loads substances from a XML
	public static void load(File file) {
		if (file != null) {
			loadedPacks.add(new SubstancePack(file));
		} else {
			LoggingHandler.getLog().warning("Cannot load a null file");
		}
	}
	
//	public static void saveAll()
}
