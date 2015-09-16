package at.aau.dwaspgui.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Enum class that contains all messages.
 * 
 * @author Philip Gasteiger
 */
public enum Messages {
	PRJPARSER_PROJECT_NOT_FOUND("prjparser.projectfile.notfound")
  , PRJPARSER_INVALIDXML("prjparser.projectfile.invalidxml")
  , PRJPARSER_IOERROR("prjparser.ioerror")
  , PRJPARSER_NO_TESTCASES("prjparser.notestcases")
  , PRJPARSER_NO_ENCODINGS("prjparser.noencodings")
  , PRJPARSER_NO_BASEDIRECTORY("prjparser.nobasedirectory")
  , PRJPARSER_NO_DIRECT_ENCODING_ATTRIBUTES("prjparser.nodirectencodingattributes")
  , ERROR_OPEN_PROJECT("error.openproject")
  , ERROR_GROUNDING("error.grounding")
  , ERROR_START_DEBUGGER("error.debugger.start")
  , OPENPRJ_FILE_NOT_FOUND("openprj.filenotfound")
  , MSGPARSER_INVALID_MESSAGE("msgparser.invalidmessage")
  , MSGPARSER_IOERROR("msgparser.ioerror")
  , ERROR_NEW_PROJECT("newproject.error")
  , ERROR_SAVE_PROJECT("saveproject.error")
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
