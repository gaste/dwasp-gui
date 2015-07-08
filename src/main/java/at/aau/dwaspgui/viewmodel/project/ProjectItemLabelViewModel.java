package at.aau.dwaspgui.viewmodel.project;

import at.aau.dwaspgui.domain.Encoding;

class ProjectItemLabelViewModel extends AbstractProjectItemViewModel {
	private final String label;
	
	protected ProjectItemLabelViewModel (String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	public String getContent() {
		return null;
	}

	@Override
	public Encoding getEncoding() {
		return null;
	}
}