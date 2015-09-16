package at.aau.dwaspgui.debugger.protocol.info;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A message from DWASP informing that the program is coherent.
 * @author Philip Gasteiger
 */
public class ProgramCoherentInfoMessage extends InfoMessage {
	/** the identifier of this message type */
	public static final String MESSAGE_IDENTIFIER = "coherent";
	
	/** the answer set */
	private final List<String> answerSet;
	
	public ProgramCoherentInfoMessage(String message) {
		String unpacked = message.substring((MESSAGE_IDENTIFIER + DELIM_PART).length());
		
		if (unpacked.isEmpty())
			answerSet = Collections.emptyList();
		else
			answerSet = Arrays.asList(getMessageParts(unpacked));
	}
	
	/**
	 * Get the answer set of the program.
	 * @return The answer set.
	 */
	public List<String> getAnswerSet() {
		return answerSet;
	}
}
