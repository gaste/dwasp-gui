package at.aau.dwaspgui.serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

import org.custommonkey.xmlunit.XMLTestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.xml.sax.SAXException;

import at.aau.dwaspgui.domain.DirectEncoding;
import at.aau.dwaspgui.domain.FileEncoding;
import at.aau.dwaspgui.domain.Project;
import at.aau.dwaspgui.domain.TestCase;

/**
 * Unit tests for {@link ProjectSerializerXMLImpl}.
 * @author Philip Gasteiger
 */
@RunWith(JUnit4.class)
public class ProjectSerializerXMLImplTest extends XMLTestCase {
	private ProjectSerializerXMLImpl serializer;
	
	@Before
	public void setUp() {
		serializer = new ProjectSerializerXMLImpl();
	}
	
	private String getActual(String filename) throws IOException, URISyntaxException {
		URI resourceUri = this.getClass().getResource(filename).toURI();
		return new String(Files.readAllBytes(Paths.get(resourceUri)));
	}
	
	private void testSerialize(Project project, String filename) throws ProjectSerializationException, SAXException, IOException, URISyntaxException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		
		serializer.serialize(project, result);
		assertXMLEqual(getActual(filename), result.toString());
	}
	
	// -------------------------------------------------------------------------
	// parseProject tests
	// -------------------------------------------------------------------------
	@Test
	public void serialize_emptyProject_serializesCorrect() throws ProjectSerializationException, SAXException, IOException, URISyntaxException {
		Project project = new Project("/path/to/base", Collections.emptyList(), Collections.emptyList());
		testSerialize(project, "emptyProject.xml");
	}
	
	@Test
	public void serialize_twoEncodingsNoTestcases_serializesCorrect() throws ProjectSerializationException, SAXException, IOException, URISyntaxException {
		Project project = new Project(
				"/path/to/base",
				Arrays.asList(
						new FileEncoding("/path/to/base", "file1.lp"),
						new DirectEncoding("a :- b. b : - c. c.", "/path/to/sourcefile", 1, 2, 3, 4)),
				Collections.emptyList());
		
		testSerialize(project, "twoEncodingsProject.xml");
	}
	
	@Test
	public void serialize_noEncodingsTwoTestcases_serializesCorrect() throws ProjectSerializationException, SAXException, IOException, URISyntaxException {
		Project project = new Project(
				"/path/to/base",
				Collections.emptyList(),
				Arrays.asList(
						new TestCase("Test Case 1", "assertTrue(a)."),
						new TestCase("Test Case 2", "assertFalse(b).")));
		
		testSerialize(project, "twoTestcasesProject.xml");
	}
}
