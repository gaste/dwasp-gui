package at.aau.dwaspgui.viewmodel.project;

import at.aau.dwaspgui.domain.Encoding;

class ProjectItemEncodingViewModel extends AbstractProjectItemViewModel {
	private final Encoding encoding;
	
	protected ProjectItemEncodingViewModel (Encoding encoding) {
		this.encoding = encoding;
	}

	@Override
	public String toString() {
		return encoding.toString();
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	public String getContent() {
		return encoding.getContent();
	}
}