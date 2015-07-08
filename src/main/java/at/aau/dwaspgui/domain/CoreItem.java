package at.aau.dwaspgui.domain;

public class CoreItem {
	private Encoding encoding;
	private int fromIndex;
	private int length;
	
	public CoreItem(Encoding encoding, int fromIndex, int length) {
		this.encoding = encoding;
		this.fromIndex = fromIndex;
		this.length = length;
	}

	public Encoding getEncoding() {
		return encoding;
	}

	public int getFromIndex() {
		return fromIndex;
	}

	public int getLength() {
		return length;
	}
}
