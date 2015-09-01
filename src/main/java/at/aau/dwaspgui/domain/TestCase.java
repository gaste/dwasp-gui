package at.aau.dwaspgui.domain;

public class TestCase {
	private String name;
	private String assertions;
	
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
	
	@Override
	public String toString() {
		return name;
	}
}
