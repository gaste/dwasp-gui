package at.aau.dwaspgui.debugger.protocol;

/**
 * Indicates an error during the parsing of a message.
 * @author Philip Gasteiger
 */
public class MessageParsingException extends Exception {
	private static final long serialVersionUID = 1L;

	public MessageParsingException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageParsingException(String message) {
		super(message);
	}

	public MessageParsingException(Throwable cause) {
		super(cause);
	}
}
