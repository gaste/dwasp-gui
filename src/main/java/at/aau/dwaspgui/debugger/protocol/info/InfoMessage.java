package at.aau.dwaspgui.debugger.protocol.info;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.aau.dwaspgui.debugger.protocol.MessageParsingException;
import at.aau.dwaspgui.debugger.protocol.ReadableMessage;
import at.aau.dwaspgui.util.Messages;

/**
 * A information message from the debugger.
 * @author Philip Gasteiger
 */
public class InfoMessage extends ReadableMessage {
	/** logger instance */
	private static final Logger log = LoggerFactory.getLogger(InfoMessage.class);
	
	/** the identifier of this message type */
	public static final String MESSAGE_IDENTIFIER = "info";

	/**
	 * Parse the given message.
	 * @param message The message to parse.
	 * @return The parsed message.
	 * @throws MessageParsingException If the message is not a valid response
	 *                                 message.
	 */
	public static InfoMessage parseFromString(String message) throws MessageParsingException {
		if (!message.startsWith(MESSAGE_IDENTIFIER + DELIM_PART))
			throw new MessageParsingException(Messages.MSGPARSER_INVALID_MESSAGE.format());
		
		String unpacked = message.substring((MESSAGE_IDENTIFIER + DELIM_PART).length());
		
		if (unpacked.startsWith(ProgramCoherentInfoMessage.MESSAGE_IDENTIFIER))
			return new ProgramCoherentInfoMessage(unpacked);
		
		log.error("Could not parse the message '{}' because the message type is unkown.", message);
		throw new MessageParsingException(Messages.MSGPARSER_INVALID_MESSAGE.format());
	}
}
