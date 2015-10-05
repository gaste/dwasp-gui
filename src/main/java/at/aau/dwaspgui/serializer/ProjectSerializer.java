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
