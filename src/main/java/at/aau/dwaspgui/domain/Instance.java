package at.aau.dwaspgui.domain;

import java.util.Set;

/**
 * A instance of a logic program.
 * 
 * @author Philip Gasteiger
 */
public class Instance {
	/** Name of the instance */
	private String name;
	
	/** The encoding of the instance */
	private Encoding instance;
	
	/** The test cases associated with the instance */
	private Set<TestCase> testCases;
	
	public Instance(String name, Encoding instance, Set<TestCase> testCases) {
		this.name = name;
		this.instance = instance;
		this.testCases = testCases;
	}
	
	public String getName() {
		return name;
	}

	public Encoding getInstance() {
		return instance;
	}

	public Set<TestCase> getTestCases() {
		return testCases;
	}
}
