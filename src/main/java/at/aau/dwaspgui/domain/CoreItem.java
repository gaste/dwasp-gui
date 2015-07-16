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
