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

import java.util.List;
import java.util.Map;

import at.aau.Rule;

public class CoreItem {
	private Encoding encoding;
	private Rule rule;
	private List<Map<String, String>> substitutions;
	private int fromIndex;
	private int length;
	
	public CoreItem(Encoding encoding, Rule rule, List<Map<String, String>> substitutions) {
		this.encoding = encoding;
		this.rule = rule;
		this.substitutions = substitutions;
		this.fromIndex = encoding.getContent().indexOf(rule.getRule());
		this.length = rule.getRule().length();
	}

	public Encoding getEncoding() {
		return encoding;
	}
	
	public Rule getRule() {
		return rule;
	}
	
	public List<Map<String, String>> getSubstitutions() {
		return substitutions;
	}

	public int getFromIndex() {
		return fromIndex;
	}

	public int getLength() {
		return length;
	}
}
