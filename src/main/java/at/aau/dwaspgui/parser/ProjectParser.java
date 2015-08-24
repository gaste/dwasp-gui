package at.aau.dwaspgui.parser;

import java.io.InputStream;

import at.aau.dwaspgui.domain.Project;

/**
 * Parse a project from a file.
 * @author Philip Gasteiger
 */
public interface ProjectParser {
	/**
	 * Parse a project from the given project file.
	 * @param source The input stream for reading the project.
	 * @throws ProjectParsingException If the project could not be parsed.
	 */
	Project parseProject(InputStream source) throws ProjectParsingException;
}
