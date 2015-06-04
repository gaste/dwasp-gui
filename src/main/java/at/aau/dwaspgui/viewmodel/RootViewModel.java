package at.aau.dwaspgui.viewmodel;

import java.io.File;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import org.w3c.dom.Document;

import at.aau.dwaspgui.app.WindowManager;
import at.aau.dwaspgui.domain.Project;
import at.aau.dwaspgui.parser.ProjectParsingException;
import at.aau.dwaspgui.parser.XMLProjectParser;
import at.aau.dwaspgui.util.Messages;
import at.aau.dwaspgui.viewmodel.project.AbstractProjectItemViewModel;

import com.google.inject.Inject;

/**
 * Main view model of the application.
 * 
 * @author Philip Gasteiger
 */
public class RootViewModel implements ViewModel {
	private final WindowManager windowManager;
	private final XMLProjectParser projectParser;
	private ObjectProperty<AbstractProjectItemViewModel> project = new SimpleObjectProperty<AbstractProjectItemViewModel>();
	
	@Inject
	public RootViewModel(WindowManager windowManager, XMLProjectParser projectParser) {
		this.windowManager = windowManager;
		this.projectParser = projectParser;
	}
	
	public void openAction() {
		openProject(windowManager.chooseFile());
	}
	
	public void openProject(File projectFile) {
		if (projectFile != null && projectFile.exists()) {
			try {
				Project project = projectParser.parseProject(projectFile);
				this.project.set(AbstractProjectItemViewModel.create(project));
			} catch (ProjectParsingException e) {
				windowManager.showErrorDialog(Messages.ERROR_OPEN_PROJECT, e);
			}
		}
	}
	
	public void openProject(Document projectDocument) {
		try {
			Project project = projectParser.parseProject(projectDocument);
			this.project.set(AbstractProjectItemViewModel.create(project));
		} catch (ProjectParsingException e) {
			windowManager.showErrorDialog(Messages.ERROR_OPEN_PROJECT, e);
		}
	}
	
	public void exitAction() {
		Platform.exit();
	}
	
	public ObjectProperty<AbstractProjectItemViewModel> projectProperty() {
		return this.project;
	}
}
