/*
 *  Copyright 2015 Philip Gasteiger
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
import at.aau.dwaspgui.view.history.HistoryListView;
import at.aau.dwaspgui.view.query.QueryListView;
import at.aau.dwaspgui.viewmodel.RootViewModel;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
	@FXML private ResizingListView<Encoding> projectListView;
	@FXML private ResizingListView<TestCase> testCaseListView;
	@FXML private QueryListView queryListView;
	@FXML private HistoryListView historyListView;
	@FXML private VBox queryView;
	@FXML private CodeArea codeArea;
	@FXML private MenuButton debugButton;
	@FXML private MenuItem saveMenuItem;
	@FXML private MenuItem addFileMenuItem;
	@FXML private MenuItem newTestCaseMenuItem;
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
		initDebugButton();
		
		// add listener for the core highlight inside the editor
		viewModel.coreItems().addListener((ListChangeListener.Change<? extends CoreItem> c) -> {
			JFXUtil.runOnJFX(() -> {
				if (null != viewModel.getSelectedEncoding()) {
					codeArea.setStyleSpans(0, AspCore2Highlight.computeHighlighting(viewModel.getSelectedEncoding(), codeArea.getText(), viewModel.coreItems()));
				} else if (null != viewModel.getSelectedTestCase()) {
					codeArea.setStyleSpans(0, AspCore2Highlight.computeHighlighting(codeArea.getText()));
				}
			});
		});
		
		mainPane.visibleProperty().bind(viewModel.isMainPaneVisible());
		mainPane.managedProperty().bind(mainPane.visibleProperty());
		
		noProjectPane.visibleProperty().bind(viewModel.isNoProjectPaneVisible());
		noProjectPane.managedProperty().bind(noProjectPane.visibleProperty());
		
		emptyProjectPane.visibleProperty().bind(viewModel.isEmptyProjectPaneVisible());
		emptyProjectPane.managedProperty().bind(emptyProjectPane.visibleProperty());
		
		addFileMenuItem.disableProperty().bind(viewModel.isAddFileDisabled());
		newTestCaseMenuItem.disableProperty().bind(viewModel.isNewTestCaseDisabled());
		
		saveMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN));
		saveMenuItem.disableProperty().bind(dirtyEncoding.not());
		aspideButton.managedProperty().bind(aspideButton.visibleProperty());
		aspideButton.visibleProperty().bind(viewModel.isAspideSessions());
		saveButton.disableProperty().bind(dirtyEncoding.not());
		debugButton.disableProperty().bind(viewModel.isDebugButtonDisabled());
		stopButton.visibleProperty().bind(viewModel.isDebuggingProperty());
		queryView.visibleProperty().bind(viewModel.isDebuggingProperty());

		Bindings.bindContentBidirectional(viewModel.queryAtoms(), queryListView.getQueries());
		Bindings.bindContentBidirectional(viewModel.history(), historyListView.getHistory());
		
		initializeCodeArea();
	}
	
	private void initDebugButton() {
		if (viewModel.testCases().size() == 0) {
			debugButton.getItems().add(createDebugMenuItem(new TestCase("No test case", "")));
		}
		
		viewModel.testCases().addListener((InvalidationListener) observable -> {
			debugButton.getItems().clear();
			
			if (viewModel.testCases().size() == 0) {
				// add one menu item for starting the debugger without an test-case
				debugButton.getItems().add(createDebugMenuItem(new TestCase("No test case", "")));
			}
			
			for(TestCase testCase : viewModel.testCases()) {
				debugButton.getItems().add(createDebugMenuItem(testCase));
			}
		});
	}
	
	private MenuItem createDebugMenuItem(TestCase testCase) {
		MenuItem item = new MenuItem(testCase.getName());
		item.setOnAction(e -> viewModel.debugAction(testCase));
		return item;
	}

	private void initProjectListView() {
		projectListView.itemsProperty().set(viewModel.encodings());
		
		viewModel.selectedEncodingProperty().addListener((ChangeListener<Encoding>) (observable, oldEncoding, newEncoding) -> {
			projectListView.getSelectionModel().select(newEncoding);
			
			if (null == newEncoding) return;
			
			viewModel.setSelectedTestCase(null);
			editableEncoding.set(newEncoding instanceof FileEncoding);
			
			codeArea.replaceText(newEncoding.getContent());
			codeArea.getUndoManager().forgetHistory();
			
			if (newEncoding instanceof FileEncoding) {
				FileEncoding enc = (FileEncoding) newEncoding;
				
				dirtyEncoding.bind(enc.dirtyProperty());
			} else {
				dirtyEncoding.unbind();
				dirtyEncoding.set(false);
			}
		});
		
		projectListView.getSelectionModel().selectedItemProperty().addListener((obs, oldProjectItem, newProjectItem) -> {
			viewModel.setSelectedEncoding(newProjectItem);
		});
	}

	private void initTestCaseListView() {
		testCaseListView.itemsProperty().set(viewModel.testCases());
		
		viewModel.selectedTestCaseProperty().addListener((ChangeListener<TestCase>) (observable, oldTestCase, newTestCase) -> {
			testCaseListView.getSelectionModel().select(newTestCase);
			
			if (null == newTestCase) return;
			
			viewModel.setSelectedEncoding(null);
			editableEncoding.set(true);
			dirtyEncoding.bind(newTestCase.dirtyProperty());
			
			codeArea.replaceText(newTestCase.getAssertions());
			codeArea.getUndoManager().forgetHistory();
		});

		testCaseListView.getSelectionModel().selectedItemProperty().addListener((obs, oldTC, newTC) -> {
			viewModel.setSelectedTestCase(newTC);
		});
	}

	private void initializeCodeArea() {
		codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
		codeArea.editableProperty().bind(viewModel.isDebuggingProperty().not().and(editableEncoding));
		codeArea.textProperty().addListener((InvalidationListener) observable -> {
			if (null != viewModel.getSelectedEncoding()) {
				codeArea.setStyleSpans(0, AspCore2Highlight.computeHighlighting(viewModel.getSelectedEncoding(), codeArea.getText(), viewModel.coreItems()));
			} else if (null != viewModel.getSelectedTestCase()) {
				codeArea.setStyleSpans(0, AspCore2Highlight.computeHighlighting(codeArea.getText()));
			}		
		});
		codeArea.textProperty().addListener((obs, oldText, newText) -> {
			if (!editableEncoding.get()) return;
			
			if (null != viewModel.getSelectedEncoding()) {
            	FileEncoding enc = (FileEncoding) viewModel.getSelectedEncoding();
            	enc.setContent(newText);				
			} else if (null != viewModel.getSelectedTestCase()) {
				TestCase tc = viewModel.getSelectedTestCase();
				tc.setAssertions(newText);
			}
		});
			
		Popup popup = new Popup();
		Label popupMsg = new Label();
		popupMsg.getStylesheets().add("/at/aau/dwaspgui/view/popup.css");
		popupMsg.getStyleClass().add("popup");
		popup.getContent().add(popupMsg);
		
		codeArea.setMouseOverTextDelay(Duration.ofSeconds(1));
		codeArea.addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_BEGIN, e -> {
			if (!viewModel.isDebuggingProperty().get()) return;
			
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
	@FXML public void addFileAction() { viewModel.addFileAction(); }
	@FXML public void saveAction() { viewModel.saveAction(); }
	@FXML public void aspideAction() { viewModel.aspideAction(codeArea.getSelection()); }
	@FXML public void openProjectAction() { viewModel.openAction(); }
	@FXML public void exitAction() { viewModel.exitAction(); }
	@FXML public void stopAction() { viewModel.stopAction(); }
	@FXML public void preferencesAction() { viewModel.preferencesAction(); }
	@FXML public void assertAction() { viewModel.assertAction(); }
	@FXML public void newTestCaseAction() { viewModel.newTestCaseAction(); }
}
