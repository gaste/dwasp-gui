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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A message from DWASP informing that the program is coherent.
 * @author Philip Gasteiger
 */
public class ProgramCoherentInfoMessage extends InfoMessage {
	/** the identifier of this message type */
	public static final String MESSAGE_IDENTIFIER = "coherent";
	
	/** the answer set */
	private final List<String> answerSet;
	
	public ProgramCoherentInfoMessage(String message) {
		String unpacked = message.substring((MESSAGE_IDENTIFIER + DELIM_PART).length());
		
		if (unpacked.isEmpty())
			answerSet = Collections.emptyList();
		else
			answerSet = Arrays.asList(getMessageParts(unpacked));
	}
	
	/**
	 * Get the answer set of the program.
	 * @return The answer set.
	 */
	public List<String> getAnswerSet() {
		return answerSet;
	}
}
