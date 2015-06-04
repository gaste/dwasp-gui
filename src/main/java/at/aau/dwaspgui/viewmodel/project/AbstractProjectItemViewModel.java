package at.aau.dwaspgui.viewmodel.project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import at.aau.dwaspgui.domain.Project;

/**
 * View Model for a project item.
 * 
 * @author Philip Gasteiger
 */
public abstract class AbstractProjectItemViewModel {
	ObservableList<AbstractProjectItemViewModel> children = FXCollections.observableArrayList();
	
	protected void addChild(AbstractProjectItemViewModel child) {
		children.add(child);
	}
	
	public ObservableList<AbstractProjectItemViewModel> getChildren() {
		return children;
	}
	
	@Override
	public abstract String toString();
	
	public abstract boolean isEditable();
	
	public abstract String getContent();
	
	public static AbstractProjectItemViewModel create(Project p) {
		return new ProjectItemViewModel(p);
	}
}
