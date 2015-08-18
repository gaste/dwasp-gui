package at.aau.dwaspgui.debugger;

/**
 * Indicates that debugging the logic program failed.
 * @author Philip Gasteiger
 */
public class DebuggerException extends Exception {
	private static final long serialVersionUID = 1L;

	public DebuggerException(String message, Throwable cause) {
		super(message, cause);
	}

	public DebuggerException(String message) {
		super(message);
	}

	public DebuggerException(Throwable cause) {
		super(cause);
	}
}
