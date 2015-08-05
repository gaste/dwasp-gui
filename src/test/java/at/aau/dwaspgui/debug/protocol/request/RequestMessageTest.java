package at.aau.dwaspgui.debug.protocol.request;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import at.aau.dwaspgui.debug.protocol.request.RequestMessage.RequestType;

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
