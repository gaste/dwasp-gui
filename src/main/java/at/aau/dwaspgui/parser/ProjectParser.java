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
