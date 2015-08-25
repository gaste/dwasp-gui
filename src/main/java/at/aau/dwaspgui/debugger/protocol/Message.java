package at.aau.dwaspgui.debugger.protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.aau.dwaspgui.debugger.protocol.response.ResponseMessage;
import at.aau.dwaspgui.util.Messages;

/**
 * A message from/to DWASP.
 * @author Philip Gasteiger
 */
public abstract class Message {
	/** logger instance */
	private static final Logger log = LoggerFactory.getLogger(Message.class);
	
	/** character set of all messages */ 
	protected static final Charset CHARSET = StandardCharsets.US_ASCII;
	
	/** delimiter of the parts of a single message */
	protected static final char DELIM_PART = ':';
	
	/** delimiter of a message */
	protected static final char DELIM_MSG = '\n';

	public static ResponseMessage parseFromInputStream(InputStream inputStream) throws MessageParsingException {
		String message = readMessage(inputStream);
		
		if (message.startsWith(ResponseMessage.MESSAGE_IDENTIFIER))
			return ResponseMessage.parseFromString(message);
		
		log.error("Could not parse the message '{}' because the message type is unknown.", message);
		throw new MessageParsingException(Messages.MSGPARSER_INVALID_MESSAGE.format());
	}
	
	private static String readMessage(InputStream inputStream) throws MessageParsingException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, CHARSET));
		StringBuilder message = new StringBuilder();
		
		try {
			char c;
			while ((c = (char)reader.read()) != -1 && c != DELIM_MSG) {
				if (c != '\r')
					message.append(c);
				else
					log.warn("Read '\\r' character from the input stream");
			}
		} catch (IOException e) {
			log.error("Could not read the message from the debugger.", e);
			throw new MessageParsingException(Messages.MSGPARSER_IOERROR.format(), e);
		}
		
		return message.toString();
	}
}
