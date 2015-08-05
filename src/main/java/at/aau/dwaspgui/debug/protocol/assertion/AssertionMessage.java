package at.aau.dwaspgui.debug.protocol.assertion;

import java.util.Map;

import at.aau.dwaspgui.debug.protocol.WritableMessage;
import at.aau.dwaspgui.domain.QueryAnswer;

/**
 * A message to DWASP for asserting the truth value of atoms.
 * @author Philip Gasteiger
 */
public class AssertionMessage extends WritableMessage {
	/** the identifier of this message type */
	public static final String MESSAGE_IDENTIFIER = "assert";
	
	private static final char QUERY_ANSWER_YES = 'y';
	private static final char QUERY_ANSWER_NO = 'n';
	private static final char QUERY_ANSWER_UNDEFINED = 'u';
	private static final char DELIM_ANSWER = '=';
	
	private final Map<String, QueryAnswer> assertions;
	
	public AssertionMessage(Map<String, QueryAnswer> assertions) {
		this.assertions = assertions;
	}

	@Override
	protected String serialize() {
		StringBuilder message = new StringBuilder();
		
		message.append(MESSAGE_IDENTIFIER);
		
		if (assertions.isEmpty()) {
			message.append(DELIM_PART);
		}
		
		assertions.forEach((atom, answer) -> {
			message.append(DELIM_PART);
			message.append(atom);
			message.append(DELIM_ANSWER);
			message.append(getQueryAnswerEncoding(answer));
		});
		
		return message.toString();
	}
	
	private char getQueryAnswerEncoding(QueryAnswer answer) {
		switch (answer) {
		case YES:     return QUERY_ANSWER_YES;
		case NO:      return QUERY_ANSWER_NO;
		case UNKNOWN: return QUERY_ANSWER_UNDEFINED;
		default:      return QUERY_ANSWER_UNDEFINED;
		}
	}
}
