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
