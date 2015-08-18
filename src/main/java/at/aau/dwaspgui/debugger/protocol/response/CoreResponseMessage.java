package at.aau.dwaspgui.debugger.protocol.response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A message from DWASP containing the debug atoms currently inside the core.
 * @author Philip Gasteiger
 */
public class CoreResponseMessage extends ResponseMessage {
	/** the identifier of this message type */
	public static final String MESSAGE_IDENTIFIER = "core";
	
	private final List<String> coreItems;
	
	public CoreResponseMessage(String message) {
		String unpacked = message.substring((MESSAGE_IDENTIFIER + DELIM_PART).length());
		
		if (unpacked.isEmpty())
			coreItems = new ArrayList<String>();
		else
			coreItems = Arrays.asList(getMessageParts(unpacked));
	}
	
	public List<String> getCoreItems() {
		return coreItems;
	}
}
