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

/**
 * Indicates an error during the parsing of a message.
 * @author Philip Gasteiger
 */
public class MessageParsingException extends Exception {
	private static final long serialVersionUID = 1L;

	public MessageParsingException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageParsingException(String message) {
		super(message);
	}

	public MessageParsingException(Throwable cause) {
		super(cause);
	}
}
