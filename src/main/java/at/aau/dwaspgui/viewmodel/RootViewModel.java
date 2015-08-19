package at.aau.dwaspgui.viewmodel;

import java.io.File;
import java.io.IOException;
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
import at.aau.dwaspgui.util.JFXUtil;
import at.aau.dwaspgui.util.Messages;
import at.aau.dwaspgui.viewmodel.query.QueryViewModel;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
	private final Debugger debugger;
	private final AspideNotifier notifier;
	private Project project;
	private BooleanProperty isAspideSession = new SimpleBooleanProperty(false);
	private ObjectProperty<Encoding> selectedEncoding = new SimpleObjectProperty<Encoding>();
	private ObservableList<Encoding> encodings = FXCollections.observableArrayList();
	private ObservableList<TestCase> testCases = FXCollections.observableArrayList();
	private ObservableList<CoreItem> coreItems = FXCollections.observableArrayList();
	private ObservableList<QueryViewModel> queryAtoms = FXCollections.observableArrayList();
	
	@Inject
	public RootViewModel(WindowManager windowManager,
			ProjectParser projectParser, Debugger debugger, 
			AspideNotifier notifier) {
		this.windowManager = windowManager;
		this.projectParser = projectParser;
		this.debugger = debugger;
		this.notifier = notifier;
		
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
			this.project = projectParser.parseProject(projectFile);
			
			encodings.clear();
			encodings.addAll(project.getProgram());
			
			testCases.clear();
			testCases.addAll(project.getTestCases());
			
			if (encodings.size() > 0) {
				selectedEncoding.set(encodings.get(0));
			}
		} catch (ProjectParsingException e) {
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
		}
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
			notifier.notifyBack(enc, selection);
		}
	}

	public void openAction() {
		openProject(windowManager.chooseFile());
	}

	public void newProjectAction() {
		windowManager.showModalDialog(new NewProjectViewModel(windowManager, this));
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
	public BooleanProperty isDebuggingProperty() { return debugger.isRunning(); }
	public BooleanProperty isAspideSessions() { return this.isAspideSession; }
	public ObservableList<Encoding> encodings() { return this.encodings; }
	public ObservableList<TestCase> testCases() { return this.testCases; }
	public ObservableList<CoreItem> coreItems() { return this.coreItems; }
	public ObservableList<QueryViewModel> queryAtoms() { return this.queryAtoms; }

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
}
