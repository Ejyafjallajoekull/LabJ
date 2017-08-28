package labj.functionality.project;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import labj.functionality.housekeeping.configuration.ConfigurationHandler;
import labj.functionality.packs.OrganismPack;
import labj.functionality.packs.SubstancePack;
import labj.functionality.packs.TaxonomyPack;
import labjframework.logging.LoggingHandler;
import labjframework.packs.Pack;
import labjframework.packs.PackHandler;
import labjframework.utilities.ByteUtilities;

public class LabJProject extends PackHandler {
// a project file holding all informations on the current session
/* this does only summarise which packs present in the pack folder should be
 * checked for loading, actually data loading only occurs in the specified
 * PackHandler */
	
	// constants
	public static final byte[] VERSION = {1}; // the version byte for this file format
	public static final String HEADER = "LabJProject"; // the file header required for file recognition
	public static final String FILE_EXTENSION = "labj"; // the regular file extension for project files
	
	@SuppressWarnings("rawtypes")
	public static final Class[] packClasses = {SubstancePack.class, TaxonomyPack.class, OrganismPack.class};
	
//	private ArrayList<Pack> projectPacks = new ArrayList<Pack>(); // packs which were loaded into the project
	private File projectFile = null; // the file this project is stored in
	private Charset stringEncoding = StandardCharsets.UTF_8;
	
	// empty constructor
	public LabJProject() {
		
	}
	
	public LabJProject(File file) {
		this.projectFile = file;
	}
	
	public File getProjectFile() {
		return this.projectFile;
	}
	
	public void setProjectFile(File file) {
		this.projectFile = file;
	}
	
	// load data from this project file
	public boolean loadProject() {
		if (this.projectFile != null && this.projectFile.isFile()) {
			try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(this.projectFile))) {
				if (bis.available() >= HEADER.getBytes(this.stringEncoding).length + VERSION.length) { // read file only if enough bytes for header and version byte
					if (HEADER.equals(ByteUtilities.readString(bis, HEADER.getBytes(this.stringEncoding).length, this.stringEncoding))) { // only proceed if indeed a project file
						bis.skip(VERSION.length); // skip version byte
						if (bis.available() >= Integer.BYTES) { // check whether an int can actually be called
							int packNumber = ByteUtilities.readInteger(bis);
							for (int i = 0; i < packNumber; i++) {
								this.loadPack(ByteUtilities.readString(bis, ByteUtilities.readInteger(bis), this.stringEncoding), ByteUtilities.readString(bis, ByteUtilities.readInteger(bis), this.stringEncoding));
							}
						}
						return true;
					}  else {
						LoggingHandler.getLog().warning("The file " + this.projectFile + " is not a project file: missing header");
					}
				} else {
					LoggingHandler.getLog().warning("The file " + this.projectFile + " is not a project file: not enough bytes for header");
				}
			} catch (FileNotFoundException e) {
				LoggingHandler.getLog().log(Level.SEVERE, "Project file " + this.projectFile + " not found", e);
				e.printStackTrace();
			} catch (IOException e) {
				LoggingHandler.getLog().log(Level.SEVERE, "Cannot read project file " + this.projectFile, e);
				e.printStackTrace();
			}
		}
		return false;
	}
	
	// load data from a project file
	public boolean loadProject(File file) {
		this.projectFile = file;
		return this.loadProject();
	}
	
	// save the current project to the specified project file
	public boolean saveProject() {
		if (this.projectFile != null) {
			try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(this.projectFile))) { // auto close writer
				bos.write(HEADER.getBytes(this.stringEncoding));
				bos.write(VERSION);
				bos.write(ByteUtilities.integerToByte(this.loadedPacks.size()));
				for (Pack pack : this.loadedPacks) { // save packs as combination of pack class and pack file
					bos.write(ByteUtilities.stringLengthToByte(pack.getClass().getName(), this.stringEncoding));
					bos.write(pack.getClass().getName().getBytes(this.stringEncoding));
					bos.write(ByteUtilities.stringLengthToByte(pack.getPackFile().getName(), this.stringEncoding));
					bos.write(pack.getPackFile().getName().getBytes(this.stringEncoding));
				}
				LoggingHandler.getLog().info("Project saved to " + this.projectFile);
				return true;
			} catch (IOException e) {
				LoggingHandler.getLog().log(Level.SEVERE, "Cannot write to project file " + this.projectFile, e);
				e.printStackTrace();
			}
		}
		return false;
	}
	
	// save the current project to a file
	public boolean saveProject(File file) {
		if (file != null) {
			this.projectFile = file;
			return this.saveProject();
		}
		return false;
	}
	
	// load a pack from the specified file in the pack folder
	private boolean loadPack(String className, String fileName) {
		return this.loadPack(new File(ConfigurationHandler.getPackFolder(), fileName), className);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadPacks(File file) {
		if (packClasses != null) {
			for (Class<? extends Pack> packClass : packClasses) {
				this.loadPack(file, packClass);
			}
		}		
	}
	
}
