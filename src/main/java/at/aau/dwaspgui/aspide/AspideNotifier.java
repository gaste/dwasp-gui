package at.aau.dwaspgui.aspide;

import at.aau.dwaspgui.domain.FileEncoding;

public class AspideNotifier {
	public void notifySave(FileEncoding encoding) {
		System.out.println("save,\"" + encoding.getFilename() + "\"");
		System.out.flush();
	}
	
	public void notifyBack(FileEncoding encoding, int startLine) {
		System.out.println("notify,\"" + encoding.getFilename() + "\"," + startLine + "," + startLine + ",0,0");
		System.out.flush();
	}
}
