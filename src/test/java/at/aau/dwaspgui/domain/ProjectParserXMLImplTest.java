package at.aau.dwaspgui.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import at.aau.dwaspgui.parser.ProjectParsingException;
import at.aau.dwaspgui.parser.ProjectParserXMLImpl;

/**
 * Unit tests for {@see ProjectParserXMLImpl}.
 * 
 * @author Philip Gasteiger
 */
@RunWith(JUnit4.class)
public class ProjectParserXMLImplTest {
	private ProjectParserXMLImpl parser;
	
	@Before
	public void setUp() throws IOException {
		parser = new ProjectParserXMLImpl();
	}
	
	// -------------------------------------------------------------------------
	// parseProject tests
	// -------------------------------------------------------------------------
	@Test
	public void parseProject_projectValid_throwsException()
			throws ProjectParsingException {
		Project project = parser.parseProject(getClass().getResourceAsStream("projectValid.xml"));
		
		assertEquals("/foo/bar", project.getBaseDirectory());
		assertEquals(4, project.getProgram().size());
		assertEquals(3, project.getTestCases().size());
		
		int numFileEncodings = 0;
		int numDirectEncodings = 0;
		
		for (Encoding encoding : project.getProgram()) {
			if (encoding instanceof DirectEncoding)
				numDirectEncodings ++;
			else if (encoding instanceof FileEncoding)
				numFileEncodings ++;
			else
				fail("Found encoding that is neither DirectEncoding nor FileEncoding");
		}
		
		assertEquals(2, numDirectEncodings);
		assertEquals(2, numFileEncodings);
	}
	
	@Test(expected=ProjectParsingException.class)
	public void parseProject_projectNoBasedirectory_throwsException()
			throws ProjectParsingException {
		parser.parseProject(getClass().getResourceAsStream("projectNoBasedirectory.xml"));
	}
	
	@Test(expected=ProjectParsingException.class)
	public void parseProject_projectNoEncodings_throwsException()
			throws ProjectParsingException {
		parser.parseProject(getClass().getResourceAsStream("projectNoEncodings.xml"));
	}
	
	@Test(expected=ProjectParsingException.class)
	public void parseProject_projectNoInstances_throwsException()
			throws ProjectParsingException {
		parser.parseProject(getClass().getResourceAsStream("projectNoTestcases.xml"));
	}
}
