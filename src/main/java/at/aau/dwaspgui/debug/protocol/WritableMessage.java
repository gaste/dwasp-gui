package at.aau.dwaspgui.debug.protocol;

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
