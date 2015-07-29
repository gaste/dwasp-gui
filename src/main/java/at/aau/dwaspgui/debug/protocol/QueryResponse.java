package at.aau.dwaspgui.debug.protocol;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class QueryResponse extends ResponseMessage {
	/** the identifier of this message type */
	public static final String MESSAGE_IDENTIFIER = ResponseMessage.MESSAGE_IDENTIFIER + DELIMITER_PART + "query";
	
	private final List<String> atoms;
	
	public QueryResponse(String message) {
		atoms = Arrays
				.asList(getMessageParts(message)).stream().filter((atom) -> !atom.isEmpty())
				.collect(Collectors.toList());
	}
	
	private String[] getMessageParts(String message) {
		String unpacked = message.substring((MESSAGE_IDENTIFIER + DELIMITER_PART).length());
		return Pattern.compile("" + DELIMITER_PART, Pattern.LITERAL).split(unpacked);
	}
	
	public List<String> getAtoms() {
		return atoms;
	}
}
