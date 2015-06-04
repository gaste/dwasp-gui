package at.aau.dwaspgui.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit Tests for {@see Messages}
 *  
 * @author Philip Gasteiger
 */
@RunWith(JUnit4.class)
public class MessagesTest {
	@Test
	public void testResourceKeysFound() {
		for (Messages message : Messages.values()) {
			// will throw an exception if the resource key is not present
			message.format();
		}
	}
}
