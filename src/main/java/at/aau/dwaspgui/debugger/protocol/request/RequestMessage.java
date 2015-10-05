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

package at.aau.dwaspgui.debugger.protocol.request;

import at.aau.dwaspgui.debugger.protocol.WritableMessage;

/**
 * A message to DWASP for requesting data.
 * @author Philip Gasteiger
 */
public class RequestMessage extends WritableMessage {
	/** the identifier of this message type */
	public static final String MESSAGE_IDENTIFIER = "get";
	
	/** the type of the request */
	private final RequestType requestType;

	/**
	 * Creates a new request message with the given type.
	 * @param request The type of the request.
	 */
	public RequestMessage(RequestType request) {
		this.requestType = request;
	}
	
	@Override
	protected String serialize() {
		StringBuilder message = new StringBuilder();
		
		message.append(MESSAGE_IDENTIFIER);
		message.append(DELIM_PART);
		message.append(requestType.getCommand());
		
		return message.toString();
	}
	
	/**
	 * Contains all requests.
	 * @author Philip Gasteiger
	 */
	public enum RequestType {
		GET_CORE("core")
	  , GET_HISTORY("history")
	  , GET_QUERY("query")
	  ;
		
		/** protocol text of the command */
		private final String command;
		
		RequestType (String command) {
			this.command = command;
		}
		
		/**
		 * Get the protocol text of the command.
		 * @return The protocol text.
		 */
		String getCommand() {
			return this.command;
		}
	}
}
