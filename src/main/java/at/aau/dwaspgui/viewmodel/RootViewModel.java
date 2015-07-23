package at.aau.dwaspgui.viewmodel;

import java.io.File;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import org.w3c.dom.Document;

import at.aau.dwaspgui.app.WindowManager;
import at.aau.dwaspgui.debug.Debugger;
import at.aau.dwaspgui.debug.DebuggerException;
import at.aau.dwaspgui.domain.CoreItem;
import at.aau.dwaspgui.domain.Encoding;
import at.aau.dwaspgui.domain.Project;
import at.aau.dwaspgui.domain.QueryAnswer;
import at.aau.dwaspgui.domain.TestCase;
import at.aau.dwaspgui.parser.ProjectParsingException;
import at.aau.dwaspgui.parser.XMLProjectParser;
import at.aau.dwaspgui.util.JFXUtil;
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
	private ObjectProperty<Encoding> selectedEncoding = new SimpleObjectProperty<Encoding>();
	private ObjectProperty<AbstractProjectItemViewModel> projectViewModel = new SimpleObjectProperty<AbstractProjectItemViewModel>();
	private ObservableList<TestCase> testCases = FXCollections.observableArrayList();
	private ObservableList<CoreItem> coreItems = FXCollections.observableArrayList();
	private ObservableList<String> queryAtoms = FXCollections.observableArrayList();
	
	@Inject
	public RootViewModel(WindowManager windowManager,
			XMLProjectParser projectParser, Debugger debugger) {
		this.windowManager = windowManager;
		this.projectParser = projectParser;
		this.debugger = debugger;
		
		debugger.registerCoreCallback((coreItems) -> {
			JFXUtil.runOnJFX(() -> {
				this.coreItems.clear();
				this.coreItems.addAll(coreItems);
			});
		});
		
		debugger.registerQueryCallback((queryAtoms) -> {
			JFXUtil.runOnJFX(() -> {
				this.queryAtoms.clear();
				this.queryAtoms.addAll(queryAtoms);
			});
		});
	}
	
	public void openProject(File projectFile) {
		if (projectFile != null && projectFile.exists()) {
			try {
				openProject(projectParser.parseProject(projectFile));
			} catch (ProjectParsingException e) {
				windowManager.showErrorDialog(Messages.ERROR_OPEN_PROJECT, e);
			}
		} else {
			windowManager.showErrorDialog(Messages.ERROR_OPEN_PROJECT, null);
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
		testCases.addAll(project.getTestCases());
	}

	public void openAction() {
		openProject(windowManager.chooseFile());
	}
	
	public void exitAction() {
		Platform.exit();
	}
	
	public void debugAction(TestCase testCase) {
		try {
			debugger.startDebugger(project.getProgram(), testCase);
		} catch (DebuggerException e) {
			windowManager.showErrorDialog(Messages.ERROR_START_DEBUGGER, e);
		}
	}

	public void stopAction() {
		debugger.stopDebugger();
	}
	
	public void assertAction(List<Pair<String, QueryAnswer>> assertions) {
		debugger.assertAtoms(assertions);
	}

	// properties and lists
	public ObjectProperty<AbstractProjectItemViewModel> projectProperty() { return projectViewModel; }
	public ObjectProperty<Encoding> selectedEncodingProperty() { return selectedEncoding; }
	public BooleanProperty isDebuggingProperty() { return debugger.isRunning(); }
	public ObservableList<TestCase> testCases() { return this.testCases; }
	public ObservableList<CoreItem> coreItems() { return this.coreItems; }
	public ObservableList<String> queryAtoms() { return this.queryAtoms; }
}
