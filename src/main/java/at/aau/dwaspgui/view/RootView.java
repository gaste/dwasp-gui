package at.aau.dwaspgui.view;

import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.ResourceBundle;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.MouseOverTextEvent;

import at.aau.dwaspgui.domain.CoreItem;
import at.aau.dwaspgui.domain.TestCase;
import at.aau.dwaspgui.util.JFXUtil;
import at.aau.dwaspgui.view.highlight.AspCore2Highlight;
import at.aau.dwaspgui.view.query.QueryListView;
import at.aau.dwaspgui.viewmodel.RootViewModel;
import at.aau.dwaspgui.viewmodel.project.AbstractProjectItemViewModel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Popup;

public class RootView extends AbstractView<RootViewModel> {
	// JavaFX controls
	@FXML private TreeView<AbstractProjectItemViewModel> projectTreeView;
	@FXML private QueryListView queryListView;
	@FXML private CodeArea codeArea;
	@FXML private MenuButton debugButton;
	@FXML private Button stopButton;
	@FXML private Button assertButton;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// refresh the project
		viewModel.projectProperty().addListener((obs, oldProject, newProject) -> {
			projectChanged(newProject);
		});

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
		
		debugButton.disableProperty().bind(viewModel.isDebuggingProperty());
		stopButton.visibleProperty().bind(viewModel.isDebuggingProperty());
		assertButton.visibleProperty().bind(viewModel.isDebuggingProperty());
		queryListView.visibleProperty().bind(viewModel.isDebuggingProperty());

		Bindings.bindContentBidirectional(viewModel.queryAtoms(), queryListView.getQueries());
		
		initializeProjectView();
		initializeCodeArea();
	}

	private void initializeProjectView() {
		projectTreeView.getSelectionModel().selectedItemProperty().addListener((obs, oldProjectItem, newProjectItem) -> {
			if (newProjectItem.getValue().isEncoding()) {
				viewModel.selectedEncodingProperty().set(newProjectItem.getValue().getEncoding());
			} else {
				viewModel.selectedEncodingProperty().set(null);
			}
			
			if (newProjectItem.getValue().isEditable()) {
				codeArea.replaceText(newProjectItem.getValue().getContent());
				codeArea.getUndoManager().forgetHistory();
			}
			
			// non-editable project items are not selectable
			if (!newProjectItem.getValue().isEditable()) {
				Platform.runLater(() -> projectTreeView.getSelectionModel().select(oldProjectItem));
			}
		});
	}
	
	private void initializeCodeArea() {
		codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
		codeArea.textProperty().addListener((obs, oldText, newText) -> {
            codeArea.setStyleSpans(0, AspCore2Highlight.computeHighlighting(viewModel.selectedEncodingProperty().get(), newText, viewModel.coreItems()));
        });
		codeArea.editableProperty().bind(viewModel.isDebuggingProperty().not());
				
		Popup popup = new Popup();
		Label popupMsg = new Label();
		popupMsg.getStylesheets().add("/at/aau/dwaspgui/view/popup.css");
		popupMsg.getStyleClass().add("popup");
		popup.getContent().add(popupMsg);
		
		codeArea.setMouseOverTextDelay(Duration.ofSeconds(1));
		codeArea.addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_BEGIN, e -> {
			if (viewModel.isDebuggingProperty().get()) {
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
			}
		});
		codeArea.addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_END, e -> {
			popup.hide();
		});
	}
	
	private void projectChanged(AbstractProjectItemViewModel project) {
		TreeItem<AbstractProjectItemViewModel> root = new TreeItem<AbstractProjectItemViewModel>(project);
		
		addChildren(root, project.getChildren());
		
		projectTreeView.setRoot(root);
		projectTreeView.getSelectionModel().select(0);
		
	}
	
	private void addChildren(TreeItem<AbstractProjectItemViewModel> parent, ObservableList<AbstractProjectItemViewModel> children) {
		for (AbstractProjectItemViewModel child : children) {
			TreeItem<AbstractProjectItemViewModel> childItem = new TreeItem<AbstractProjectItemViewModel>(child);
			addChildren(childItem, child.getChildren());
			parent.getChildren().add(childItem);
		}
	}
	
	@FXML public void openAction() { viewModel.openAction(); }
	@FXML public void exitAction() { viewModel.exitAction(); }
	@FXML public void stopAction() { viewModel.stopAction(); }
	@FXML public void assertAction() { viewModel.assertAction(); }
}
