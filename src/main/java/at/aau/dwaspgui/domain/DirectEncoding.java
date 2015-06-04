package at.aau.dwaspgui.domain;

/**
 * Encoding of a logic program that is in-memory.
 * 
 * @author Philip Gasteiger
 */
public class DirectEncoding extends Encoding {
	/** The content of the encoding */
	private String encoding;

	public DirectEncoding(String encoding) {
		this.encoding = encoding.replace(" ", "").replace("\t", "");
	}

	@Override
	public String getContent() {
		return encoding;
	}
}
