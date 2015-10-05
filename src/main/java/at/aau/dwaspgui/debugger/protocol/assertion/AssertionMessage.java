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

import java.util.Map;

import at.aau.dwaspgui.debugger.protocol.WritableMessage;
import at.aau.dwaspgui.domain.QueryAnswer;

/**
 * A message to DWASP for asserting the truth value of atoms.
 * @author Philip Gasteiger
 */
public class AssertionMessage extends WritableMessage {
	/** the identifier of this message type */
	public static final String MESSAGE_IDENTIFIER = "assert";
	
	private static final char QUERY_ANSWER_YES = 'y';
	private static final char QUERY_ANSWER_NO = 'n';
	private static final char QUERY_ANSWER_UNDEFINED = 'u';
	private static final char DELIM_ANSWER = '=';
	
	private final Map<String, QueryAnswer> assertions;
	
	public AssertionMessage(Map<String, QueryAnswer> assertions) {
		this.assertions = assertions;
	}

	@Override
	protected String serialize() {
		StringBuilder message = new StringBuilder();
		
		message.append(MESSAGE_IDENTIFIER);
		
		if (assertions.isEmpty()) {
			message.append(DELIM_PART);
		}
		
		assertions.forEach((atom, answer) -> {
			message.append(DELIM_PART);
			message.append(atom);
			message.append(DELIM_ANSWER);
			message.append(getQueryAnswerEncoding(answer));
		});
		
		return message.toString();
	}
	
	private char getQueryAnswerEncoding(QueryAnswer answer) {
		switch (answer) {
		case YES:     return QUERY_ANSWER_YES;
		case NO:      return QUERY_ANSWER_NO;
		case UNKNOWN: return QUERY_ANSWER_UNDEFINED;
		default:      return QUERY_ANSWER_UNDEFINED;
		}
	}
}
