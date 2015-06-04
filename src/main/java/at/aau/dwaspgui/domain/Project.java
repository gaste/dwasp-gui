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
	private Set<Instance> instances;
	
	public Project(String baseDirectory, Set<Encoding> program, Set<Instance> instances) {
		this.baseDirectory = baseDirectory;
		this.program = program;
		this.instances = instances;
	}

	public String getBaseDirectory() {
		return baseDirectory;
	}

	public Set<Encoding> getProgram() {
		return program;
	}

	public Set<Instance> getInstances() {
		return instances;
	}
}
