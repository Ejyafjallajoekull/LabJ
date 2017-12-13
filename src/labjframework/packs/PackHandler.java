package labjframework.packs;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.logging.Level;

import labjframework.logging.LoggingHandler;

public abstract class PackHandler {
// keeps track of loaded packs

//	private static ArrayList<Substance> substances = new ArrayList<Substance>(); // a list of all loaded substances
	protected ArrayList<Pack> loadedPacks = new ArrayList<Pack>(); // list of all loaded packs
	
	
	// loads all possible entries from a XML
	public abstract void loadPacks(File file);
	
	// whether the handler has this pack loaded or not
	public boolean hasPack(Pack pack) {
		return this.loadedPacks.contains(pack);
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
				if (loadedPack.getPackFile().equals(file)) {
					packsOfFile.add(loadedPack);
				}
			}
		}
		return packsOfFile;
	}
	
	// saves all packs to XMLs // convenience method
	public void saveAllPacks() {
		for (Pack pack : this.loadedPacks) {
			if (pack != null) { // failsafe check
				pack.save();
			}
		}
	}
	
	public boolean addPack(Pack pack) {
		if (pack != null && !this.loadedPacks.contains(pack)) {
			return this.loadedPacks.add(pack);
		}
		return false;
	}
	
	public boolean removePack(Pack pack) {
		if (pack != null) {
			return this.loadedPacks.remove(pack);
		}
		return false;
	}
	
	public void unloadAll() {
		this.loadedPacks.clear();
		LoggingHandler.getLog().finest("All packs have been unloaded");
	}
	
	// load a specific pack from a pack file // boolean: load even if it's an empty pack or not?
	public boolean loadPack(File file, Class<? extends Pack> packClass, boolean loadEmptyPack) {
		if (file != null && packClass != null) {
			try { // get the appropriate constructor
				Constructor<? extends Pack> packConstructor = packClass.getConstructor(File.class);
				try {
					Pack pack = packConstructor.newInstance(file);
					if (loadEmptyPack || pack.getEntryCount() > 0) {
						System.out.println("PackClass: " + pack.getClass().getName());
						this.loadedPacks.add(pack); // add a instance of this pack to the list of loaded packs
						LoggingHandler.getLog().fine(packClass.getName() + " loaded from " + file);
						return true;
					}
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					LoggingHandler.getLog().log(Level.SEVERE, "Dynamic pack creation failed for " + file + "::" + packClass.getName(), e);
					e.printStackTrace();
				}
			} catch (NoSuchMethodException | SecurityException e) {
				LoggingHandler.getLog().log(Level.SEVERE, "Dynamic pack creation failed for " + file + "::" + packClass.getName(), e);
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public boolean loadPack(File file, String className, boolean loadEmptyPack) {
		try {
			@SuppressWarnings("unchecked")
			Class<? extends Pack> packClass = (Class<? extends Pack>) Class.forName(className);
			return loadPack(file, packClass, loadEmptyPack);
		} catch (Exception e) {
			LoggingHandler.getLog().log(Level.SEVERE, "Dynamic pack creation failed for " + file + "::" + className, e);
			e.printStackTrace();
		}
		return false;
	}
	
}
