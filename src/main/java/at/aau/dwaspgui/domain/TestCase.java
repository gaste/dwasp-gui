package at.aau.dwaspgui.domain;

public class TestCase {
	private String name;
	private String assertions;
	
	public TestCase(String name, String assertions) {
		this.name = name;
		this.assertions = assertions.replace(" ", "").replace("\t", "");
	}

	public String getName() {
		return name;
	}

	public String getAssertions() {
		return assertions;
	}
}
