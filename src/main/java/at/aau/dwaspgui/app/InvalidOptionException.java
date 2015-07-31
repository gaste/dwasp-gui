package at.aau.dwaspgui.app;

/**
 * Exception gets thrown when invalid command line options are passed to the
 * program.
 * 
 * @author Philip Gasteiger
 *
 */
public class InvalidOptionException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidOptionException(String message) {
		super(message);
	}
}
