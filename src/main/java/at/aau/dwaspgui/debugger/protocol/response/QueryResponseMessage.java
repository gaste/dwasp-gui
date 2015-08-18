package at.aau.dwaspgui.debugger.protocol.response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A message from DWASP containing the atoms for a query.
 * @author Philip Gasteiger
 */
public class QueryResponseMessage extends ResponseMessage {
	/** the identifier of this message type */
	public static final String MESSAGE_IDENTIFIER = "query";
	
	private final List<String> atoms;
	
	public QueryResponseMessage(String message) {
		String unpacked = message.substring((MESSAGE_IDENTIFIER + DELIM_PART).length());
		
		if (unpacked.isEmpty())
			atoms = new ArrayList<String>();
		else 
			atoms = Arrays.asList(getMessageParts(unpacked));
	}
	
	public List<String> getAtoms() {
		return atoms;
	}
}
