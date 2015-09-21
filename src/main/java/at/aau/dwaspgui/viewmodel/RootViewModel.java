package at.aau.dwaspgui.viewmodel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import at.aau.dwaspgui.app.WindowManager;
import at.aau.dwaspgui.aspide.AspideNotifier;
import at.aau.dwaspgui.debugger.Debugger;
import at.aau.dwaspgui.debugger.DebuggerException;
import at.aau.dwaspgui.domain.CoreItem;
import at.aau.dwaspgui.domain.Encoding;
import at.aau.dwaspgui.domain.FileEncoding;
import at.aau.dwaspgui.domain.Project;
import at.aau.dwaspgui.domain.QueryAnswer;
import at.aau.dwaspgui.domain.TestCase;
import at.aau.dwaspgui.parser.ProjectParser;
import at.aau.dwaspgui.parser.ProjectParsingException;
import at.aau.dwaspgui.serializer.ProjectSerializationException;
import at.aau.dwaspgui.serializer.ProjectSerializer;
import at.aau.dwaspgui.util.JFXUtil;
import at.aau.dwaspgui.util.Messages;
import at.aau.dwaspgui.viewmodel.query.QueryViewModel;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.IndexRange;
import javafx.scene.image.Image;

/**
 * Main view model of the application.
 * 
 * @author Philip Gasteiger
 */
public class RootViewModel implements ViewModel {
	private final WindowManager windowManager;
	private final ProjectParser projectParser;
	private final ProjectSerializer projectSerializer;
	private final Debugger debugger;
	private final AspideNotifier notifier;
	private File projectFile;
	private final ObjectProperty<Project> project = new SimpleObjectProperty<Project>(null);
	private final BooleanProperty isAspideSession = new SimpleBooleanProperty(false);
	private final ObjectProperty<Encoding> selectedEncoding = new SimpleObjectProperty<Encoding>();
	private final ObjectProperty<TestCase> selectedTestCase = new SimpleObjectProperty<TestCase>();
	private final ObservableList<Encoding> encodings;
	private final ObservableList<TestCase> testCases;
	private final ObservableList<CoreItem> coreItems = FXCollections.observableArrayList();
	private final ObservableList<QueryViewModel> queryAtoms = FXCollections.observableArrayList();
	
	@Inject
	public RootViewModel(WindowManager windowManager,
			ProjectParser projectParser, ProjectSerializer projectSerializer,
			Debugger debugger, AspideNotifier notifier) {
		this.windowManager = windowManager;
		this.projectParser = projectParser;
		this.projectSerializer = projectSerializer;
		this.debugger = debugger;
		this.notifier = notifier;
		
		this.encodings = FXCollections.observableArrayList((encoding) -> {
			if (encoding instanceof FileEncoding) {
				FileEncoding enc = (FileEncoding) encoding;
				return new Observable[] { enc.dirtyProperty() };
			} else {
				return new Observable[] {};
			}
		});
		
		this.testCases = FXCollections.observableArrayList((testCase) -> {
			return new Observable[] { testCase.dirtyProperty() };
		});
		
		debugger.registerCoherentCallback((answerSet) -> {
			JFXUtil.runOnJFX(() -> {
				Alert a = new Alert(AlertType.INFORMATION);
				a.setTitle("Answer set found!");
				a.setHeaderText("The program is coherent (see the answer set below).");
				a.setContentText(answerSet.toString());
				a.show();
			});
		});
		
		debugger.registerCoreCallback((coreItems) -> {
			JFXUtil.runOnJFX(() -> {
				this.coreItems.clear();
				this.coreItems.addAll(coreItems);
			});
		});
		
		debugger.registerQueryCallback((queryAtoms) -> {
			JFXUtil.runOnJFX(() -> {
				this.queryAtoms.clear();
				for (String atom : queryAtoms) {
					atom = atom.replaceAll("\r", "");
					this.queryAtoms.add(new QueryViewModel(atom));
				}
			});
		});
	}
	
	public void openProject(File projectFile) {
		if (projectFile == null) return;
		
		if (!projectFile.exists()) {
			windowManager.showErrorDialog(Messages.OPENPRJ_FILE_NOT_FOUND);
			return;
		}
		
		try {
			this.project.set(projectParser.parseProject(new FileInputStream(projectFile)));
			
			this.projectFile = projectFile;
			
			Bindings.bindContent(encodings, project.get().getProgram());
			Bindings.bindContent(testCases, project.get().getTestCases());
			
			if (encodings.size() > 0) {
				selectedEncoding.set(encodings.get(0));
			}
		} catch (ProjectParsingException e) {
			windowManager.showErrorDialog(Messages.ERROR_OPEN_PROJECT, e);
		} catch (FileNotFoundException e) {
			windowManager.showErrorDialog(Messages.ERROR_OPEN_PROJECT, e);
		}
	}
	
	public void saveAction() {
		if (selectedEncoding.get() != null && selectedEncoding.get() instanceof FileEncoding) {
			FileEncoding enc = (FileEncoding) selectedEncoding.get();
			try {
				enc.save();
				
				if (isAspideSession.get())
					notifier.notifySave(enc);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (null != selectedTestCase.get()) {
			try {
				projectSerializer.serialize(project.get(), new FileOutputStream(projectFile));
				selectedTestCase.get().dirtyProperty().set(false);
			} catch (FileNotFoundException | ProjectSerializationException e) {
				windowManager.showErrorDialog(Messages.ERROR_SAVE_PROJECT, e);
			}
		}
	}

	public void addFileAction() {
		if (project.isNull().get()) return;
		
		File f = windowManager.chooseFile();
		
		if (f == null) return;
		
		Path basePath = Paths.get(project.get().getBaseDirectory());
		Path relativePath = basePath.relativize(f.toPath());
		
		Encoding encoding = new FileEncoding(basePath.toString(), relativePath.toString());
		
		project.get().getProgram().add(encoding);
		
		try {
			projectSerializer.serialize(project.get(), new FileOutputStream(projectFile));
		} catch (ProjectSerializationException | FileNotFoundException e) {
			windowManager.showErrorDialog(Messages.ERROR_SAVE_PROJECT, e);
		}

		selectedEncoding.set(encoding);
	}

	public void newTestCaseAction() {
		if (project.isNull().get()) return;
		
		windowManager.showModalDialog(new NewTestCaseViewModel(windowManager, this, project.get(), projectFile, projectSerializer));
	}
	
	public void preferencesAction() {
		windowManager.showModalDialog(new PreferencesViewModel(windowManager));
	}
	
	public void aspideAction(IndexRange selection) {
		if (!isAspideSession.get())
			return;
		
		FileEncoding enc = null;
		if (selectedEncoding.get() != null && selectedEncoding.get() instanceof FileEncoding) {
			enc = (FileEncoding) selectedEncoding.get();
		} else {
			selection = new IndexRange(0, 0);
			for (Encoding encoding : encodings()) {
				if (encoding instanceof FileEncoding) {
					enc = (FileEncoding) enc;
					break;
				}
			}
		}
		
		if (enc != null) {
			if (coreItems.size() > 0) {
				notifier.notifyBack(enc, coreItems);
			} else {
				notifier.notifyBack(enc, selection);
			}
		}
	}

	public void openAction() {
		openProject(windowManager.chooseFile());
	}

	public void newProjectAction() {
		windowManager.showModalDialog(new NewProjectViewModel(windowManager, this, projectSerializer));
	}
	
	public void exitAction() {
		Platform.exit();
	}
	
	public void debugAction(TestCase testCase) {
		try {
			debugger.startDebugger(project.get().getProgram(), testCase);
		} catch (DebuggerException e) {
			windowManager.showErrorDialog(Messages.ERROR_START_DEBUGGER, e);
		}
	}

	public void stopAction() {
		debugger.stopDebugger();
	}
	
	public void assertAction() {
		Map<String, QueryAnswer> assertions = new HashMap<String, QueryAnswer>();
		
		for (QueryViewModel query : queryAtoms) {
			if (query.getAnswer() != QueryAnswer.UNKNOWN) {
				assertions.put(query.getAtom(), query.getAnswer());
			}
		}
		
		debugger.assertAtoms(assertions);
	}

	// properties and lists
	public ObjectProperty<Encoding> selectedEncodingProperty() { return selectedEncoding; }
	public Encoding getSelectedEncoding() { return selectedEncoding.get(); }
	public void setSelectedEncoding(Encoding encoding) { selectedEncoding.set(encoding); }
	
	public ObjectProperty<TestCase> selectedTestCaseProperty() { return selectedTestCase; }
	public TestCase getSelectedTestCase() { return selectedTestCase.get(); }
	public void setSelectedTestCase(TestCase testCase) { selectedTestCase.set(testCase); }
	
	public BooleanProperty isDebuggingProperty() { return debugger.isRunning(); }
	public BooleanProperty isAspideSessions() { return this.isAspideSession; }
	public ObservableList<Encoding> encodings() { return this.encodings; }
	public ObservableList<TestCase> testCases() { return this.testCases; }
	public ObservableList<CoreItem> coreItems() { return this.coreItems; }
	public ObservableList<QueryViewModel> queryAtoms() { return this.queryAtoms; }
	
	public ObservableBooleanValue isMainPaneVisible() {
		return project.isNotNull()
			   .and(Bindings.size(encodings).greaterThan(0));
	}
	
	public ObservableBooleanValue isNoProjectPaneVisible() {
		return project.isNull();
	}
	
	public ObservableBooleanValue isEmptyProjectPaneVisible() {
		return project.isNotNull()
			   .and(Bindings.size(encodings).isEqualTo(0));
	}
	
	public ObservableBooleanValue isAddFileDisabled() {
		return project.isNull();
	}

	public ObservableBooleanValue isNewTestCaseDisabled() {
		return project.isNull();
	}

	@Override
	public String getTitle() {
		return "DWASP - ASP Debugger";
	}

	@Override
	public List<Image> getIcons() {
		return Arrays.asList(
			new Image(WindowManager.class.getResourceAsStream("dwasp-icon-16.png")),
			new Image(WindowManager.class.getResourceAsStream("dwasp-icon-32.png")),
			new Image(WindowManager.class.getResourceAsStream("dwasp-icon-64.png")));
	}

	public ObservableValue<? extends Boolean> isDebugButtonDisabled() {
		return isDebuggingProperty();
	}
}
