package at.aau.dwaspgui.domain;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class TestCase {
	private final String name;
	
	/** The assertions of the testcase */
	private String assertions;

	/** Flag whether there are unsaved changes present */
	private final BooleanProperty dirty = new SimpleBooleanProperty(false);
	
	public TestCase(String name, String assertions) {
		this.name = name;
		this.assertions = assertions;
	}

	public String getName() {
		return name;
	}

	public String getAssertions() {
		return assertions;
	}
	
	public void setAssertions(String newAssertions) {
		if (this.assertions.equals(newAssertions))
			return;
		
		dirty.set(true);
		this.assertions = newAssertions;
	}
	
	public BooleanProperty dirtyProperty() { return dirty; }
	public final boolean isDirty() { return dirty.get(); }
	
	@Override
	public String toString() {
		return name + (dirty.get() ? " \u25CF" : "");
	}
}
