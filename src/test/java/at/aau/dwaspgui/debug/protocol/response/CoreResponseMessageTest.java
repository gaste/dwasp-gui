package at.aau.dwaspgui.debug.protocol.response;

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
		
		assertTrue(msg.getCoreItems().isEmpty());
	}
	
	@Test
	public void construct_oneCoreItem_returnsCorrect() {
		CoreResponseMessage msg = new CoreResponseMessage("core:_debug1(a)");
		
		assertEquals(Arrays.asList("_debug1(a)"), msg.getCoreItems());
	}
	
	@Test
	public void construct_multipleCoreItems_returnsCorrect() {
		CoreResponseMessage msg = new CoreResponseMessage("core:_debug1(a):_debug2(a,b):_debug3");
		
		assertEquals(Arrays.asList("_debug1(a)", "_debug2(a,b)", "_debug3"), msg.getCoreItems());
	}
}
