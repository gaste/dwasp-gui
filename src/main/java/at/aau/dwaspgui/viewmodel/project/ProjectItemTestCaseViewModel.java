package at.aau.dwaspgui.viewmodel.project;

import at.aau.dwaspgui.domain.Encoding;
import at.aau.dwaspgui.domain.TestCase;

class ProjectItemTestCaseViewModel extends AbstractProjectItemViewModel {
	private final TestCase testCase;
	
	protected ProjectItemTestCaseViewModel (TestCase testCase) {
		this.testCase = testCase;
	}

	@Override
	public String toString() {
		return testCase.getName();
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	public String getContent() {
		return testCase.getAssertions();
	}

	@Override
	public Encoding getEncoding() {
		return null;
	}
}