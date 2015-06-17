package at.aau.dwaspgui.viewmodel.project;

import at.aau.dwaspgui.domain.Encoding;
import at.aau.dwaspgui.domain.Project;
import at.aau.dwaspgui.domain.TestCase;

class ProjectItemViewModel extends AbstractProjectItemViewModel {
	private ProjectItemLabelViewModel testCases;
	
	public ProjectItemViewModel(Project project) {
		this.testCases = new ProjectItemLabelViewModel("Test Cases");
		
		for (Encoding encoding : project.getProgram()) {
			this.addChild(new ProjectItemEncodingViewModel(encoding));
		}
		
		for (TestCase testCase : project.getTestCases()) {
			testCases.addChild(new ProjectItemTestCaseViewModel(testCase));
		}
		
		addChild(testCases);
	}

	@Override
	public String toString() {
		return "";
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	public String getContent() {
		return null;
	}
}