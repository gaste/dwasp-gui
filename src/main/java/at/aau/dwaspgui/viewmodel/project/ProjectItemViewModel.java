package at.aau.dwaspgui.viewmodel.project;

import at.aau.dwaspgui.domain.Encoding;
import at.aau.dwaspgui.domain.Instance;
import at.aau.dwaspgui.domain.Project;
import at.aau.dwaspgui.domain.TestCase;

class ProjectItemViewModel extends AbstractProjectItemViewModel {
	private ProjectItemLabelViewModel programs;
	private ProjectItemLabelViewModel instances;
	
	public ProjectItemViewModel(Project project) {
		this.programs = new ProjectItemLabelViewModel("Program");
		this.instances = new ProjectItemLabelViewModel("Instances");
		
		for (Instance instance : project.getInstances()) {
			ProjectItemLabelViewModel in = new ProjectItemLabelViewModel(instance.getName());
			
			in.addChild(new ProjectItemEncodingViewModel(instance.getInstance()));

			for (Encoding encoding : project.getProgram()) {
				this.programs.addChild(new ProjectItemEncodingViewModel(encoding));
			}
			
			for (TestCase testCase : instance.getTestCases()) {
				in.addChild(new ProjectItemTestCaseViewModel(testCase));
			}

			this.instances.addChild(in);
		}
		
		
		children.add(this.programs);
		children.add(this.instances);
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