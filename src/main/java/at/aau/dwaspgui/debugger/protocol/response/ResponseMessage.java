package at.aau.dwaspgui.debugger.protocol.response;

import java.util.regex.Pattern;

import at.aau.dwaspgui.debugger.protocol.Message;
import at.aau.dwaspgui.debugger.protocol.MessageParsingException;
import at.aau.dwaspgui.util.Messages;

/**
 * A response message from DWASP.
 * 
 * @author Philip Gasteiger
 */
public class ResponseMessage extends Message {
	/** the identifier of this message type */
	public static final String MESSAGE_IDENTIFIER = "response";
	
	/** pattern that matches the part delimiter */
	private static final Pattern PATTERN_PART = Pattern.compile("" + DELIM_PART, Pattern.LITERAL);

	/**
	 * Parse the given message.
	 * @param message The message to parse.
	 * @return The parsed message.
	 * @throws MessageParsingException If the message is not a valid response
	 *                                 message.
	 */
	public static ResponseMessage parseFromString(String message) throws MessageParsingException {
		if (!message.startsWith(MESSAGE_IDENTIFIER + DELIM_PART))
			throw new MessageParsingException(Messages.MSGPARSER_INVALID_MESSAGE.format());
		
		String unpacked = message.substring((MESSAGE_IDENTIFIER + DELIM_PART).length());
		
		if (unpacked.startsWith(CoreResponseMessage.MESSAGE_IDENTIFIER))
			return new CoreResponseMessage(unpacked);

		if (unpacked.startsWith(QueryResponseMessage.MESSAGE_IDENTIFIER))
			return new QueryResponseMessage(unpacked);
		
		throw new MessageParsingException(Messages.MSGPARSER_INVALID_MESSAGE.format());
	}
	
	/**
	 * Get the parts of the given message.
	 * @param message The message.
	 * @return An array containing the parts of the message.
	 */
	protected String[] getMessageParts(String message) {
		return PATTERN_PART.split(message);
	}
}
