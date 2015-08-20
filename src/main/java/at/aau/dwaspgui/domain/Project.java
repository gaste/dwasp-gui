package at.aau.dwaspgui.domain;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
	private final ObservableList<Encoding> program;
	
	/** Set of instances of the program */
	private final ObservableList<TestCase> testCases;
	
	public Project(String baseDirectory, List<Encoding> program, List<TestCase> testCases) {
		this.baseDirectory = baseDirectory;
		this.program = FXCollections.observableList(program);
		this.testCases = FXCollections.observableList(testCases);
	}
	
	public String getBaseDirectory() {
		return baseDirectory;
	}

	public ObservableList<Encoding> getProgram() {
		return program;
	}

	public ObservableList<TestCase> getTestCases() {
		return testCases;
	}
}
