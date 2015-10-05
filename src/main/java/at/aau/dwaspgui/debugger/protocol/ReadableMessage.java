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

import java.util.regex.Pattern;

/**
 * A message that can be read from DWASP.
 * @author Philip Gasteiger
 */
public abstract class ReadableMessage extends Message {
	/** pattern that matches the part delimiter */
	private static final Pattern PATTERN_PART = Pattern.compile("" + DELIM_PART, Pattern.LITERAL);
	
	/**
	 * Get the parts of the given message.
	 * @param message The message.
	 * @return An array containing the parts of the message.
	 */
	protected String[] getMessageParts(String message) {
		return PATTERN_PART.split(message);
	}
}
