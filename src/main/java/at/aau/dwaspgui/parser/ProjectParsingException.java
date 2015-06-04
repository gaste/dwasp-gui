package at.aau.dwaspgui.parser;

/**
 * Signals that an error occurred during the parsing of the project.
 * 
 * @author Philip Gasteiger
 */
public class ProjectParsingException extends Exception {
	private static final long serialVersionUID = 1L;

	public ProjectParsingException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProjectParsingException(String message) {
		super(message);
	}
}
