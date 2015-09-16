package at.aau.dwaspgui.debugger.protocol;

import java.util.regex.Pattern;

/**
 * A message that can be read from DWASP.
 * @author Philip Gasteiger
 */
public abstract class ReadableMessage extends Message {
	/** pattern that matches the part delimiter */
	private static final Pattern PATTERN_PART = Pattern.compile("" + DELIM_PART, Pattern.LITERAL);
	
	/**
	 * Get the parts of the given message.
	 * @param message The message.
	 * @return An array containing the parts of the message.
	 */
	protected String[] getMessageParts(String message) {
		return PATTERN_PART.split(message);
	}
}
