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
