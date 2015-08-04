package at.aau.dwaspgui.aspide;

import at.aau.dwaspgui.domain.FileEncoding;
import javafx.scene.control.IndexRange;

public class AspideNotifier {
	public void notifySave(FileEncoding encoding) {
		System.out.println("save,\"" + encoding.getFilename() + "\"");
		System.out.flush();
	}
	
	public void notifyBack(FileEncoding encoding, IndexRange selection) {
		String content = encoding.getContent();
		
		int startLine = 0;
		int startColumn = 0;
		
		for (int i = 0; i < selection.getStart() && i < content.length(); i ++) {
			if (content.charAt(i) == '\n') {
				startLine ++;
				startColumn = 0;
			} else {
				startColumn ++;				
			}
		}
		
		int endLine = startLine;
		int endColumn = startColumn;
		
		for (int i = selection.getStart(); i < selection.getEnd() && i < content.length(); i ++) {
			if (content.charAt(i) == '\n') {
				endLine ++;
				endColumn = 0;
			} else {
				endColumn ++;
			}
		}
		
		
		System.out.println("notify,\"" + encoding.getFilename() + "\""
					+ "," + startLine + "," + endLine 
					+ "," + startColumn + "," + endColumn);
		System.out.flush();
	}
}
