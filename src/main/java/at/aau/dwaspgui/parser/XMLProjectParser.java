package at.aau.dwaspgui.parser;

import java.io.File;

import org.w3c.dom.Document;

import at.aau.dwaspgui.domain.Project;

/**
 * Specifies methods to parse a project from a file.
 * 
 * @author Philip Gasteiger
 */
public interface XMLProjectParser {
	/**
	 * Parse a project from the given project file.
	 * 
	 * @param projectFile
	 *            The file containing the project.
	 * @throws ProjectParsingException
	 *             If the project could not be parsed.
	 */
	Project parseProject(File projectFile) throws ProjectParsingException;

	/**
	 * Parse a project from the given XML document.
	 * 
	 * @param projectDocument
	 *            The XML document containing the project.
	 * @throws ProjectParsingException
	 *             If the project could not be parsed.
	 */
	Project parseProject(Document projectDocument) throws ProjectParsingException;
}
