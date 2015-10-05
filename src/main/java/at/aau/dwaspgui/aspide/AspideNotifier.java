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

package at.aau.dwaspgui.aspide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.aau.dwaspgui.domain.CoreItem;
import at.aau.dwaspgui.domain.FileEncoding;
import javafx.scene.control.IndexRange;

/**
 * Notify ASPIDE of debugging events.
 * @author Philip Gasteiger
 */
public class AspideNotifier {
	public void notifySave(FileEncoding encoding) {
		System.out.println("save,\"" + encoding.getAbsolutePath() + "\"");
		System.out.flush();
	}
	
	public void notifyBack(FileEncoding encoding, List<CoreItem> coreItems) {
		String content = encoding.getContent();
		List<Selection> selections = new ArrayList<Selection>();
		
		for (CoreItem i : coreItems) {
			selections.add(new Selection(content, i.getFromIndex(), i.getFromIndex() + i.getLength()));
		}
		
		notifyBackSelection(encoding, selections);
	}
	
	public void notifyBack(FileEncoding encoding, IndexRange selection) {
		notifyBackSelection(encoding, Arrays.asList(new Selection(encoding.getContent(), selection)));
	}
	
	private void notifyBackSelection(FileEncoding encoding, List<Selection> selections) {
		System.out.print("notify,");
		System.out.print("\"" + encoding.getAbsolutePath() + "\",");
		System.out.print(selections.size());
		
		for (Selection sel : selections) {
			System.out.print("," + sel.getStartLine());
			System.out.print("," + sel.getEndLine());
			System.out.print("," + sel.getStartColumn());
			System.out.print("," + sel.getEndColumn());
		}
		System.out.print("\n");
		System.out.flush();
	}
	
	class Selection {
		private int startLine;
		private int startColumn;
		private int endLine;
		private int endColumn;
		
		public Selection(String text, IndexRange selection) {
			this(text, selection.getStart(), selection.getEnd());
		}
		
		public Selection(String text, int startIndex, int endIndex) {
			startLine = 0;
			startColumn = 0;
			
			for (int i = 0; i < startIndex && i < text.length(); i ++) {
				if (text.charAt(i) == '\n') {
					startLine ++;
					startColumn = 0;
				} else {
					startColumn ++;				
				}
			}
			
			endLine = startLine;
			endColumn = startColumn;
			
			for (int i = startIndex; i < endIndex && i < text.length(); i ++) {
				if (text.charAt(i) == '\n') {
					endLine ++;
					endColumn = 0;
				} else {
					endColumn ++;
				}
			}
		}
		
		public final int getStartLine() {
			return startLine;
		}
		
		public final int getStartColumn() {
			return startColumn;
		}
		
		public final int getEndLine() {
			return endLine;
		}
		
		public final int getEndColumn() {
			return endColumn;
		}
	}
}
