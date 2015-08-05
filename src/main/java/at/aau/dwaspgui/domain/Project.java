package at.aau.dwaspgui.domain;

import java.util.List;

/**
 * Logic Program consists of a set of encodings, as well as a set of instances
 * of the program.
 * 
 * @author Philip Gasteiger
 */
public class Project {
	/** Base directory, to which all path's are relative to */
	private final String baseDirectory;
	
	/** Set of encodings that represent the logic program */
	private final List<Encoding> program;
	
	/** Set of instances of the program */
	private final List<TestCase> testCases;
	
	public Project(String baseDirectory, List<Encoding> program, List<TestCase> testCases) {
		this.baseDirectory = baseDirectory;
		this.program = program;
		this.testCases = testCases;
	}

	public String getBaseDirectory() {
		return baseDirectory;
	}

	public List<Encoding> getProgram() {
		return program;
	}

	public List<TestCase> getTestCases() {
		return testCases;
	}
}
