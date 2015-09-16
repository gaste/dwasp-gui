package at.aau.dwaspgui.serializer;

import static at.aau.dwaspgui.parser.XMLTagNames.TAG_BASE_DIRECTORY;
import static at.aau.dwaspgui.parser.XMLTagNames.TAG_ENCODING_DIRECT;
import static at.aau.dwaspgui.parser.XMLTagNames.TAG_ENCODING_FILE;
import static at.aau.dwaspgui.parser.XMLTagNames.TAG_ENDOCINGS;
import static at.aau.dwaspgui.parser.XMLTagNames.TAG_ROOT;
import static at.aau.dwaspgui.parser.XMLTagNames.TAG_TEST_CASE;
import static at.aau.dwaspgui.parser.XMLTagNames.TAG_TEST_CASES;
import static at.aau.dwaspgui.parser.XMLTagNames.TAG_TEST_CASE_ASSERTIONS;
import static at.aau.dwaspgui.parser.XMLTagNames.TAG_TEST_CASE_NAME;
import static at.aau.dwaspgui.parser.XMLTagNames.*;

import java.io.OutputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import at.aau.dwaspgui.domain.DirectEncoding;
import at.aau.dwaspgui.domain.Encoding;
import at.aau.dwaspgui.domain.FileEncoding;
import at.aau.dwaspgui.domain.Project;
import at.aau.dwaspgui.domain.TestCase;

/**
 * Implementation of {@link ProjectSerializer} using XML.
 * @see ProjectSerializer
 * @author Philip Gasteiger
 */
public class ProjectSerializerXMLImpl implements ProjectSerializer {
	@Override
	public void serialize(Project project, OutputStream dest) throws ProjectSerializationException {
		try {
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document doc = builder.newDocument();
			
			Element root = doc.createElement(TAG_ROOT);
			doc.appendChild(root);
			
			root.appendChild(getBaseDirectory(doc, project.getBaseDirectory()));
			root.appendChild(getEncodings(doc, project.getProgram()));
			root.appendChild(getTestcases(doc, project.getTestCases()));
			
			writeDocument(doc, dest);
		} catch (ParserConfigurationException e) {
			throw new ProjectSerializationException(e);
		}
	}

	private void writeDocument(Document doc, OutputStream dest) throws ProjectSerializationException {
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
	        
	        DOMSource source = new DOMSource(doc);
	        StreamResult result = new StreamResult(dest);
	        
	        transformer.transform(source, result);
		} catch (TransformerFactoryConfigurationError | TransformerException e) {
			throw new ProjectSerializationException(e);
		}
	}
	
	private Node getTestcases(Document doc, List<TestCase> testCases) {
		Element tc = doc.createElement(TAG_TEST_CASES);
		
		for (final TestCase testCase : testCases) {
			tc.appendChild(getTestcase(doc, testCase));
		}
		
		return tc;
	}

	private Node getTestcase(Document doc, TestCase testCase) {
		Element tc = doc.createElement(TAG_TEST_CASE);
		
		Element name = doc.createElement(TAG_TEST_CASE_NAME);
		name.appendChild(doc.createTextNode(testCase.getName()));
		tc.appendChild(name);
		
		Element assertions = doc.createElement(TAG_TEST_CASE_ASSERTIONS);
		assertions.appendChild(doc.createTextNode(testCase.getAssertions()));
		tc.appendChild(assertions);
		
		return tc;
	}

	private Node getEncodings(Document doc, List<Encoding> program) {
		Element encodings = doc.createElement(TAG_ENDOCINGS);
		
		for (final Encoding encoding : program) {
			encodings.appendChild(getEncoding(doc, encoding));
		}
		
		return encodings;
	}

	private Node getEncoding(Document doc, Encoding encoding) {
		if (encoding instanceof FileEncoding)
			return getFileEncoding(doc, (FileEncoding) encoding);
		
		if (encoding instanceof DirectEncoding)
			return getDirectEncoding(doc, (DirectEncoding) encoding);
		
		// invalid encoding type
		return null;
	}

	private Node getDirectEncoding(Document doc, DirectEncoding encoding) {
		Element enc = doc.createElement(TAG_ENCODING_DIRECT);
		enc.appendChild(doc.createTextNode(encoding.getContent()));
		enc.setAttribute(ATTR_ENCODING_DIRECT_SOURCEFILE, encoding.getSourceFile());
		enc.setAttribute(ATTR_ENCODING_DIRECT_STARTLINE, "" + encoding.getStartLine());
		enc.setAttribute(ATTR_ENCODING_DIRECT_ENDLINE, "" + encoding.getEndLine());
		enc.setAttribute(ATTR_ENCODING_DIRECT_STARTCOLUMN, "" + encoding.getStartColumn());
		enc.setAttribute(ATTR_ENCODING_DIRECT_ENDCOLUMN, "" + encoding.getEndColumn());
		
		return enc;
	}

	private Node getFileEncoding(Document doc, FileEncoding encoding) {
		Element enc = doc.createElement(TAG_ENCODING_FILE);
		enc.appendChild(doc.createTextNode(encoding.getRelativePath()));
		
		return enc;
	}

	private Node getBaseDirectory(Document doc, String baseDirectory) {
		Element bd = doc.createElement(TAG_BASE_DIRECTORY);
		bd.appendChild(doc.createTextNode(baseDirectory));
		
		return bd;
	}
}
