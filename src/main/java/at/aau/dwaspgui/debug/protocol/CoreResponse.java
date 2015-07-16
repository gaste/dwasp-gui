package at.aau.dwaspgui.debug.protocol;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CoreResponse extends ResponseMessage {
	/** the identifier of this message type */
	public static final String MESSAGE_IDENTIFIER = ResponseMessage.MESSAGE_IDENTIFIER + DELIMITER_PART + "core";
	
	private final List<String> coreItems;
	
	public CoreResponse(String message) {
		coreItems = Arrays.asList(getMessageParts(message));
	}
	
	private String[] getMessageParts(String message) {
		String unpacked = message.substring((MESSAGE_IDENTIFIER + DELIMITER_PART).length());
		return Pattern.compile("" + DELIMITER_PART, Pattern.LITERAL).split(unpacked);
	}
	
	public List<String> getCoreItems() {
		return coreItems;
	}
}
