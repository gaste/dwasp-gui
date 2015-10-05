/*
 *  Copyright 2015 Philip Gasteiger
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package at.aau.dwaspgui.debugger.protocol;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.aau.dwaspgui.debugger.protocol.info.InfoMessage;
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
	public static final Charset CHARSET = StandardCharsets.US_ASCII;
	
	/** delimiter of the parts of a single message */
	protected static final char DELIM_PART = ':';
	
	/** delimiter of a message */
	protected static final char DELIM_MSG = '\n';

	public static ReadableMessage parseFromInputStream(InputStreamReader inputStream) throws MessageParsingException {
		String message = readMessage(inputStream);
		
		if (message.startsWith(ResponseMessage.MESSAGE_IDENTIFIER))
			return ResponseMessage.parseFromString(message);
		
		if (message.startsWith(InfoMessage.MESSAGE_IDENTIFIER))
			return InfoMessage.parseFromString(message);
		
		log.error("Could not parse the message '{}' because the message type is unknown.", message);
		throw new MessageParsingException(Messages.MSGPARSER_INVALID_MESSAGE.format());
	}
	
	private static String readMessage(InputStreamReader reader) throws MessageParsingException {
		StringBuilder message = new StringBuilder();
		
		try {
			int c;
			while ((c = reader.read()) != -1 && c != DELIM_MSG) {
				if (c != '\r')
					message.append((char)c);
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
