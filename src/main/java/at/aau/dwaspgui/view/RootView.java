package at.aau.dwaspgui.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import at.aau.dwaspgui.domain.CoreItem;
import at.aau.dwaspgui.domain.TestCase;
import at.aau.dwaspgui.util.JFXUtil;
import at.aau.dwaspgui.view.highlight.AspCore2Highlight;
import at.aau.dwaspgui.viewmodel.RootViewModel;
import at.aau.dwaspgui.viewmodel.project.AbstractProjectItemViewModel;

public class RootView extends AbstractView<RootViewModel> {
	// JavaFX controls
	@FXML private TreeView<AbstractProjectItemViewModel> projectTreeView;
	@FXML private CodeArea codeArea;
	@FXML private MenuButton debugButton;
	@FXML private Button stopButton;
	@FXML private Button askButton;
	
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
		askButton.visibleProperty().bind(viewModel.isDebugging());
		
		initializeProjectView();
		initializeCodeArea();
	}

	private void initializeProjectView() {
		projectTreeView.getSelectionModel().selectedItemProperty().addListener(
				(obs, oldProjectItem, newProjectItem) -> {
					if (newProjectItem.getValue().isEncoding()) {
						viewModel.selectedEncoding().set(newProjectItem.getValue().getEncoding());
					}
					
					if (newProjectItem.getValue().isEditable()) {
						codeArea.replaceText(newProjectItem.getValue().getContent());
						codeArea.getUndoManager().forgetHistory();
					}
				});
	}
	
	private void initializeCodeArea() {
		codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
		codeArea.textProperty().addListener((obs, oldText, newText) -> {
            codeArea.setStyleSpans(0, AspCore2Highlight.computeHighlighting(viewModel.selectedEncoding().get(), newText, viewModel.coreItems()));
        });
	}
	
	private void projectChanged(AbstractProjectItemViewModel project) {
		TreeItem<AbstractProjectItemViewModel> root = new TreeItem<AbstractProjectItemViewModel>(project);
		
		addChildren(root, project.getChildren());
		
		projectTreeView.setRoot(root);
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
	public void askAction() {
		viewModel.askAction();
	}
}
