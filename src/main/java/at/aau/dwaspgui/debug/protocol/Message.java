package at.aau.dwaspgui.debug.protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import at.aau.dwaspgui.util.Messages;

/**
 * A message from/to dwasp.
 * @author Philip Gasteiger
 */
public abstract class Message {
	/** character set of all messages */ 
	private static final Charset CHARSET = StandardCharsets.US_ASCII;
	
	/** delimiter of the parts of a single message */
	protected static final char DELIMITER_PART = ':';
	
	/** delimiter of a message */
	protected static final char DELIMITER_MSG = '\n';
	
	public static Message parseFromInputStream(InputStream inputStream) throws MessageParsingException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, CHARSET));
		StringBuilder messageBuilder = new StringBuilder();
		
		try {
			char c;
			while ((c = (char)reader.read()) != -1 && c != DELIMITER_MSG) {
				messageBuilder.append(c);
			}
		} catch (IOException e) {
			throw new MessageParsingException(Messages.MSGPARSER_IOERROR.format(), e);
		}
		
		String message = messageBuilder.toString();
		
		if (message.startsWith(ResponseMessage.MESSAGE_IDENTIFIER)) {
			return ResponseMessage.parseFromString(message);
		} else {
			throw new MessageParsingException(Messages.MSGPARSER_INVALID_MESSAGE.format());
		}
	}
	
	public void writeToOutputStream(OutputStream outputStream) throws IOException {
		OutputStreamWriter writer  = new OutputStreamWriter(outputStream, CHARSET);

		writer.write(serialize());
		writer.write(DELIMITER_MSG);
		writer.flush();
	}
	
	protected abstract String serialize();
}
