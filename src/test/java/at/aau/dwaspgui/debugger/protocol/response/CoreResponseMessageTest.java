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

/**
 * Unit tests for {@link CoreResponseMessage}
 * @author Philip Gasteiger
 */
@RunWith(JUnit4.class)
public class CoreResponseMessageTest {
	@Test
	public void construct_emptyCore_returnsCorrect() {
		CoreResponseMessage msg = new CoreResponseMessage("core:");
		
		assertTrue(msg.getCoreDebugAtoms().isEmpty());
	}
	
	@Test
	public void construct_oneCoreItem_returnsCorrect() {
		CoreResponseMessage msg = new CoreResponseMessage("core:_debug1(a)");
		
		assertEquals(Arrays.asList("_debug1(a)"), msg.getCoreDebugAtoms());
	}
	
	@Test
	public void construct_multipleCoreItems_returnsCorrect() {
		CoreResponseMessage msg = new CoreResponseMessage("core:_debug1(a):_debug2(a,b):_debug3");
		
		assertEquals(Arrays.asList("_debug1(a)", "_debug2(a,b)", "_debug3"), msg.getCoreDebugAtoms());
	}
}
