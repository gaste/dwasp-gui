package at.aau.dwaspgui.domain;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Encoding of a logic program that is stored in a file.
 * 
 * @author Philip Gasteiger
 */
public class FileEncoding extends Encoding {
	/** The file that contains the encoding */
	private final File encodingFile;
	
	private final String relativePath;
	
	private String content = null;
	
	private BooleanProperty dirty = new SimpleBooleanProperty(false);

	public FileEncoding(String baseDirectory, String relativePath) {
		this.relativePath = relativePath;

		// check whether the encodingFile is already an absolute path
		File f = new File(relativePath);
		
		if (f.isAbsolute()) {
			this.encodingFile = f;
		} else {
			this.encodingFile = new File(baseDirectory, relativePath);
		}
	}
	
	public String getRelativePath() {
		return relativePath;
	}
	
	public String getAbsolutePath() {
		return encodingFile.getAbsolutePath();
	}
	
	public void save() throws IOException {
		if (!dirty.get()) return;
		
		Files.write(Paths.get(encodingFile.getAbsolutePath()), content.getBytes());
		dirty.set(false);
	}

	@Override
	public String toString() {
		return encodingFile.getName() + (dirty.get() ? "*" : "");
	}

	@Override
	public String getContent() {
		if (content == null) {
			try {
				content = new String(Files.readAllBytes(Paths.get(encodingFile.getAbsolutePath())));
			} catch (IOException e) {
				// TODO handle here
				content = "";
			}
		}
		
		return content;
	}
	
	public void setContent(String newContent) {
		if (content.equals(newContent))
			return;
		
		content = newContent;
		dirty.set(true);
	}
	
	public BooleanProperty dirtyProperty() { return dirty; }
	public boolean isDirty() { return dirty.get(); }
}
