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
