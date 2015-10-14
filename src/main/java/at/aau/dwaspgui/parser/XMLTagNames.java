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

package at.aau.dwaspgui.parser;

/**
 * Holds all tag names for (de-)serializing the project.
 * @author Philip Gasteiger
 */
public final class XMLTagNames {
	public static final String TAG_ROOT                 = "logicProgram";
	public static final String TAG_BASE_DIRECTORY       = "baseDirectory";
	public static final String TAG_TEST_CASES           = "testCases";
	public static final String TAG_TEST_CASE            = "testCase";
	public static final String TAG_TEST_CASE_NAME       = "name";
	public static final String TAG_TEST_CASE_ASSERTIONS = "assertions";
	public static final String TAG_ENDOCINGS            = "encodings";
	public static final String TAG_ENCODING_DIRECT      = "encodingDirect";
	public static final String TAG_ENCODING_FILE        = "encodingFile";
	
	public static final String ATTR_ENCODING_DIRECT_SOURCEFILE  = "sourceFile";
	public static final String ATTR_ENCODING_DIRECT_STARTLINE   = "startLine";
	public static final String ATTR_ENCODING_DIRECT_ENDLINE     = "endLine";
	public static final String ATTR_ENCODING_DIRECT_STARTCOLUMN = "startColumn";
	public static final String ATTR_ENCODING_DIRECT_ENDCOLUMN   = "endColumn";
	
	// prevent construction of this class
	private XMLTagNames() { throw new AssertionError(); }
}
