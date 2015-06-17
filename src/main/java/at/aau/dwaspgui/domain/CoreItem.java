package at.aau.dwaspgui.domain;

public class CoreItem {
	private Encoding encoding;
	private int line;
	private int column;
	
	public CoreItem(Encoding encoding, int line, int column) {
		this.encoding = encoding;
		this.line = line;
		this.column = column;
	}

	public Encoding getEncoding() {
		return encoding;
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}
}
