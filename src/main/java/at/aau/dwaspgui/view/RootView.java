package at.aau.dwaspgui.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import at.aau.dwaspgui.view.highlight.AspCore2Highlight;
import at.aau.dwaspgui.viewmodel.RootViewModel;
import at.aau.dwaspgui.viewmodel.project.AbstractProjectItemViewModel;

public class RootView extends AbstractView<RootViewModel> {
	// JavaFX controls
	@FXML private TreeView<AbstractProjectItemViewModel> projectTreeView;
	@FXML private CodeArea codeArea;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		viewModel.projectProperty().addListener(
				(obs, oldProject, newProject) -> {
					projectChanged(newProject);
				});
		
		initializeProjectView();
		initializeCodeArea();
	}

	private void initializeProjectView() {
		projectTreeView.getSelectionModel().selectedItemProperty().addListener(
				(obs, oldProjectItem, newProjectItem) -> {
					if (newProjectItem.getValue().isEditable()) {
						codeArea.replaceText(newProjectItem.getValue().getContent());
					}
				});
	}
	
	private void initializeCodeArea() {
		codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
		codeArea.textProperty().addListener((obs, oldText, newText) -> {
            codeArea.setStyleSpans(0, AspCore2Highlight.computeHighlighting(newText));
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
	public void runAction() {
		
	}
	
	@FXML
	public void debugAction() {
		
	}
}
