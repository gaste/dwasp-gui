package at.aau.dwaspgui.debug.protocol.response;

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
