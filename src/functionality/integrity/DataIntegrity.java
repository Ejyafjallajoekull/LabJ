package functionality.integrity;

import java.util.ArrayList;

import functionality.data.Substance;

public class DataIntegrity {
// keeps track of all loaded data
	
	private static ArrayList<Substance> substances = new ArrayList<Substance>(); // a list of all loaded substancess

	public static boolean addSubstance(Substance substance) {
		if (substance != null && !substances.contains(substance)) {
			for (Substance item : substances) {
				if (item.getId().equals(substance.getId())) {
					return false; // only allow substances with different Id
				}
			}
			substances.add(substance);
			return true;
		} else {
			return false;
		}
	}
}
