package at.aau.dwaspgui.viewmodel;

import java.io.File;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.w3c.dom.Document;

import at.aau.dwaspgui.app.WindowManager;
import at.aau.dwaspgui.debug.Debugger;
import at.aau.dwaspgui.domain.Encoding;
import at.aau.dwaspgui.domain.Instance;
import at.aau.dwaspgui.domain.Project;
import at.aau.dwaspgui.domain.QueryAnswer;
import at.aau.dwaspgui.domain.TestCase;
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
	private final Debugger debugger;
	private Project project;
	private ObjectProperty<AbstractProjectItemViewModel> projectViewModel = new SimpleObjectProperty<AbstractProjectItemViewModel>();
	private ObservableList<TestCase> testCases = FXCollections.observableArrayList();
	
	@Inject
	public RootViewModel(WindowManager windowManager,
			XMLProjectParser projectParser, Debugger debugger) {
		this.windowManager = windowManager;
		this.projectParser = projectParser;
		this.debugger = debugger;
		
		debugger.registerCoreCallback((coreItems) -> {
			System.out.println("IN CORE!");
		});
	}
	
	public void openAction() {
		openProject(windowManager.chooseFile());
	}
	
	public void openProject(File projectFile) {
		if (projectFile != null && projectFile.exists()) {
			try {
				openProject(projectParser.parseProject(projectFile));
			} catch (ProjectParsingException e) {
				windowManager.showErrorDialog(Messages.ERROR_OPEN_PROJECT, e);
			}
		}
	}
	
	public void openProject(Document projectDocument) {
		try {
			openProject(projectParser.parseProject(projectDocument));
		} catch (ProjectParsingException e) {
			windowManager.showErrorDialog(Messages.ERROR_OPEN_PROJECT, e);
		}
	}
	
	private void openProject(Project project) {
		this.project = project;
		this.projectViewModel.set(AbstractProjectItemViewModel.create(project));
		
		testCases.clear();
		
		for (Instance instance : project.getInstances()) {
			for (TestCase testCase : instance.getTestCases()) {
				testCases.add(testCase);
			}
		}
	}
	
	public void exitAction() {
		Platform.exit();
	}
	
	public void debugAction(TestCase testCase) {
		Encoding instance = null;
		
		for (Instance i : project.getInstances()) {
			for (TestCase c : i.getTestCases()) {
				if (c == testCase) {
					instance = i.getInstance();
				}
			}
		}
		
		debugger.startDebugger(project.getProgram(), instance, testCase);
	}
	
	public ObjectProperty<AbstractProjectItemViewModel> projectProperty() {
		return this.projectViewModel;
	}
	
	public ObservableList<TestCase> testCases() {
		return this.testCases;
	}
	
	public BooleanProperty isDebugging() {
		return debugger.isRunning();
	}

	public void stopAction() {
		debugger.stopDebugger();
	}
	
	public void askAction() {
		debugger.computeQuery((atom) -> {
			return QueryAnswer.YES;
		});
	}
}
