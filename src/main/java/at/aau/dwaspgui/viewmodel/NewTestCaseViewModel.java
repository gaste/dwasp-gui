package at.aau.dwaspgui.viewmodel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.List;

import at.aau.dwaspgui.app.WindowManager;
import at.aau.dwaspgui.domain.Project;
import at.aau.dwaspgui.domain.TestCase;
import at.aau.dwaspgui.serializer.ProjectSerializationException;
import at.aau.dwaspgui.serializer.ProjectSerializer;
import at.aau.dwaspgui.util.Messages;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.image.Image;

public class NewTestCaseViewModel implements ViewModel {
	private final WindowManager windowManager;
	private final RootViewModel rootViewModel;
	private final ProjectSerializer projectSerializer;
	private final StringProperty testCaseName = new SimpleStringProperty();
	private final Project project;
	private final File projectFile;
	
	public NewTestCaseViewModel(WindowManager windowManager, RootViewModel rootViewModel, Project project, File projectFile, ProjectSerializer projectSerializer) {
		this.windowManager = windowManager;
		this.rootViewModel = rootViewModel;
		this.projectSerializer = projectSerializer;
		this.project = project;
		this.projectFile = projectFile;
	}
	
	@Override
	public String getTitle() {
		return "New Test Case";
	}

	@Override
	public List<Image> getIcons() {
		return Collections.singletonList(new Image(WindowManager.class.getResourceAsStream("new-icon.png")));
	}
	
	public void cancel() {
		windowManager.closeModalDialog(this);
	}

	public void create() {
		TestCase testCase = new TestCase(testCaseName.get(), "% Use assertTrue(atom) and assertFalse(atom) to add assertions");

		project.getTestCases().add(testCase);
		try{
			projectSerializer.serialize(project, new FileOutputStream(projectFile));
			rootViewModel.selectedTestCaseProperty().set(testCase);
		} catch (ProjectSerializationException e) {
			windowManager.showErrorDialog(Messages.ERROR_SAVE_PROJECT, e);
		} catch (FileNotFoundException e) {
			windowManager.showErrorDialog(Messages.ERROR_SAVE_PROJECT, e);
		} finally {
			windowManager.closeModalDialog(this);
		}
	}
	
	public ObservableBooleanValue createDisabledProperty() {
		return testCaseName.length().greaterThan(0)
			   .not();
	}
	
	public StringProperty testCaseNameProperty() { return testCaseName; }
}
