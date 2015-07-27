package at.aau.dwaspgui.debug.protocol;

import java.util.Map;

import at.aau.dwaspgui.domain.QueryAnswer;


public class AssertionMessage extends Message {
	/** the identifier of this message type */
	public static final String MESSAGE_IDENTIFIER = "assert";
	
	private static final char QUERY_ANSWER_YES = 'y';
	private static final char QUERY_ANSWER_NO = 'n';
	private static final char QUERY_ANSWER_UNDEFINED = 'u';
	
	private Map<String, QueryAnswer> assertions;
	
	public AssertionMessage(Map<String, QueryAnswer> assertions) {
		this.assertions = assertions;
	}

	@Override
	protected String serialize() {
		StringBuilder msg = new StringBuilder();
		
		msg.append(MESSAGE_IDENTIFIER);
		
		assertions.forEach((atom, answer) -> {
			msg.append(DELIMITER_PART);
			msg.append(atom);
			msg.append('=');
			msg.append(getQueryAnswerEncoding(answer));
		});
		
		return msg.toString();
	}
	
	private char getQueryAnswerEncoding(QueryAnswer answer) {
		char ans = QUERY_ANSWER_YES;
		switch (answer) {
		case YES: ans = QUERY_ANSWER_YES; break;
		case NO: ans = QUERY_ANSWER_NO; break;
		case UNKNOWN: ans = QUERY_ANSWER_UNDEFINED; break;
		}
		return ans;
	}
}
