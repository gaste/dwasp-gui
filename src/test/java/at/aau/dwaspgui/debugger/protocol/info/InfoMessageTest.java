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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import at.aau.dwaspgui.debugger.protocol.MessageParsingException;

/**
 * Unit tests for {@link InfoMessage}.
 * @author Philip Gasteiger
 */
@RunWith(JUnit4.class)
public class InfoMessageTest {
	@Test(expected = MessageParsingException.class)
	public void parseFromString_invalidMessage_throwsException() throws MessageParsingException {
		InfoMessage.parseFromString("asf");
	}
	
	@Test(expected = MessageParsingException.class)
	public void parseFromString_noInfoType_throwsException() throws MessageParsingException {
		InfoMessage.parseFromString("info:");
	}
	
	@Test(expected = MessageParsingException.class)
	public void parseFromString_invalidInfoType_throwsException() throws MessageParsingException {
		InfoMessage.parseFromString("info:asdf");
	}
	
	@Test
	public void parseFromString_infoCoherent_returnsCorrect() throws MessageParsingException {
		InfoMessage msg = InfoMessage.parseFromString("info:coherent:");
		
		assertThat(msg, instanceOf(ProgramCoherentInfoMessage.class));
	}
}
