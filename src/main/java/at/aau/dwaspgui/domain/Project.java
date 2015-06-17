package at.aau.dwaspgui.domain;

import java.util.Set;

/**
 * Logic Program consists of a set of encodings, as well as a set of instances
 * of the program.
 * 
 * @author Philip Gasteiger
 */
public class Project {
	/** Base directory, to which all path's are relative to */
	private String baseDirectory;
	
	/** Set of encodings that represent the logic program */
	private Set<Encoding> program;
	
	/** Set of instances of the program */
	private Set<TestCase> testCases;
	
	public Project(String baseDirectory, Set<Encoding> program, Set<TestCase> testCases) {
		this.baseDirectory = baseDirectory;
		this.program = program;
		this.testCases = testCases;
	}

	public String getBaseDirectory() {
		return baseDirectory;
	}

	public Set<Encoding> getProgram() {
		return program;
	}

	public Set<TestCase> getTestCases() {
		return testCases;
	}
}
