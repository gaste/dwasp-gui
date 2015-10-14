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

package at.aau.dwaspgui.domain;

/**
 * Encoding of a logic program that is in-memory.
 * 
 * @author Philip Gasteiger
 */
public class DirectEncoding extends Encoding {
	/** The content of the encoding */
	private final String encoding;
	private final String sourceFile;
	private final int startLine;
	private final int endLine;
	private final int startColumn;
	private final int endColumn;
	

	public DirectEncoding(String encoding, String sourceFile, int startLine, int endLine, int startColumn, int endColumn) {
		this.encoding = encoding;
		this.sourceFile = sourceFile;
		this.startLine = startLine;
		this.endLine = endLine;
		this.startColumn = startColumn;
		this.endColumn = endColumn;
	}
	
	@Override
	public String toString() {
		if (sourceFile.contains("\\")) {
			return sourceFile.substring(sourceFile.lastIndexOf('\\') + 1);
		} else if (sourceFile.contains("/")) {
			return sourceFile.substring(sourceFile.lastIndexOf('/') + 1);
		} else {
			return sourceFile;
		}
	}

	@Override
	public String getContent() {
		return encoding;
	}
	
	public String getSourceFile() {
		return sourceFile;
	}
	
	public int getStartLine() {
		return startLine;
	}
	
	public int getEndLine() {
		return endLine;
	}
	
	public int getStartColumn() {
		return startColumn;
	}
	
	public int getEndColumn() {
		return endColumn;
	}
}
