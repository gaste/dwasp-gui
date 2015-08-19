package at.aau.dwaspgui.viewmodel;

import java.io.File;
import java.util.Collections;
import java.util.List;

import at.aau.dwaspgui.app.WindowManager;
import at.aau.dwaspgui.domain.Project;
import at.aau.dwaspgui.serializer.ProjectSerializationException;
import at.aau.dwaspgui.serializer.ProjectSerializer;
import at.aau.dwaspgui.serializer.ProjectSerializerXMLImpl;
import at.aau.dwaspgui.util.Messages;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.image.Image;

public class NewProjectViewModel implements ViewModel {
	private final WindowManager windowManager;
	private final RootViewModel rootViewModel;
	private final StringProperty projectName = new SimpleStringProperty();
	private final StringProperty location = new SimpleStringProperty(System.getProperty("user.home"));
	
	public NewProjectViewModel(WindowManager windowManager, RootViewModel rootViewModel) {
		this.windowManager = windowManager;
		this.rootViewModel = rootViewModel;
	}
	
	@Override
	public String getTitle() {
		return "New Project";
	}

	@Override
	public List<Image> getIcons() {
		return Collections.singletonList(new Image(WindowManager.class.getResourceAsStream("new-icon.png")));
	}
	
	public void chooseLocation() {
		File location = windowManager.chooseDirectory();
		
		if (null != location && location.isDirectory())
			this.location.set(location.getAbsolutePath());
	}
	
	public void cancel() {
		windowManager.closeModalDialog(this);
	}

	public void create() {
		Project project = new Project(location.get(), Collections.emptyList(), Collections.emptyList());
		ProjectSerializer serializer = new ProjectSerializerXMLImpl();
		
		try {
			File projectFile = new File(location.get(), projectName.get() + ".xml");
			serializer.serialize(project, projectFile);
			
			rootViewModel.openProject(projectFile);
		} catch (ProjectSerializationException e) {
			windowManager.showErrorDialog(Messages.ERROR_NEW_PROJECT, e);
		} finally {
			windowManager.closeModalDialog(this);
		}
	}
	
	public ObservableBooleanValue createDisabledProperty() {
		return projectName.length().greaterThan(0)
			   .not();
	}
	
	public StringProperty projectNameProperty() { return projectName; }
	public StringProperty locationProperty() { return location; }
}
