package at.aau.dwaspgui.debug.protocol;

import at.aau.dwaspgui.util.Messages;

public class ResponseMessage extends Message {
	/** the identifier of this message type */
	public static final String MESSAGE_IDENTIFIER = "response";

	@Override
	protected String serialize() {
		return null;
	}
	
	protected static ResponseMessage parseFromString(String message)
			throws MessageParsingException {
		if (!message.startsWith(MESSAGE_IDENTIFIER))
			throw new MessageParsingException(Messages.MSGPARSER_INVALID_MESSAGE.format());
		
		if (message.startsWith(CoreResponse.MESSAGE_IDENTIFIER))
			return new CoreResponse(message);
		else if (message.startsWith(QueryResponse.MESSAGE_IDENTIFIER))
			return new QueryResponse(message);
		else
			throw new MessageParsingException(Messages.MSGPARSER_INVALID_MESSAGE.format());
	}
}
