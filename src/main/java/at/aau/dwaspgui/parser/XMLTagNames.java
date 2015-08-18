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
	
	// prevent construction of this class
	private XMLTagNames() { throw new AssertionError(); }
}
