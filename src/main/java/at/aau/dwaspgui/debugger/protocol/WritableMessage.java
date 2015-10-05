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

package at.aau.dwaspgui.debugger.protocol;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * A message that can be written to DWASP.
 * @author Philip Gasteiger
 */
public abstract class WritableMessage extends Message {
	/**
	 * Write this message to the given output stream.
	 * @param outputStream The output stream.
	 * @throws IOException If an IO error occurs.
	 */
	public void writeToOutputStream(OutputStream outputStream) throws IOException {
		OutputStreamWriter writer  = new OutputStreamWriter(outputStream, CHARSET);

		writer.write(serialize());
		writer.write(DELIM_MSG);
		writer.flush();
	}
	
	/**
	 * Serialize the given message as string.
	 * @return The serialized message.
	 */
	protected abstract String serialize();
}
