package at.aau.dwaspgui.domain;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Encoding of a logic program that is stored in a file.
 * 
 * @author Philip Gasteiger
 */
public class FileEncoding extends Encoding {
	/** The file that contains the encoding */
	private File encodingFile;

	public FileEncoding(String baseDirectory, String encodingFile) {
		this.encodingFile = new File(baseDirectory, encodingFile);
	}
	
	public String getFilename() {
		return encodingFile.getAbsolutePath();
	}

	@Override
	public String toString() {
		return encodingFile.getName();
	}

	@Override
	public String getContent() {
		try {
			return new String(Files.readAllBytes(Paths.get(encodingFile.getAbsolutePath())));
		} catch (IOException e) {
			// TODO handle here
			return "";
		}
	}
}
