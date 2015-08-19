package at.aau.dwaspgui.view;

import java.net.URL;
import java.util.ResourceBundle;

import at.aau.dwaspgui.viewmodel.NewProjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class NewProjectView extends AbstractView<NewProjectViewModel> {
	@FXML TextField nameTextField;
	@FXML TextField locationTextField;
	@FXML Button createButton;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nameTextField.textProperty().bindBidirectional(viewModel.projectNameProperty());
		locationTextField.textProperty().bindBidirectional(viewModel.locationProperty());
		createButton.disableProperty().bind(viewModel.createDisabledProperty());
		
	}
	
	@FXML public void chooseLocationAction() { viewModel.chooseLocation(); }
	@FXML public void cancelAction() { viewModel.cancel(); }
	@FXML public void createAction() { viewModel.create(); }
}
