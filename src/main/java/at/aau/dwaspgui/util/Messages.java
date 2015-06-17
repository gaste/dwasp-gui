package at.aau.dwaspgui.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Enum class that contains all messages.
 * 
 * @author Philip Gasteiger
 */
public enum Messages {
	PARSER_PROJECT_NOT_FOUND("parser.projectfile.notfound")
  , PARSER_INVALIDXML("parser.projectfile.invalidxml")
  , PARSER_IOERROR("parser.ioerror")
  , PARSER_NO_TESTCASES("parser.notestcases")
  , PARSER_NO_ENCODINGS("parser.noencodings")
  , PARSER_NO_BASEDIRECTORY("parser.nobasedirectory")
  , ERROR_OPEN_PROJECT("error.openproject")
  ;

	private static final String BUNDLE_BASENAME = Messages.class.getCanonicalName();
	private static final ResourceBundle patterns = ResourceBundle.getBundle(BUNDLE_BASENAME);

	private final String patternKey;

	private Messages(String patternKey) {
		this.patternKey = patternKey;
	}

	/**
	 * Format the message with the given values for the place holders.
	 * 
	 * @param values
	 *            The values of the place holders.
	 * @return The formatted message
	 */
	public String format(Object... values) {
		return MessageFormat.format(patterns.getString(patternKey), values);
	}
}
