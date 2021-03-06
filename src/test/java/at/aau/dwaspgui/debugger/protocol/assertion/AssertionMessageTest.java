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

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import at.aau.dwaspgui.debugger.protocol.assertion.AssertionMessage;
import at.aau.dwaspgui.domain.QueryAnswer;

/**
 * Unit tests for {@link AssertionMessage}.
 * @author Philip Gasteiger
 */
@RunWith(JUnit4.class)
public class AssertionMessageTest {
	@Test
	public void serialize_emptyMap_returnsCorrect() {
		AssertionMessage msg = new AssertionMessage(
				new HashMap<String, QueryAnswer>());

		String serialized = msg.serialize();

		assertEquals("assert:", serialized);
	}

	@Test
	public void serialize_answerYes_returnsCorrect() {
		Map<String, QueryAnswer> assertion = new HashMap<String, QueryAnswer>();
		assertion.put("atom", QueryAnswer.YES);
		AssertionMessage msg = new AssertionMessage(assertion);

		String serialized = msg.serialize();

		assertEquals("assert:atom=y", serialized);
	}

	@Test
	public void serialize_answerNo_returnsCorrect() {
		Map<String, QueryAnswer> assertion = new LinkedHashMap<String, QueryAnswer>();
		assertion.put("atom", QueryAnswer.NO);
		AssertionMessage msg = new AssertionMessage(assertion);

		String serialized = msg.serialize();

		assertEquals("assert:atom=n", serialized);
	}

	@Test
	public void serialize_answerUnknown_returnsCorrect() {
		Map<String, QueryAnswer> assertion = new LinkedHashMap<String, QueryAnswer>();
		assertion.put("atom", QueryAnswer.UNKNOWN);
		AssertionMessage msg = new AssertionMessage(assertion);

		String serialized = msg.serialize();

		assertEquals("assert:atom=u", serialized);
	}

	@Test
	public void serialize_multipleAssertions_returnsCorrect() {
		Map<String, QueryAnswer> assertion = new LinkedHashMap<String, QueryAnswer>();
		assertion.put("atom(a)", QueryAnswer.YES);
		assertion.put("atom(b)", QueryAnswer.NO);
		assertion.put("atom(c)", QueryAnswer.UNKNOWN);
		AssertionMessage msg = new AssertionMessage(assertion);

		String serialized = msg.serialize();

		assertEquals("assert:atom(a)=y:atom(b)=n:atom(c)=u", serialized);
	}
}
