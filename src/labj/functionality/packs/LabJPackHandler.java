package labj.functionality.packs;

import java.io.File;

import labjframework.logging.LoggingHandler;
import labjframework.packs.PackHandler;

public class LabJPackHandler extends PackHandler {

	// loads all possible entries from a XML
	public void loadPack(File file) {
		if (file != null) {
			this.loadSubstancePack(file); // load substance file
			this.loadOrganismPack(file); // load organism file
			// TODO: load anything else
		} else {
			LoggingHandler.getLog().warning("Cannot load a null file");
		}
	}
	
	// loads a substance pack from a XML
	public void loadSubstancePack(File file) {
		if (file != null) {
			SubstancePack packToAdd = new SubstancePack(file);
			if (!this.hasPack(packToAdd)) {
				this.loadedPacks.add(packToAdd);
			} else {
				LoggingHandler.getLog().warning("The pack " + packToAdd + " is already loaded.");
			}
		} else {
			LoggingHandler.getLog().warning("Cannot load a null file");
		}
	}
	
	// loads a substance pack from a XML
	public void loadOrganismPack(File file) {
		if (file != null) {
			OrganismPack packToAdd = new OrganismPack(file);
			if (!this.hasPack(packToAdd)) {
				this.loadedPacks.add(packToAdd);
			} else {
				LoggingHandler.getLog().warning("The pack " + packToAdd + " is already loaded.");
			}
		} else {
			LoggingHandler.getLog().warning("Cannot load a null file");
		}
	}
}
