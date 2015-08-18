package at.aau.dwaspgui.serializer;

/**
 * Signals that an error occurred while serializing a project.
 * @author Philip Gasteiger
 */
public class ProjectSerializationException extends Exception {
	private static final long serialVersionUID = 1L;

	public ProjectSerializationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProjectSerializationException(String message) {
		super(message);
	}
	
	public ProjectSerializationException(Throwable cause) {
		super(cause);
	}
}
