/*
 *  Copyright 2015 Philip Gasteiger
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
	public void parseProject_projectValid_parsesCorrect()
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
	public void parseProject_directEncodingAttributesMissing_throwsException() 
			throws ProjectParsingException {
		parser.parseProject(getClass().getResourceAsStream("projectDirectEncodingAttributesMissing.xml"));
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
	public void parseProject_projectNoTestcases_throwsException()
			throws ProjectParsingException {
		parser.parseProject(getClass().getResourceAsStream("projectNoTestcases.xml"));
	}
}
