package at.aau.dwaspgui.debugger.protocol.info;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit tests for {@link ProgramCoherentInfoMessage}.
 * @author Philip Gasteiger
 */
@RunWith(JUnit4.class)
public class ProgramCoherentInfoMessageTest {
	@Test
	public void construct_emptyAnswerSet_returnsCorrect() {
		ProgramCoherentInfoMessage msg = new ProgramCoherentInfoMessage("coherent:");
		
		assertTrue(msg.getAnswerSet().isEmpty());
	}
	
	@Test
	public void construct_oneAtomInAnswerSet_returnsCorrect() {
		ProgramCoherentInfoMessage msg = new ProgramCoherentInfoMessage("coherent:atom1(a)");
		
		assertEquals(Arrays.asList("atom1(a)"), msg.getAnswerSet());
	}
	
	@Test
	public void construct_multipleAtomsInAnswerSet_returnsCorrect() {
		ProgramCoherentInfoMessage msg = new ProgramCoherentInfoMessage("coherent:atom1(a):atom2:atom3(a,b,c)");
		
		assertEquals(Arrays.asList("atom1(a)", "atom2", "atom3(a,b,c)"), msg.getAnswerSet());
	}
}
