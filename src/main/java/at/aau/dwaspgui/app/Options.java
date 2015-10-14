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

package at.aau.dwaspgui.app;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.converters.FileConverter;

import at.aau.input.InvalidOptionException;

/**
 * Holds the options passed by the command line interface.
 * 
 * @author Philip Gasteiger
 */
@Parameters(separators = "=")
public class Options {
	private static final String PROGRAM_NAME = "dwasp-gui";
	
	private JCommander cli;
	
	@Parameter(description = "[project_file]", converter = FileConverter.class)
	private List<File> projectFile = new ArrayList<File>();

	@Parameter(names = { "-h" }, help = true, description = "Print help information and exit")
	private boolean printHelp = false;
	
	@Parameter(names = { "-aspide" }, description = "Specify this option if the GUI is started from ASPIDE")
	private boolean aspide = false;
	
	@Parameter(names = { "-d" }, description = "The command used to start the debugger", required = false)
	private String debuggerCommand;
	
	@Parameter(names = { "-g" }, description = "The command used to start the grounder", required = false)
	private String grounderCommand;
	
	/**
	 * Parse the given command line arguments.
	 * 
	 * @param args The arguments to parse.
	 * @throws InvalidOptionException If an error occurs while parsing the
	 * 								  options.
	 */
	public Options(String[] args) throws InvalidOptionException {
		try{
			cli = new JCommander(this, args);
			cli.setProgramName(PROGRAM_NAME);
		} catch (ParameterException e) {
			// unknown option
			StringBuilder errorMessage = new StringBuilder(e.getMessage() + "\n");
			
			cli = new JCommander(this);
			cli.setProgramName(PROGRAM_NAME);
			cli.usage(errorMessage);
			
			throw new InvalidOptionException(errorMessage.toString());
		}
	}
	
	public void printHelp() { cli.usage(); }
	public boolean isPrintHelp() { return printHelp; }
	public boolean isStartedFromAspide() { return aspide; }
	
	public boolean isProjectFileSpecified() { return projectFile.size() > 0; }
	public File getProjectFile() { return projectFile.size() > 0 ? projectFile.get(0) : null; }
	
	public boolean isGrounderCommandSpecified() { return grounderCommand != null; }
	public String getGrounderCommand() { return grounderCommand; }
	
	public boolean isDebuggerCommandSpecified() { return debuggerCommand != null; }
	public String getDebuggerCommand() { return debuggerCommand; }
}
