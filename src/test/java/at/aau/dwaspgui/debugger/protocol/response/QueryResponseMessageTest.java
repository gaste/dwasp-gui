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

package at.aau.dwaspgui.debugger.protocol.response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import at.aau.dwaspgui.debugger.protocol.response.QueryResponseMessage;

/**
 * Unit tests for {@link QueryResponseMessage}.
 * @author Philip Gasteiger
 */
@RunWith(JUnit4.class)
public class QueryResponseMessageTest {
	@Test
	public void construct_noQueries_returnsCorrect() {
		QueryResponseMessage msg = new QueryResponseMessage("query:");
		
		assertTrue(msg.getAtoms().isEmpty());
	}
	
	@Test
	public void construct_oneQuery_returnsCorrect() {
		QueryResponseMessage msg = new QueryResponseMessage("query:atom1(a)");
		
		assertEquals(Arrays.asList("atom1(a)"), msg.getAtoms());
	}
	
	@Test
	public void construct_multipleQueries_returnsCorrect() {
		QueryResponseMessage msg = new QueryResponseMessage("query:atom1(a):atom2:atom3(a,b,c)");
		
		assertEquals(Arrays.asList("atom1(a)", "atom2", "atom3(a,b,c)"), msg.getAtoms());
	}
}
