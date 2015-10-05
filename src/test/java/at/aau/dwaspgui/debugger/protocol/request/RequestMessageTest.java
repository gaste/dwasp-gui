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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import at.aau.dwaspgui.debugger.protocol.request.RequestMessage;
import at.aau.dwaspgui.debugger.protocol.request.RequestMessage.RequestType;

/**
 * Unit tests for {@link RequestMessage}.
 * @author Philip Gasteiger
 */
@RunWith(JUnit4.class)
public class RequestMessageTest {
	@Test
	public void serialize_getCore_returnsCorrect() {
		RequestMessage msg = new RequestMessage(RequestType.GET_CORE);
		
		String serialized = msg.serialize();
		
		assertEquals("get:core", serialized);
	}
	
	@Test
	public void serialize_getQuery_returnsCorrect() {
		RequestMessage msg = new RequestMessage(RequestType.GET_QUERY);
		
		String serialized = msg.serialize();
		
		assertEquals("get:query", serialized);
	}
	
	@Test
	public void serialize_getHistory_returnsCorrect() {
		RequestMessage msg = new RequestMessage(RequestType.GET_HISTORY);
		
		String serialized = msg.serialize();
		
		assertEquals("get:history", serialized);
	}
}
