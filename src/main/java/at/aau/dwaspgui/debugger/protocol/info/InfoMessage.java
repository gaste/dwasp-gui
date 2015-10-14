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
		
		if (unpacked.startsWith(ComputingCoreInfoMessage.MESSAGE_IDENTIFIER))
			return new ComputingCoreInfoMessage();
		
		if (unpacked.startsWith(ComputingQueryInfoMessage.MESSAGE_IDENTIFIER))
			return new ComputingQueryInfoMessage();
		
		log.error("Could not parse the message '{}' because the message type is unkown.", message);
		throw new MessageParsingException(Messages.MSGPARSER_INVALID_MESSAGE.format());
	}
}
