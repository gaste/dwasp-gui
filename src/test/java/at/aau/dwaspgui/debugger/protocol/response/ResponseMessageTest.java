package at.aau.dwaspgui.debugger.protocol.response;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import at.aau.dwaspgui.debugger.protocol.MessageParsingException;
import at.aau.dwaspgui.debugger.protocol.response.CoreResponseMessage;
import at.aau.dwaspgui.debugger.protocol.response.QueryResponseMessage;
import at.aau.dwaspgui.debugger.protocol.response.ResponseMessage;

/**
 * Unit tests for {@link ResponseMessage}.
 * @author Philip Gasteiger
 */
@RunWith(JUnit4.class)
public class ResponseMessageTest {
	@Test(expected = MessageParsingException.class)
	public void parseFromString_invalidMessage_throwsException() throws MessageParsingException {
		ResponseMessage.parseFromString("asf");
	}
	
	@Test(expected = MessageParsingException.class)
	public void parseFromString_noResponseType_throwsException() throws MessageParsingException {
		ResponseMessage.parseFromString("response:");
	}
	
	@Test(expected = MessageParsingException.class)
	public void parseFromString_invalidResponseType_throwsException() throws MessageParsingException {
		ResponseMessage.parseFromString("response:asdf");
	}
	
	@Test
	public void parseFromString_queryResponse_returnsCorrect() throws MessageParsingException {
		ResponseMessage msg = ResponseMessage.parseFromString("response:query:");
		
		assertThat(msg, instanceOf(QueryResponseMessage.class));
	}
	
	@Test
	public void parseFromString_coreResponse_returnsCorrect() throws MessageParsingException {
		ResponseMessage msg = ResponseMessage.parseFromString("response:core:");
		
		assertThat(msg, instanceOf(CoreResponseMessage.class));
	}
}
