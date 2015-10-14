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
