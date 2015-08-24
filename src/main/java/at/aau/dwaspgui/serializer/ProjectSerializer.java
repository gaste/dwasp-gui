package at.aau.dwaspgui.serializer;

import java.io.OutputStream;

import at.aau.dwaspgui.domain.Project;

/**
 * Serialize a project to a file.
 * @author Philip Gasteiger
 */
public interface ProjectSerializer {
	/**
	 * Serialize the project to the given file.
	 * @param project The project the serialize.
	 * @param dest    The stream to which the project should be serialized.
	 * @throws ProjectSerializationException If the project could not be
	 *                                       serialized.
	 */
	public void serialize(Project project, OutputStream dest) throws ProjectSerializationException;
}
