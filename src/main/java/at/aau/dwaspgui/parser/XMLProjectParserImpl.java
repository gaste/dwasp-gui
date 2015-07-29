package at.aau.dwaspgui.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import at.aau.dwaspgui.domain.DirectEncoding;
import at.aau.dwaspgui.domain.Encoding;
import at.aau.dwaspgui.domain.FileEncoding;
import at.aau.dwaspgui.domain.Project;
import at.aau.dwaspgui.domain.TestCase;
import at.aau.dwaspgui.util.Messages;

/**
 * {@see ProjectParser} implementation that parses projects from XML files.
 * 
 * @author Philip Gasteiger
 */
public class XMLProjectParserImpl implements XMLProjectParser {
	private static final String TAG_ROOT = "logicProgram";
	private static final String TAG_BASE_DIRECTORY = "baseDirectory";
	private static final String TAG_TEST_CASES = "testCases";
	private static final String TAG_TEST_CASE = "testCase";
	private static final String TAG_TEST_CASE_NAME = "name";
	private static final String TAG_TEST_CASE_ASSERTIONS = "assertions";
	private static final String TAG_ENDOCINGS = "encodings";
	private static final String TAG_ENCODING_DIRECT = "encodingDirect";
	private static final String TAG_ENCODING_FILE = "encodingFile";

	@Override
	public Project parseProject(File projectFile)
			throws ProjectParsingException {
		try {
			return parseProject(new FileInputStream(projectFile));
		} catch (FileNotFoundException e) {
			throw new ProjectParsingException(Messages.PRJPARSER_PROJECT_NOT_FOUND
					.format(projectFile.getAbsoluteFile()), e);
		}
	}
	
	public Project parseProject(InputStream projectInput) throws ProjectParsingException {
		try {
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(projectInput);
			
			doc.getDocumentElement().normalize();
			
			return parseProject(doc);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		} catch (SAXException e) {
			throw new ProjectParsingException(Messages.PRJPARSER_INVALIDXML.format(), e);
		} catch (IOException e) {
			throw new ProjectParsingException(Messages.PRJPARSER_IOERROR.format(), e); 
		}
	}
	
	private Project parseProject(Document doc) throws ProjectParsingException {
		if (!doc.getDocumentElement().getNodeName().equals(TAG_ROOT))
			throw new ProjectParsingException("Invalid project file");
		
		String baseDirectory = getBaseDirectory(doc);
		Set<Encoding> encodings = getEncodings(doc, baseDirectory);
		Set<TestCase> testCases = getTestCases(doc, baseDirectory);
		
		return new Project(baseDirectory, encodings, testCases);
	}

	private String getBaseDirectory(Document doc) throws ProjectParsingException {
		Node node = doc.getElementsByTagName(TAG_BASE_DIRECTORY).item(0);
		
		if (node == null)
			throw new ProjectParsingException(Messages.PRJPARSER_NO_BASEDIRECTORY.format());
		
		return node.getTextContent();
	}

	private Set<Encoding> getEncodings(Document doc, String baseDirectory)
			throws ProjectParsingException {
		Node node = doc.getElementsByTagName(TAG_ENDOCINGS).item(0);
		
		if (node == null)
			throw new ProjectParsingException(Messages.PRJPARSER_NO_ENCODINGS.format());
		
		NodeList encodingsList = node.getChildNodes();
		Set<Encoding> encodings = new HashSet<Encoding>();
		
		for (int i = 0; i < encodingsList.getLength(); i ++) {
			Node encoding = encodingsList.item(i);
			
			if (encoding.getNodeName().equals(TAG_ENCODING_FILE)) {
				encodings.add(new FileEncoding(baseDirectory, encoding.getTextContent()));
			} else if (encoding.getNodeName().equals(TAG_ENCODING_DIRECT)) {
				encodings.add(new DirectEncoding(encoding.getTextContent()));
			}
		}
		return encodings;
	}

	private Set<TestCase> getTestCases(Document doc, String baseDirectory)
			throws ProjectParsingException {
		Node node = doc.getElementsByTagName(TAG_TEST_CASES).item(0);
		
		if (node == null)
			throw new ProjectParsingException(Messages.PRJPARSER_NO_TESTCASES.format());
		
		NodeList testCasesList = node.getChildNodes();
		Set<TestCase> testCases = new HashSet<TestCase>();
		
		for (int i = 0; i < testCasesList.getLength(); i ++) {
			Node testCase = testCasesList.item(i);
			
			if (testCase.getNodeName().equals(TAG_TEST_CASE)
					&& testCase instanceof Element) {
				Element testCaseEl = (Element) testCase;
				String testCaseName = testCaseEl.getElementsByTagName(TAG_TEST_CASE_NAME).item(0).getTextContent();
				String assertions = testCaseEl.getElementsByTagName(TAG_TEST_CASE_ASSERTIONS).item(0).getTextContent();
						
				testCases.add(new TestCase(testCaseName, assertions));
			}
		}
		return testCases;
	}
}
