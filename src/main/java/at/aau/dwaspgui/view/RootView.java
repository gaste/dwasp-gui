package at.aau.dwaspgui.view;

import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.ResourceBundle;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.MouseOverTextEvent;

import at.aau.dwaspgui.domain.CoreItem;
import at.aau.dwaspgui.domain.Encoding;
import at.aau.dwaspgui.domain.FileEncoding;
import at.aau.dwaspgui.domain.TestCase;
import at.aau.dwaspgui.util.JFXUtil;
import at.aau.dwaspgui.view.highlight.AspCore2Highlight;
import at.aau.dwaspgui.view.query.QueryListView;
import at.aau.dwaspgui.viewmodel.RootViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

public class RootView extends AbstractView<RootViewModel> {
	// JavaFX controls
	@FXML private ListView<Encoding> projectListView;
	@FXML private ListView<TestCase> testCaseListView;
	@FXML private QueryListView queryListView;
	@FXML private VBox queryView;
	@FXML private CodeArea codeArea;
	@FXML private MenuButton debugButton;
	@FXML private MenuItem saveMenuItem;
	@FXML private MenuItem newFileMenuItem;
	@FXML private MenuItem addFileMenuItem;
	@FXML private Button saveButton;
	@FXML private Button aspideButton;
	@FXML private Button stopButton;
	@FXML private Button assertButton;
	@FXML private Pane mainPane;
	@FXML private Pane noProjectPane;
	@FXML private Pane emptyProjectPane;
	
	private BooleanProperty dirtyEncoding = new SimpleBooleanProperty(false);
	private BooleanProperty editableEncoding = new SimpleBooleanProperty(false);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initProjectListView();
		initTestCaseListView();

		// refresh test cases from the menu
		viewModel.testCases().addListener((ListChangeListener.Change<? extends TestCase> c) -> {
			debugButton.getItems().clear();
			
			for(TestCase testCase : viewModel.testCases()) {
				MenuItem item = new MenuItem(testCase.getName());
				item.setOnAction(e -> viewModel.debugAction(testCase));

				debugButton.getItems().add(item);
			}
		});
		
		// add listener for the core highlight inside the editor
		viewModel.coreItems().addListener((ListChangeListener.Change<? extends CoreItem> c) -> {
			JFXUtil.runOnJFX(() -> {
				codeArea.setStyleSpans(0, AspCore2Highlight.computeHighlighting(viewModel.selectedEncodingProperty().get(), codeArea.getText(), viewModel.coreItems()));
			});
		});
		
		mainPane.visibleProperty().bind(viewModel.isMainPaneVisible());
		mainPane.managedProperty().bind(mainPane.visibleProperty());
		
		noProjectPane.visibleProperty().bind(viewModel.isNoProjectPaneVisible());
		noProjectPane.managedProperty().bind(noProjectPane.visibleProperty());
		
		emptyProjectPane.visibleProperty().bind(viewModel.isEmptyProjectPaneVisible());
		emptyProjectPane.managedProperty().bind(emptyProjectPane.visibleProperty());
		
		newFileMenuItem.disableProperty().bind(viewModel.isNewFileDisabled());
		addFileMenuItem.disableProperty().bind(viewModel.isAddFileDisabled());
		
		saveMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN));
		saveMenuItem.disableProperty().bind(dirtyEncoding.not());
		aspideButton.managedProperty().bind(aspideButton.visibleProperty());
		aspideButton.visibleProperty().bind(viewModel.isAspideSessions());
		saveButton.disableProperty().bind(dirtyEncoding.not());
		debugButton.disableProperty().bind(viewModel.isDebuggingProperty());
		stopButton.visibleProperty().bind(viewModel.isDebuggingProperty());
		queryView.visibleProperty().bind(viewModel.isDebuggingProperty());

		Bindings.bindContentBidirectional(viewModel.queryAtoms(), queryListView.getQueries());
		
		initializeCodeArea();
	}

	private void initProjectListView() {
		projectListView.itemsProperty().set(viewModel.encodings());
		//projectListView.prefHeightProperty().bind(Bindings.size(viewModel.encodings()).multiply(29));
		
		viewModel.selectedEncodingProperty().addListener((ChangeListener<Encoding>) (observable, oldEncoding, newEncoding) -> {
			projectListView.getSelectionModel().select(newEncoding);
			editableEncoding.set(newEncoding instanceof FileEncoding);
			
			if (newEncoding instanceof FileEncoding) {
				FileEncoding enc = (FileEncoding) newEncoding;
				
				dirtyEncoding.unbind();
				dirtyEncoding.bind(enc.dirtyProperty());
			} else {
				dirtyEncoding.unbind();
				dirtyEncoding.set(false);
			}
		});
		
		projectListView.getSelectionModel().selectedItemProperty().addListener((obs, oldProjectItem, newProjectItem) -> {
			if (newProjectItem == null) return;	
			else testCaseListView.getSelectionModel().clearSelection();
			
			viewModel.selectedEncodingProperty().set(newProjectItem);
			
			codeArea.replaceText(newProjectItem.getContent());
			codeArea.getUndoManager().forgetHistory();
		});
	}

	private void initTestCaseListView() {
		testCaseListView.itemsProperty().set(viewModel.testCases());
		//testCaseListView.prefHeightProperty().bind(Bindings.size(viewModel.testCases()).multiply(29));

		testCaseListView.getSelectionModel().selectedItemProperty().addListener((obs, oldTC, newTC) -> {
			if (newTC == null) return;	
			
			projectListView.getSelectionModel().clearSelection();

			viewModel.selectedEncodingProperty().set(null);
			
			codeArea.replaceText(newTC.getAssertions());
			codeArea.getUndoManager().forgetHistory();
		});
	}

	private void initializeCodeArea() {
		codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
		codeArea.editableProperty().bind(viewModel.isDebuggingProperty().not().and(editableEncoding));
		codeArea.textProperty().addListener((obs, oldText, newText) -> {
            codeArea.setStyleSpans(0, AspCore2Highlight.computeHighlighting(viewModel.selectedEncodingProperty().get(), newText, viewModel.coreItems()));
            
            if (editableEncoding.get()) {
            	FileEncoding enc = (FileEncoding) viewModel.selectedEncodingProperty().get();
            	enc.setContent(newText);
            }
		});
			
		Popup popup = new Popup();
		Label popupMsg = new Label();
		popupMsg.getStylesheets().add("/at/aau/dwaspgui/view/popup.css");
		popupMsg.getStyleClass().add("popup");
		popup.getContent().add(popupMsg);
		
		codeArea.setMouseOverTextDelay(Duration.ofSeconds(1));
		codeArea.addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_BEGIN, e -> {
			if (!viewModel.isDebuggingProperty().get())
				return;
			
			int chIdx = e.getCharacterIndex();
			
			for (CoreItem ci : viewModel.coreItems().filtered(ci -> ci.getEncoding() == viewModel.selectedEncodingProperty().get())) {
				if (chIdx >= ci.getFromIndex() && chIdx <= ci.getFromIndex() + ci.getLength()) {
					StringBuilder popupText = new StringBuilder();
					popupText.append(ci.getSubstitutions().toString());
					popupText.append("\n\n");
					for (Map<String, String> substitution : ci.getSubstitutions()) {
						popupText.append(ci.getRule().getGroundedRule(substitution));
						popupText.append("\n");
					}
					
					popupMsg.setText(popupText.toString());
					popup.show(codeArea, e.getScreenPosition().getX(), e.getScreenPosition().getY() + 10);
				}
			}
		});
		codeArea.addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_END, e -> {
			popup.hide();
		});
	}
	
	@FXML public void newProjectAction() { viewModel.newProjectAction(); }
	@FXML public void newFileAction() { viewModel.newFileAction(); }
	@FXML public void addFileAction() { viewModel.addFileAction(); }
	@FXML public void saveAction() { viewModel.saveAction(); }
	@FXML public void aspideAction() { viewModel.aspideAction(codeArea.getSelection()); }
	@FXML public void openProjectAction() { viewModel.openAction(); }
	@FXML public void exitAction() { viewModel.exitAction(); }
	@FXML public void stopAction() { viewModel.stopAction(); }
	@FXML public void preferencesAction() { viewModel.preferencesAction(); }
	@FXML public void assertAction() { viewModel.assertAction(); }
}
