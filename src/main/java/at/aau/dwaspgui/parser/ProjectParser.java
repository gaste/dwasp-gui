package at.aau.dwaspgui.parser;

import java.io.File;

import at.aau.dwaspgui.domain.Project;

/**
 * Parse a project from a file.
 * @author Philip Gasteiger
 */
public interface ProjectParser {
	/**
	 * Parse a project from the given project file.
	 * @param projectFile The file containing the project.
	 * @throws ProjectParsingException If the project could not be parsed.
	 */
	Project parseProject(File projectFile) throws ProjectParsingException;
}
