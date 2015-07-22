package at.aau.dwaspgui.view;

import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Popup;
import javafx.util.Pair;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.MouseOverTextEvent;

import at.aau.dwaspgui.domain.CoreItem;
import at.aau.dwaspgui.domain.QueryAnswer;
import at.aau.dwaspgui.domain.TestCase;
import at.aau.dwaspgui.util.JFXUtil;
import at.aau.dwaspgui.view.highlight.AspCore2Highlight;
import at.aau.dwaspgui.viewmodel.RootViewModel;
import at.aau.dwaspgui.viewmodel.project.AbstractProjectItemViewModel;

public class RootView extends AbstractView<RootViewModel> {
	// JavaFX controls
	@FXML private TreeView<AbstractProjectItemViewModel> projectTreeView;
	@FXML private ListView<String> queryListView;
	@FXML private CodeArea codeArea;
	@FXML private MenuButton debugButton;
	@FXML private Button stopButton;
	@FXML private Button assertButton;
	
	private Map<String, QueryAnswer> assertions = new HashMap<String, QueryAnswer>();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		viewModel.projectProperty().addListener(
				(obs, oldProject, newProject) -> {
					projectChanged(newProject);
				});
		
		viewModel.testCases().addListener(new ListChangeListener<TestCase> () {
			@Override
			public void onChanged(Change<? extends TestCase> c) {
				debugButton.getItems().clear();
				for(TestCase tc : viewModel.testCases()) {
					MenuItem item = new MenuItem(tc.getName());
					item.setOnAction((e) -> {
						viewModel.debugAction(tc);
					});
					debugButton.getItems().add(item);
				}
			}
		});
		
		viewModel.coreItems().addListener(new ListChangeListener<CoreItem>() {
			@Override
			public void onChanged(Change<? extends CoreItem> c) {
				JFXUtil.runOnJFX(() -> {
					codeArea.setStyleSpans(0, AspCore2Highlight.computeHighlighting(viewModel.selectedEncoding().get(), codeArea.getText(), viewModel.coreItems()));
				});
			}
		});
		
		debugButton.disableProperty().bind(viewModel.isDebugging());
		stopButton.visibleProperty().bind(viewModel.isDebugging());
		assertButton.visibleProperty().bind(viewModel.isDebugging());
		queryListView.visibleProperty().bind(viewModel.isDebugging());
		
		queryListView.setItems(viewModel.queryAtoms());
		queryListView.setCellFactory((list) -> {
			return new ListCell<String>() {
				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					
					System.out.println("in update item: Item = '" + item + "'");
					
					if (item != null && item != "") {
						ToggleGroup group = new ToggleGroup();

						ToggleButton yes = new ToggleButton("yes");
						yes.getStyleClass().add("btn-true");
						yes.setToggleGroup(group);
						yes.selectedProperty().addListener((o) -> {
							assertions.put(item, QueryAnswer.YES);
						});

						ToggleButton no = new ToggleButton("no");
						no.getStyleClass().add("btn-false");
						no.setToggleGroup(group);
						no.selectedProperty().addListener((o) -> {
							assertions.put(item, QueryAnswer.NO);
						});

						ToggleButton undef = new ToggleButton("undef");
						undef.getStyleClass().add("btn-undef");
						undef.setToggleGroup(group);
						undef.setSelected(true);
						undef.selectedProperty().addListener((o) -> {
							assertions.put(item, QueryAnswer.UNKNOWN);
						});
						
						HBox toggleBox = new HBox(yes, no, undef);
						HBox labelBox = new HBox(new Label(item));
						HBox hbox = new HBox(labelBox, toggleBox);
						HBox.setHgrow(labelBox, Priority.ALWAYS);
						hbox.setSpacing(10);
                        setGraphic(hbox);
					} else {
						setGraphic(new HBox());
					}
				}
			};
		});
		
		initializeProjectView();
		initializeCodeArea();
	}

	private void initializeProjectView() {
		projectTreeView.getSelectionModel().selectedItemProperty().addListener(
			(obs, oldProjectItem, newProjectItem) -> {
				if (newProjectItem.getValue().isEncoding()) {
					viewModel.selectedEncoding().set(newProjectItem.getValue().getEncoding());
				} else {
					viewModel.selectedEncoding().set(null);
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
            codeArea.setStyleSpans(0, AspCore2Highlight.computeHighlighting(viewModel.selectedEncoding().get(), newText, viewModel.coreItems()));
        });
		codeArea.editableProperty().bind(viewModel.isDebugging().not());
				
		Popup popup = new Popup();
		Label popupMsg = new Label();
		popupMsg.getStylesheets().add("/at/aau/dwaspgui/view/popup.css");
		popupMsg.getStyleClass().add("popup");
		popup.getContent().add(popupMsg);
		
		codeArea.setMouseOverTextDelay(Duration.ofSeconds(1));
		codeArea.addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_BEGIN, e -> {
			if (viewModel.isDebugging().get()) {
				int chIdx = e.getCharacterIndex();
				
				for (CoreItem ci : viewModel.coreItems().filtered(ci -> ci.getEncoding() == viewModel.selectedEncoding().get())) {
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
	
	@FXML
	public void openAction() {
		viewModel.openAction();
	}
	
	@FXML
	public void exitAction() {
		viewModel.exitAction();
	}
	
	@FXML
	public void stopAction() {
		viewModel.stopAction();
	}
	
	@FXML
	public void assertAction() {
		List<Pair<String, QueryAnswer>> a = new ArrayList<Pair<String,QueryAnswer>>();
		assertions.forEach((atom, answer) -> {
			if (answer != QueryAnswer.UNKNOWN){
				a.add(new Pair<String, QueryAnswer>(atom, answer));
			}
		});
		viewModel.assertAction(a);
	}
}
