package at.aau.dwaspgui.domain;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Encoding of a logic program that is stored in a file.
 * @author Philip Gasteiger
 */
public class FileEncoding extends Encoding {
	/** Logger instance */
	private static final Logger log = LoggerFactory.getLogger(FileEncoding.class);
	
	/** The file that contains the encoding */
	private final File encodingFile;
	
	/** Relative path of the file in the project */
	private final String relativePath;
	
	/** Transient content of the file */
	private transient String content = null;
	
	/** Flag wether there are unsaved changes */
	private BooleanProperty dirty = new SimpleBooleanProperty(false);

	/**
	 * Instantiate a new file encoding from an existing file.
	 * @param baseDirectory The base directory.
	 * @param relativePath The path of the file relative to the base directory.
	 */
	public FileEncoding(String baseDirectory, String relativePath) {
		this.relativePath = relativePath;

		// check whether the encodingFile is already an absolute path
		File f = new File(relativePath);
		
		if (f.isAbsolute()) {
			this.encodingFile = f;
			log.warn("Relative path '{}' is absolute. The base directory '{}' is ignored", relativePath, baseDirectory);
		} else {
			this.encodingFile = new File(baseDirectory, relativePath);
		}
	}
	
	public final String getRelativePath() {
		return relativePath;
	}
	
	public final String getAbsolutePath() {
		return encodingFile.getAbsolutePath();
	}
	
	public void save() throws IOException {
		if (!dirty.get()) return;
		
		Files.write(Paths.get(encodingFile.getAbsolutePath()), content.getBytes());
		dirty.set(false);
	}

	@Override
	public String toString() {
		return encodingFile.getName() + (dirty.get() ? " \u25CF" : "");
	}

	@Override
	public String getContent() {
		if (content == null) {
			try {
				content = new String(Files.readAllBytes(Paths.get(encodingFile.getAbsolutePath())));
			} catch (IOException e) {
				log.error("Could not open the encoding file '{}'.", encodingFile.getAbsolutePath(), e);
				// TODO handle here
				content = "";
			}
		}
		
		return content;
	}
	
	public final void setContent(String newContent) {
		if (content.equals(newContent))
			return;
		
		content = newContent;
		dirty.set(true);
	}
	
	public BooleanProperty dirtyProperty() { return dirty; }
	public final boolean isDirty() { return dirty.get(); }
}
