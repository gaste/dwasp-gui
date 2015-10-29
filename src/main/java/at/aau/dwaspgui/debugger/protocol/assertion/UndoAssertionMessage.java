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

package at.aau.dwaspgui.debugger.protocol.assertion;

import at.aau.dwaspgui.debugger.protocol.WritableMessage;

/**
 * A message to DWASP for undoing a previous assertion.
 * @author Philip Gasteiger
 */
public class UndoAssertionMessage extends WritableMessage {
	/** the identifier of this message type */
	public static final String MESSAGE_IDENTIFIER = "undo";
	
	/** the atom to undo */
	private final String atom;
	
	public UndoAssertionMessage(String atom) {
		this.atom = atom;
	}
	
	@Override
	protected String serialize() {
		StringBuilder message = new StringBuilder();
		
		message.append(MESSAGE_IDENTIFIER);
		message.append(DELIM_PART);
		message.append(atom);
		
		return message.toString();
	}

}
